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

    private static int marginLeftRight = 16, marginTopBottom = 14;

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
            Graphics2D g;

            //g.drawImage(image, dst-left-x, dst-top-y, dst-right-x, dst-bottom-y, src-left-x, src-top-y, src-right-x, src-bottom-y, null);

            BufferedImage outLeftRight = new BufferedImage(marginLeftRight * 2, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            g = (Graphics2D) outLeftRight.getGraphics();
            g.drawImage(image, 0, 0, marginLeftRight, outLeftRight.getHeight(), 0, 0, marginLeftRight, image.getHeight(), null); // left side
            g.drawImage(image, marginLeftRight, 0, outLeftRight.getWidth(), outLeftRight.getHeight(), image.getWidth() - marginLeftRight, 0, image.getWidth(), image.getHeight(), null); // right side

            BufferedImage outTopBottom = new BufferedImage(outLeftRight.getWidth(), marginTopBottom * 2, BufferedImage.TYPE_INT_ARGB);
            g = (Graphics2D) outTopBottom.getGraphics();
            g.drawImage(outLeftRight, 0, 0, outTopBottom.getWidth(), marginTopBottom, 0, 0, outLeftRight.getWidth(), marginTopBottom, null); // top side
            g.drawImage(outLeftRight, 0, marginTopBottom, outTopBottom.getWidth(), outTopBottom.getHeight(), 0, outLeftRight.getHeight() - marginTopBottom, outLeftRight.getWidth(), outLeftRight.getHeight(), null); // bottom side

            ImageIO.write(outTopBottom, "png", file);
        }
    }

}
