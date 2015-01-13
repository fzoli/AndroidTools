package cropper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zoli on 2015.01.13..
 */
public class Main {

    private static String[] FILES = {
        "/home/zoli/workspace/paystation/src/main/res/drawable-xxhdpi/btn_*.png"
    };

    private static int marginLeftRight = 16;

    private static void putFile(List<File> ls, File f) {
        if (f.isFile()) {
            ls.add(f);
        }
    }

    private static List<File> getFiles() {
        List<File> files = new ArrayList<File>();
        for (String s : FILES) {
            File f = new File(s);
            if (s.contains("*")) {
                String pattern = f.getName();
                pattern = pattern.replaceAll("\\.", "\\\\.");
                pattern = pattern.replaceAll("\\*", ".*");
                Pattern p = Pattern.compile(pattern);
                File dir = f.getParentFile();
                for (File cnt : dir.listFiles()) {
                    Matcher m = p.matcher(cnt.getName());
                    if (m.matches()) {
                        putFile(files, cnt);
                    }
                }
            }
            else {
                putFile(files, f);
            }
        }
        return files;
    }

    public static void main(String[] args) throws IOException {
        List<File> files = getFiles();
        for (File file : files) {
            BufferedImage image = ImageIO.read(file);
            BufferedImage out = new BufferedImage(marginLeftRight * 2, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) out.getGraphics();
            g.drawImage(image, 0, 0, marginLeftRight, out.getHeight(), 0, 0, marginLeftRight, image.getHeight(), null); // left side
            g.drawImage(image, marginLeftRight, 0, out.getWidth(), out.getHeight(), image.getWidth() - marginLeftRight, 0, image.getWidth(), image.getHeight(), null); // right side
            ImageIO.write(out, "png", file);
        }
    }

}
