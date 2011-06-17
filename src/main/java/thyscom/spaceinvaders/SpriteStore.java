package thyscom.spaceinvaders;

import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * A resource manager for sprites in the game. Its often quite important
 * how and where you getInstance your game resources from. In most cases
 * it makes sense to have a central resource loader that goes away, gets
 * your resources and caches them for future use.
 * <p>
 * [singleton]
 * <p>
 * @author Kevin Glass
 * @author Thys Meintjes
 */
public class SpriteStore {

    private static final Logger logger = Logger.getLogger(SpriteStore.class);
    /** The single instance of this class */
    private static SpriteStore single = new SpriteStore();
    /** The cached sprite map, from reference to sprite instance */
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private SpriteFactory factory;
    
    private SpriteStore() {
        factory = new SpriteFactory();
    }
    
    /**
     * Get the single instance of this class 
     * @return The single instance of this class
     */
    public static SpriteStore getInstance() {
        return single;
    }

    /**
     * Retrieve a sprite from the store
     * 
     * @param ref The reference to the image to use for the sprite
     * @return A sprite instance containing an accelerate image of the request reference
     */
    public Sprite getSprite(String ref) {
        // if we've already got the sprite in the cache
        // then just return the existing version
        if (!sprites.containsKey(ref)) {
            Sprite sprite = factory.getSprite(ref);
            sprites.put(ref, sprite);
        }
        return sprites.get(ref);
    }
}