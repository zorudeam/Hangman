package utilities;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * The {@code ImageUtilities} class contains methods relating to images that are
 * small, efficient, and commonly used.
 *
 * @author Oliver Abdulrahim
 */
public final class ImageUtilities {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private ImageUtilities() { 
    
    }
    
    /**
     * Affine transformation that allows for horizontally-oriented operation on 
     * images.
     *  
     * <p> This object is treated as if it were immutable within this class,
     * or in other words, transform operations are never performed on it. Rather
     * than performing such operations on this object, a new 
     * {@code AffineTransform} object is created using the 
     * {@link AffineTransform#AffineTransform(java.awt.geom.AffineTransform)}
     * constructor.</p>
     * 
     * @see AffineTransform#AffineTransform(java.awt.geom.AffineTransform)
     */
    private static final AffineTransform HORIZONTAL = 
            AffineTransform.getScaleInstance(-1, 1);
    
    /**
     * Affine transformation that allows for vertically-oriented operation on 
     * images.
     * 
     * <p> This object is treated as if it were immutable within this class,
     * or in other words, transform operations are never performed on it. Rather
     * than performing such operations on this object, a new 
     * {@code AffineTransform} object is created using the 
     * {@link AffineTransform#AffineTransform(java.awt.geom.AffineTransform)}
     * constructor.</p>
     * 
     * @see AffineTransform#AffineTransform(java.awt.geom.AffineTransform)
     */
    private static final AffineTransform VERTICAL = 
            AffineTransform.getScaleInstance(1, -1);
    
