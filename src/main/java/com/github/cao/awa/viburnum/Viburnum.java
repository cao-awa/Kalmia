package com.github.cao.awa.viburnum;

import com.github.cao.awa.apricot.util.io.IOUtil;

import java.io.*;

/**
 * A high-cost encryption algorithm.
 * <p>
 * This encryption can decrypt using exactly the same high-cost process.
 * <p>
 * Experimental algorithm, do not heavily uses.
 * <p>
 * <p>
 * No quantum computer brute force decrypt resist !
 * <p>
 *
 * @author cao_awa
 * @since 1.0.0
 */
public class Viburnum {
    private static final int RELY_BLOCK = 16 - 1;

    public static void hCost(File file, File toFile, byte[] cipher, int roundCount, int blockSize) throws IOException {
        hCost(file,
              toFile,
              cipher,
              roundCount,
              blockSize,
              RELY_BLOCK
        );
    }

    public static void hCost(File file, File toFile, byte[] cipher, int roundCount, int blockSize, int relyBlock) throws IOException {
        partFile(file,
                 toFile,
                 cipher,
                 roundCount,
                 blockSize,
                 relyBlock,
                 true
        );
    }

    public static void basic(File file, File toFile, byte[] cipher, int roundCount, int blockSize) throws IOException {
        partFile(file,
                 toFile,
                 cipher,
                 roundCount,
                 blockSize,
                 - 1,
                 false
        );
    }

    public static void partFile(File file, File toFile, byte[] cipher, int roundCount, int blockSize, int relyBlock, boolean isHCost) throws IOException {
        if (! file.isFile()) {
            return;
        }
        if (! toFile.isFile()) {
            toFile.createNewFile();
        }
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(toFile));
        int length;
        byte[] block = new byte[blockSize];
        while ((length = input.read(block)) != - 1) {
            if (length != blockSize) {
                byte[] newBlock = new byte[length];
                System.arraycopy(block,
                                 0,
                                 newBlock,
                                 0,
                                 length
                );
                block = newBlock;
            }

            if (isHCost) {
                hCost(block,
                      cipher,
                      roundCount,
                      relyBlock
                );
            } else {
                basic(block,
                      cipher,
                      roundCount
                );
            }

//            System.out.println(Arrays.toString(block));

            IOUtil.write0(output,
                          block
            );
        }

        output.close();
        input.close();
    }

    public static void hCost(byte[] bytes, byte[] cipher, int rounds) {
        hCost(bytes,
              cipher,
              rounds,
              RELY_BLOCK
        );
    }

    public static void hCost(byte[] bytes, byte[] cipher, int rounds, int relyBlock) {
        hmac(cipher,
             0
        );

        // The round must be odd number.
        if ((rounds & 1) == 0) {
            rounds++;
        }
        int cipherIndex = 0;
        long stream = 0;
        int flipper = ~ rounds;
        // Round encrypt and decrypt, the stream feature replace need a few rounds.
        for (int r = 0; r < rounds; r++) {
            // Swaps the bytes.
            // Here is making more cost to encrypt and decrypt.
            rev(bytes);

            // Every byte rely on "xor" to value at previous byte for self.
            // One byte be wrongly will destroy a whole data block.
            rely(bytes,
                 relyBlock
            );

            // Chaos step.
            for (int i = 0; i < bytes.length; i++) {
                // Up the stream, for next feature replaces.
                stream++;
                // Use new one key when current key is end.
                if (cipherIndex == cipher.length) {
                    hmac(cipher,
                         stream
                    );

                    cipherIndex = 0;
                }
                // Using stream feature replaced to prevent the feature extraction.
                // The 'offs' is stream primary.
                bytes[i] ^= stream;
                bytes[i] ^= cipher[cipherIndex] ^ stream;
                stream += cipher[cipherIndex];
                cipherIndex++;
            }

            // The flipper is sub stream.
            // Here is making more cost to encrypt and decrypt.
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) ~ (~ bytes[i] ^ (flipper = ~ flipper));
            }

            // Chaos step.
            for (int i = bytes.length - 1; i > 0; i--) {
                // Up the stream, for next feature replaces.
                stream++;
                // Use new one key when current key is end.
                if (cipherIndex == cipher.length) {
                    hmac(cipher,
                         stream
                    );

                    cipherIndex = 0;
                }
                // Make  more  chaos, further hinder the  feature extraction.
                bytes[i] ^= r;
                bytes[i] ^= (~ stream) ^ i;
                bytes[i] ^= cipher[cipherIndex] ^ (stream += cipher[cipherIndex]);
                bytes[i] = (byte) ((~ bytes[i]) ^ stream);
                cipherIndex++;
            }

            // Revert the bytes.
            // Every byte rely on "xor" to value at previous byte for self.
            // One byte be wrongly will destroy a whole data block.
            rely(bytes,
                 relyBlock
            );

            // Reverted 'rev' call on encrypt round start.
            rev(bytes);

            rely(cipher,
                 cipher.length
            );
        }
    }

    public static void basic(byte[] bytes, byte[] cipher, int rounds) {
        hmac(cipher,
             0
        );

        // The round must be odd number.
        if ((rounds & 1) == 0) {
            rounds++;
        }
        int cipherIndex = 0;
        long stream = 0;
        // Round encrypt and decrypt, the stream feature replace need a few rounds.
        for (int r = 0; r < rounds; r++) {
            // Chaos step.
            for (int i = 0; i < bytes.length; i++) {
                // Up the stream, for next feature replaces.
                stream++;
                // Use new one key when current key is end.
                if (cipherIndex == cipher.length) {
                    hmac(cipher,
                         // Using stream feature replaced to prevent the feature extraction
                         stream
                    );

                    cipherIndex = 0;
                }
                // Using stream feature replaced to prevent the feature extraction.
                bytes[i] ^= stream;
                bytes[i] ^= cipher[cipherIndex] ^ stream;
                stream += cipher[cipherIndex];
                cipherIndex++;
            }
        }
    }

    public static void hmac(byte[] cipher, long offset) {
        for (int i = 0; i < cipher.length; i++) {
            cipher[i] ^=
                    (byte) offset++ >>> (offset % 4) *
                            Math.max(1,
                                     i
                            );
        }
    }

    public static void rely(byte[] bytes, int block) {
        byte last = bytes[0];
        int cur = 0;
        for (int i = 1; i < bytes.length; i++) {
            if (cur++ == block) {
                cur = 0;
                last = bytes[i];
                continue;
            }
            bytes[i] ^= last;
        }
    }

    public static void rev(byte[] bytes) {
        for (int start = 0, end = bytes.length - 1; start < end; start++, end--) {
            byte temp = bytes[end];
            bytes[end] = bytes[start];
            bytes[start] = temp;
        }
    }
}