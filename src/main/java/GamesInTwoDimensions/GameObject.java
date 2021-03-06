package GamesInTwoDimensions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is designed to be the base for all future GameObjects.
 * It holds basic information about the Object.
 * @author emilyhbh (Emily Healey)
 * @author jenshr (Jens Rage)
 * @version 0.1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public abstract class GameObject {

    private static List<GameObject> gameObjInstances = new ArrayList();

    private int x;
    private int y;
    private int velocityX;
    private int velocityY;
    private int width, height;
    private int movementSpeed;
    private Sprite sprite;
    private boolean isVisible;
    private HashMap<Integer, Runnable> keyCommands;

    public GameObject(){
        // default values
        x = 0;
        y = 0;
        isVisible = true;
        keyCommands = new HashMap<>();
        movementSpeed = 1;
        gameObjInstances.add(this);
    }

    public GameObject(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.height = this.sprite.image.getHeight(null);
        this.width = this.sprite.image.getWidth(null);
        isVisible = true;
        keyCommands = new HashMap<>();
        movementSpeed = 1;
        gameObjInstances.add(this);
    }

    public GameObject(int x, int y, Sprite sprite, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        movementSpeed = 1;
    }

    /**
     * Assign a key and runnable pair to a hashmap. So the key can be used to run the function later by the interact() function.
     * @param keyCode keyCode is an Integer value that corresponds to a key on a keyboard
     * @param keyCommand keyCommand Runnable object. Think of it as a function, this function will be kept to be ran later.
     */
    public void addKeyCommand(Integer keyCode, Runnable keyCommand) {
        keyCommands.put(keyCode, keyCommand);
    }

    public void removeKeyCommand(Integer keycode){
        keyCommands.remove(keycode);
    }

    /**
     * Method tick()
     * This method is called from the Handler class,
     * it exists to update required variables throughout all GameObjects currently in the game.
     * Object deriving from this class will require to override this method,
     * this is where logic is decided for what where and which variables will be updated.
     * @param delta     delta consists of how long the loop update took divided by OPTIMAL_TIME which is a double
     *                  that describes how long each update should take.
     *                  This variable is used to update GameObjects variables in a way that keeps the different
     *                  processing power of different computers in mind.
     */
    public abstract void tick(double delta);

    /**
     * Method render()
     * This method is called from the Handler class,
     * it is used to update/render the graphics of a GameObject,
     * Object deriving from this class will require to override this method,
     * this is where logic is decided for what where and when the graphic is rendered.
     * @param g         Graphics2D object, which Canvas uses to render graphics in our window.
     * @see java.awt.Canvas
     * @see java.awt.Graphics2D
     */
    public abstract void render(Graphics2D g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        checkBounds();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        checkBounds();
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.sprite.setWidth(width);
        this.sprite.setHeight(height);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void visible(){
        isVisible = true;
    }

    public void invisible(){
        isVisible = false;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;

        if(sprite != null)
            sprite.setWidth(width);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;

        if(sprite != null)
            sprite.setHeight(height);
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;

        if(sprite != null){
            sprite.setWidth(width);
            sprite.setHeight(height);
        }
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Function checkBounds() compares position with width & height in the calculation to see whether the object is within game bounds.
     * At current state, only check the left and top constraint of the game
     */
    private void checkBounds(){
        isVisible = x + width >= 0 && y + height >= 0;
    }

    /**
     * Function interact(), used to trigger the correct function assigned to the keypress sent, by looking through the keyCommand HashMap.
     * @param e KeyEvent e, used to get the keyCode()
     */
    @KeyPressed
    public static void interact(KeyEvent e){

        Integer keyCode = e.getKeyCode();

        for(GameObject go : gameObjInstances){
            if (go.keyCommands.containsKey(keyCode))
                go.keyCommands.get(keyCode).run();
        }
    }

    @Override
    public String toString() {

        String result;
        try {
            result = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }
}
