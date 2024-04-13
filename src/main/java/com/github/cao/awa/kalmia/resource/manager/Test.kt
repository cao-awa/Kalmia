package com.github.cao.awa.kalmia.resource.manager;

import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.io.IOUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) {
        ResourcesManager manager = new ResourcesManager("res");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            FileOutputStream fileOutput = new FileOutputStream("res/b.txt");

            manager.getShardedResource("A.txt",
                                       16384,
                                       (startPos, bytes, isFinal) -> {
                                           try {
                                               output.write(bytes);
                                               fileOutput.write(bytes);
                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }
                                       }
            );

            fileOutput.close();

            System.out.println(MessageDigger.digest(output.toByteArray(),
                                                    MessageDigger.Sha3.SHA_512
            ));

            byte[] read = IOUtil.readBytes(new BufferedInputStream(new FileInputStream("res/A.txt")));
            System.out.println(MessageDigger.digest(read,
                                                    MessageDigger.Sha3.SHA_512
            ));

            read = IOUtil.readBytes(new FileInputStream("res/b.txt"));
            System.out.println(MessageDigger.digest(read,
                                                    MessageDigger.Sha3.SHA_512
            ));
        } catch (Exception e) {

        }
    }
}
