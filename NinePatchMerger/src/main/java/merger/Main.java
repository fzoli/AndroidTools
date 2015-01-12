package merger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern OPT_PATTERN = Pattern.compile("-(.*)=(.*)");

    private static File OUT_DIR = new File(".", "out");

    private static String getExtension(String filename) {
        if (filename == null) return null;
        final String afterLastSlash = filename.substring(filename.lastIndexOf('/') + 1);
        final int afterLastBackslash = afterLastSlash.lastIndexOf('\\') + 1;
        final int dotIndex = afterLastSlash.indexOf('.', afterLastBackslash);
        return (dotIndex == -1) ? "" : afterLastSlash.substring(dotIndex + 1);
    }

    private static String genPatchName(String imagePath) {
        String ext = getExtension(imagePath);
        if (ext.isEmpty()) {
            throw new RuntimeException("No extension: " + imagePath);
        }
        String fileEnd = "." + ext;
        String fileStart = imagePath.substring(0, imagePath.length() - fileEnd.length());
        return fileStart + ".9" + fileEnd;
    }

    private static File genPatchPath(String imagePath) {
        String orig = genPatchName(imagePath);
        return new File(OUT_DIR, new File(orig).getName());
    }

    private static void createImage(BufferedImage patch, String imagePath, File out) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        if (patch.getWidth() != image.getWidth() + 2 || patch.getHeight() != image.getHeight() + 2) {
            System.err.println("Wrong patch size (" + imagePath + ")");
            System.exit(1);
        }
        BufferedImage outImage = new BufferedImage(patch.getWidth(), patch.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) outImage.getGraphics();
        g.drawImage(image, 1 , 1, null);
        g.drawImage(patch, 0, 0, null);
        ImageIO.write(outImage, "png", out);
    }

    public static void createImage(BufferedImage patch, String imagePath) throws IOException {
        createImage(patch, imagePath, genPatchPath(imagePath));
    }

    public static void main(String[] args) throws IOException {

        Map<String, String> options = new HashMap<String, String>();
        List<String> files = new ArrayList<String>();

        for (String arg : args) {
            Matcher m = OPT_PATTERN.matcher(arg);
            if (m.find()) {
                options.put(m.group(1), m.group(2));
            }
            else {
                File f = new File(arg);
                if (!f.isFile()) {
                    System.err.println("File not found: " + f);
                    System.exit(1);
                }
                files.add(arg);
            }
        }

        if (files.size() < 2) {
            System.err.println("Usage: [-out=directory_path] [patch file] [image files ...]");
            System.exit(1);
        }

        String outOpt = options.get("out");
        if (outOpt != null && !outOpt.isEmpty()) {
            File f = new File(outOpt);
            if (!f.isDirectory()) {
                System.err.println("Output directory does not exist: " + f);
                System.exit(1);
            }
            else {
                OUT_DIR = f;
            }
        }

        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }

        String patchFile = files.get(0);
        BufferedImage patch = ImageIO.read(new File(patchFile));
        for (int i = 1; i < files.size(); i++) {
            String imageFile = files.get(i);
            createImage(patch, imageFile);
        }

    }

}
