package com.github.cao.awa.kalmia.d.visual;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import org.jetbrains.annotations.Range;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class DataImageConvert {
    private static final Random RANDOM = new Random();
    private static final int WIDTH = 500;
    private final byte[] data;

    public DataImageConvert(byte[] data) {
        this.data = data;
    }

    public BufferedImage toImage() {
        int height = Math.max(this.data.length / WIDTH,
                              2
        );

        BufferedImage image = new BufferedImage(WIDTH,
                                                height,
                                                BufferedImage.TYPE_4BYTE_ABGR
        );

        Graphics2D graphics = image.createGraphics();

        makeHead(graphics,
                 this.data.length
        );

        int streaming = 0;
        int xReset = 0;
        int x = 0;
        int y = 1;
        while (true) {
            if (WIDTH > x) {
                if (this.data.length > streaming) {
                    paint(graphics,
                          x,
                          y,
                          this.data,
                          streaming
                    );
                    streaming += 3;
                    x += 1;
                } else {
                    break;
                }
            } else {
                x = xReset;
                y++;
            }
        }

        return image;
    }

    public static void makeHead(Graphics2D graphics, long head) {
        byte[] headData = Base256.longToBuf(head);
        int streaming = 0;
        for (int x = 0; x < WIDTH; x++) {
            if (3 < x) {
                paint(graphics,
                      x,
                      0,
                      255,
                      255,
                      random(),
                      255
                );
            } else {
                paint(graphics,
                      x,
                      0,
                      readOrRandom(headData,
                                   streaming++
                      ),
                      readOrRandom(headData,
                                   streaming++
                      ),
                      readOrRandom(headData,
                                   streaming++
                      ),
                      255
                );
            }
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
            return (byte) (b);
        } else {
            return (byte) b;
        }
    }

    public static void paint(Graphics2D graphics, int x, int y, int d1, int d2, int d3, int d4) {
        graphics.setPaint(new Color(d1,
                                    d2,
                                    d3,
                                    d4
        ));
        graphics.fillRect(x,
                          y,
                          1,
                          1
        );
    }

    public static void paint(Graphics2D graphics, int x, int y, byte[] data, int dst) {
        paint(graphics,
              x,
              y,
              readOrRandom(data,
                           dst
              ),
              readOrRandom(data,
                           dst + 1
              ),
              readOrRandom(data,
                           dst + 2
              ),
//              readOrRandom(data,
//                           dst + 3
//              )
              random()
        );
    }

    public static int readOrRandom(byte[] data, int pos) {
        return data.length > pos ? absByte(data[pos]) : random();
    }

    public static int random() {
        return RANDOM.nextInt(0,
                              255
        );
    }

    public static void read(BufferedImage image, int x, int y, byte[] data, int dst, @Range(from = 0, to = 4) int limit) {
        Color rgb = new Color(image.getRGB(x,
                                           y
        ),
                              true
        );

//        if (limit > 3) {
//            data[dst + 3] = absReByte(rgb.getAlpha());
//        }
        if (limit > 2) {
            data[dst + 2] = absReByte(rgb.getBlue());
        }
        if (limit > 1) {
            data[dst + 1] = absReByte(rgb.getGreen());
        }
        if (limit > 0) {
            data[dst] = absReByte(rgb.getRed());
        }
    }

    public static byte[] read(BufferedImage image) {
        byte[] lengthData = new byte[8];

        read(image,
             0,
             0,
             lengthData,
             0,
             3
        );
        read(image,
             1,
             0,
             lengthData,
             3,
             3
        );
        read(image,
             2,
             0,
             lengthData,
             6,
             2
        );

        long length = Base256.longFromBuf(lengthData);

        int streaming = 0;
        byte[] result = new byte[(int) length];
        int x = 0;
        int y = 1;
        while (true) {
            if (x < WIDTH) {
                if (streaming >= length) {
                    break;
                } else {
                    int limit = (int) (length - streaming);
                    limit = limit > 2 ? 3 : limit;
                    read(image,
                         x,
                         y,
                         result,
                         streaming,
                         limit
                    );
                    streaming += 3;
                }
                x++;
            } else {
                x = 0;
                y++;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            File file = new File("pfd/test.png");

            byte[] data = BytesRandomIdentifier.create((500 * 499) * 2);

            BufferedImage image = new DataImageConvert(data).toImage();

            ImageIO.write(image,
                          "PNG",
                          new FileOutputStream(file)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
