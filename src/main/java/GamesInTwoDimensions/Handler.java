package GamesInTwoDimensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class Handler is used to hold all objects in the game, looping through each of them in its tick() and render() functions.
 * @author jensh
 * @version 1.0
 */
public class Handler {
    private ArrayList<GameObject> objects;
    private CollisionHandler collisionHandler;

    public Handler() {
        this.objects = new ArrayList<>();
        this.collisionHandler = new CollisionHandler();
    }

    /**
     * Method tick()
     * This method is called from the game loop in the Game class
     * It is used to update the variables of GameObjects currently this class's objects list.
     * @param delta     delta consists of how long the loop update took divided by OPTIMAL_TIME which is a double
     *                  that describes how long each update should take.
     *                  This variable is used to update GameObjects variables in a way that keeps the different
     *                  processing power of different computers in mind.
     */
    public void tick(double delta){
        for (GameObject object : objects) {
            object.tick(delta);
            if (object instanceof ICollidable){
                collisionHandler.addCollidable(object);
            }
        }
        collisionHandler.tick();
    }

    /**
     * Method render()
     * This method is called from the game loop in the Game class
     * It is used to update/render the graphics of a GameObject
     * GameObjects have a boolean variable "visible", if that is set to false then the object will not be rendered
     * @param g         Graphics2D object, which Canvas uses to render graphics in our window.
     * @see java.awt.Canvas
     * @see java.awt.Graphics2D
     */
    public void render(Graphics2D g){
        for (GameObject object : objects) {
            if(object.isVisible())
                object.render(g);
        }
    }

    public void addObject(GameObject object){
        objects.add(object);
    }

    public void addObjects(ArrayList<GameObject>gameObjects){
        for(GameObject gameObject : gameObjects){
            addObject(gameObject);
        }
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    //Might remove return. But it might be good for debugging
    public GameObject removeObject(GameObject object) {
        objects.remove(object);
        return object;
    }

    @Override
    public String toString() {

        String result = null;
        try {
            ObjectMapper om = new ObjectMapper();
            om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            result = om.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }
}
