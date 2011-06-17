package thyscom.spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
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

    /**
     * Get the single instance of this class 
     * 
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
            addNewSprite(ref);
        }
        return sprites.get(ref);
    }

    /**
     * Add a new sprite to the store.
     * @param ref
     * @throws HeadlessException 
     */
    private void addNewSprite(String ref) {
        Image image = null;
        try {
            // The ClassLoader.getResource() ensures we get the sprite
            // from the appropriate place, this helps with deploying the game
            // with things like webstart. You could equally do a file look
            // up here.
            URL url = this.getClass().getClassLoader().getResource(ref);

            // if the source file is available, use it. Else create a figting dot 
            // on the spot.
            if (url != null) {
                image = loadImageFromDisk(url);
            } else {
                image = makeSubstituteImage();
            }
        } catch (IOException e) {
            logger.error(e);
            image = makeSubstituteImage();
        }

        // create a sprite, add it the cache then return it
        Sprite sprite = new Sprite(image);
        sprites.put(ref, sprite);
    }

    private Image loadImageFromDisk(URL url) throws IOException {
        BufferedImage sourceImage = sourceImage = ImageIO.read(url);
        // create an accelerated image of the right size to store our sprite in
        Image image = getGraphicsConfiguration().createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
        // draw our source image into the accelerated image
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        return image;
    }

    private Image makeSubstituteImage() {
        int WIDTH = 20;
        int HEIGHT = 20;
        GraphicsConfiguration gc = getGraphicsConfiguration();
        BufferedImage image = gc.createCompatibleImage(WIDTH, HEIGHT, Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.fill(new Ellipse2D.Double(0, 0, WIDTH, WIDTH));
        g2d.dispose();
        return image;
    }

    private GraphicsConfiguration getGraphicsConfiguration() throws HeadlessException {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
}