    /**
     * Tests if the given {@code String} path is a valid path that refers to an
     * image (case insensitive), accepting images of type {@code png}, 
     * {@code bmp}, {@code jpg}, {@code jpeg}, {@code gif}, or {@code wbmp}.
     * 
     * <p> This method uses the following regular expression:
     * 
     * <blockquote><pre>
     * ^(?:[\w]:|\\)(\\[a-z_\-\s0-9\.]+)+\.(png|bmp|jpg|jpeg|gif|wbmp)$</pre>
     * </blockquote>
     * 
     * The initial portion of the expression, {@code ^(?:[\w]\:|\\)}, 
     * ensures that the given path begins with either {@code "\\"}, 
     * {@code "C:\}, or some other drive variant <em>N</em> such that the path 
     * includes {@code "N:\"}.
     * 
     * <p> The following portion of the expression, 
     * {@code (\\[a-z_\-\s0-9\.]+)}, ensures that the given path contains valid 
     * characters, including any number of characters that are any type of 
     * space, {@code [-]}, {@code [_]}, {@code [.]}, or within the range 
     * {@code [0-9]} or {@code [a-z]}. (The compiled pattern is <em>case 
     * insensitive</em>.)
     * 
     * <p> The final portion of the expression, 
     * {@code \.(png|bmp|jpg|jpeg|gif|wbmp)$}, ensures that the given path ends
     * with a valid image file type.
     * 
     * <p> The statement
     * 
     * <blockquote><pre>
     * boolean b = isValidImagePath("C:\\Users\\Duke\\Pictures\\Cat.png");</pre>
     * </blockquote>
     * 
     * is an example of an invocation of this method that would return 
     * {@code true}.
     * 
     * @param path The path to test for validity.
     * @return {@code true} if and only if the given {@code String} is a valid
     *         path, {@code false} otherwise.
     */
    public static boolean isValidImagePath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        /* 
         * For case insensitivity, it is required to construct a new Pattern 
         * with the CASE_INSENSITIVE flag instead of using 
         * Pattern.matches(String, CharSequence) 
         */
        Pattern fileRegex = Pattern.compile(
                "^(?:[\\w]:|\\\\)"
              + "(\\\\[a-z_\\-\\s0-9\\.]+)"
              + "+\\.(png|bmp|jpg|jpeg|gif|wbmp)$", Pattern.CASE_INSENSITIVE);
        Matcher m = fileRegex.matcher(path);
        return m.matches();
    }
    
    /**
     * Tests if a given path refers to a valid file, ensuring that the path is
     * readable and refers to an existing file. If this method returns 
     * {@code true}, then at the time of execution, the file at the given path
     * is readable and technically exists; however, this is not an absolute 
     * requirement due any changes from the to time of check to the time of use 
     * (TOCTTOU). For purposes of this implementation, security is not an issue 
     * and images are not expected to change paths.
     * 
     * @param path The path to test for validity.
     * @return {@code true} if and only if the given {@code String} is a valid
     *         path, {@code false} otherwise.
     * @see #isValidImagePath(java.lang.String) The main usage of this method.
     */
    public static boolean isReadableExistingPath(String path) {
        Path p = Paths.get(path);
        return Files.isReadable(p) & Files.exists(p);
    }
    
    /**
     * Tests if a given image format refers to {@code png}, {@code bmp}, 
     * {@code jpg}, {@code jpeg}, {@code gif}, or {@code wbmp}.
     * 
     * @param format The image format type to test for validity.
     * @return {@code true} if and only if the given {@code String} is of valid
     *         image format, {@code false} otherwise.
     */
    public static boolean isValidImageFormat(String format) {
        if (format == null || format.isEmpty()) {
            return false;
        }
        String[] formatNames = ImageIO.getWriterFormatNames();
        for (String s : formatNames) {
            if (s.equals(format)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Attempts to read an image from a given file path.
     * 
     * @param path Refers to the path of the image to attempt to read.
     * @return The read image, if the operation was successful. If the operation
     *         was not successful, returns {@code null}.
     * @throws IllegalArgumentException if the given file path is mangled or 
     *         refers to a file that is not of type {@code png}, {@code bmp}, 
     *         {@code jpg}, {@code jpeg}, {@code gif}, or {@code wbmp}.
    */
    public static BufferedImage readImage(String path) {
        if (!isValidImagePath(path) || !isReadableExistingPath(path)) {
            throw new IllegalArgumentException("Invalid image path : " + path);
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        }
        catch (IOException ex) {
            Logger.getLogger(ImageUtilities.class.getName())
                    .log(Level.SEVERE, "IOException attempting to read image "
                            + "with file path : " + path, ex);
        }
        return image;
    }
    
    /**
     * Attempts to write the given image to the specified {@code File} output
     * as the given format.
     * 
     * <p> As an example, the following call to this method
     * 
     * <blockquote><pre>
     * writeImage(image, new File(C:\\Pictures\\Cat.png", "png");</pre>
     * </blockquote>
     * 
     * would result in an image with name {@code "Cat"} of type {@code png} 
     * being written to the given path. 
     * 
     * @param image The image to write to the specified file as the given 
     *              format.
     * @param output Refers to the {@code File} of the image to write.
     * @param format The format to write the given image as.
     * @return {@code true} if the write operation was successful, {@code false}
     *         otherwise.
     * @throws IllegalArgumentException if the given file path is mangled or 
     *         refers to a file that is not of type {@code png}, {@code bmp}, 
     *         {@code jpg}, {@code jpeg}, {@code gif}, or {@code wbmp}.
     * @throws NullPointerException if the given image {@code image} is 
     *         {@code null}.
    */
    public static boolean writeImage(RenderedImage image, File output, 
            String format) 
    {
        Objects.requireNonNull(image, "Invalid null image!");
        if (!isValidImagePath(output.getAbsolutePath())) {
            throw new IllegalArgumentException("Invalid image path : " 
                    + output.getAbsolutePath());
        }
        if (!isValidImageFormat(format)) {
            throw new IllegalArgumentException("Invalid image format : " 
                    + format);
        }
        boolean result = false;
        try {
            result = ImageIO.write(image, format, output);
        }
        catch (IOException ex) {
            Logger.getLogger(ImageUtilities.class.getName())
                    .log(Level.SEVERE, "IOException attempting to write image "
                            + "with file path : " + output.getAbsolutePath(), 
                            ex);
        }
        return result;
    }
    
    /**
     * Flips a given {@code BufferedImage} argument horizontally across its 
     * vertical center.
     * 
     * @param src The image to flip horizontally.
     * @return The flipped version of the given image.
     * @throws NullPointerException if the given image {@code src} is 
     *         {@code null}.
     */
    public static BufferedImage flipHorizontal(BufferedImage src) {
        Objects.requireNonNull(src, "Invalid null image!");
        AffineTransform at = new AffineTransform(HORIZONTAL);
        at.translate(-src.getWidth(), 0);
        AffineTransformOp operation = new AffineTransformOp(at, 
                AffineTransformOp.TYPE_BICUBIC);
        return operation.filter(src, null);
    }
    
    /**
     * Flips a given {@code BufferedImage} argument horizontally across the 
     * given axis argument.
     * 
     * @param src The image to flip horizontally.
     * @param axis The axis to flip the given image across.
     * @return The flipped version of the given image.
     * @throws NullPointerException if the given image {@code src} is 
     *         {@code null}.
     */
    public static BufferedImage flipHorizontal(BufferedImage src, int axis) {
        Objects.requireNonNull(src, "Invalid null image!");
        AffineTransform at = new AffineTransform(HORIZONTAL);
        at.translate(-axis, 0);
        AffineTransformOp operation = new AffineTransformOp(at, 
                AffineTransformOp.TYPE_BICUBIC);
        return operation.filter(src, null);
    }
    
    /**
     * Flips a given {@code BufferedImage} argument vertically across its 
     * horizontal center.
     * 
     * @param src The image to flip vertically.
     * @return The flipped version of the given image.
     * @throws NullPointerException if the given image {@code src} is 
     *         {@code null}.
     */
    public static BufferedImage flipVertical(BufferedImage src) {
        Objects.requireNonNull(src, "Invalid null image!");
        AffineTransform at = new AffineTransform(VERTICAL);
        at.translate(0, -src.getHeight());
        AffineTransformOp operation = new AffineTransformOp(at, 
                AffineTransformOp.TYPE_BICUBIC);
        return operation.filter(src, null);
    }
    
    /**
     * Flips a given {@code BufferedImage} argument vertically across the 
     * given axis argument. 
     * 
     * @param src The image to flip vertically.
     * @param axis The axis to flip the given image across.
     * @return The flipped version of the given image.
     * @throws NullPointerException if the given image {@code src} is 
     *         {@code null}.
     */
    public static BufferedImage flipVertical(BufferedImage src, int axis) {
        Objects.requireNonNull(src, "Invalid null image!");
        AffineTransform at = new AffineTransform(VERTICAL);
        at.translate(0, -axis);
        AffineTransformOp operation = new AffineTransformOp(at, 
                AffineTransformOp.TYPE_BICUBIC);
        return operation.filter(src, null);
    }
    
    /**
     * Scales and returns a given {@code BufferedImage} to the specified width
     * and height arguments. This implementation favors quality over speed.
     * 
     * @param src The image to scale.
     * @param width The desired width of the new image.
     * @param height The desired height of the new image.
     * @return The scaled version of the given image with respect to the desired
     *         width and height arguments.
     * @throws NullPointerException if the given image {@code src} is 
     *         {@code null}.
     */
    public static BufferedImage scale(BufferedImage src, int width, 
            int height)
    {
        Objects.requireNonNull(src, "Invalid null image!");
        int w = src.getWidth();
        int h = src.getHeight();
        double scaleX = (double) width / w;
        double scaleY = (double) height / h;
        
        AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp operation = new AffineTransformOp(at, 
                AffineTransformOp.TYPE_BICUBIC);
        return operation.filter(src, 
                new BufferedImage(width, height,src.getType()));
    }
    
    /**
     * Detects any edges within a given image with respect to the given 
     * tolerance using an {@link ConvolveOp} implementation.
     * 
     * @param src The image to perform the edge detect operation on.
     * @param tolerance The tolerance to use within the operation (usually 
     *                  around {@code 8.0f} or above).
     * @return The edge-detected version of the given image.
     * @throws NullPointerException if the given image {@code src} is 
     *         {@code null}.
     * @see ConvolveOp For implementation details.
     */
    public static BufferedImage edgeDetect(BufferedImage src, float tolerance) {
        Objects.requireNonNull(src, "Invalid null image!");
        BufferedImage edgeCopy = process(src, edgeDetectOperator(tolerance));
        return edgeCopy;
    }
    
    private static BufferedImage process(BufferedImage src,
            BufferedImageOp operation) {
        BufferedImage dest = operation.createCompatibleDestImage(src,
                src.getColorModel());
        dest = operation.filter(src, dest);
        return dest;
    }

    private static ConvolveOp edgeDetectOperator(float tolerence) {
        float matrix[] = new float[9];
        Arrays.fill(matrix, -1.0f);
        matrix[4] = tolerence; // Middle value of matrix
        Kernel k = new Kernel(3, 3, matrix);
        
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        ConvolveOp op = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, hints);
        return op;
    }

}
