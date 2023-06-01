import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class PaintData {
    private static final Random RANDOM = new Random();
    private static final int WEIGHT = 500;

    public static void main(String[] args) {
        try {
            BufferedImage input = ImageIO.read(new File("www/awa.png"));
            BufferedImage output = new BufferedImage(input.getWidth(),
                                                     input.getHeight(),
                                                     BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D outputGraphic = output.createGraphics();

            byte[] data = new byte[input.getHeight() * input.getWidth() * 4];

            System.out.println(data.length);

            int pos = 0;

            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    Color color = new Color(input.getRGB(x,
                                                         y
                    ),
                                            true
                    );
                    data[pos++] = absReByte(color.getRed());
                    data[pos++] = absReByte(color.getGreen());
                    data[pos++] = absReByte(color.getBlue());
                    data[pos++] = absReByte(color.getAlpha());
                }
            }

            pos = 0;

            byte[] cipher = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6};

//            Viburnum.hCost(data, cipher, 11);

            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    outputGraphic.setColor(new Color(
                            absByte(data[pos++]),
                            absByte(data[pos++]),
                            absByte(data[pos++]),
                            absByte(data[pos++])
                    ));
                    outputGraphic.fillRect(x,
                                           y,
                                           1,
                                           1
                    );
                }
            }

            ImageIO.write(output,
                          "PNG",
                          new FileOutputStream("www/www.png")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int absByte(byte b) {
        if (b < 0) {
            return (b & 0xFF);
        } else {
            return b;
        }
    }

    public static byte absReByte(int b) {
        if (b > 127) {
            return (byte) (b & 0xFF);
        } else {
            return (byte) b;
        }
    }
}
