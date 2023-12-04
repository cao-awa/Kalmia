package com.github.cao.awa.kalmia.message.unknown;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.digest.DigestData;

public class UnknownMessage extends Message {
    @AutoData
    private byte[] msgBytes;
    @AutoData
    private DigestData digest;

    public UnknownMessage() {

    }

    public UnknownMessage(byte[] msgBytes) {
        this.msgBytes = msgBytes;
        this.digest = new DigestData(MessageDigger.Sha3.SHA_512,
                                     Mathematics.toBytes(MessageDigger.digest(msgBytes,
                                                                              MessageDigger.Sha3.SHA_512
                                                         ),
                                                         16
                                     )
        );
    }

    @Override
    public DigestData digest() {
        return this.digest;
    }

    public byte[] details() {
        return this.msgBytes;
    }

    @Override
    public long getSender() {
        return - 1;
    }

    public static UnknownMessage create(BytesReader reader) {
        try {
            return KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.create(new UnknownMessage(),
                                                              reader
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
//        if (reader.read() == 0) {
//            String msg;
//
//            long sender = SkippedBase256.readLong(reader);
//
//            int msgLength = Base256.tagFromBuf(reader.read(2));
//            msg = new String(reader.read(msgLength),
//                             StandardCharsets.UTF_8
//            );
//
//            DigestData digestData = DigestData.create(reader);
//
//            return new PlainsMessage(msg,
//                                     sender,
//                                     digestData
//            );
//        } else {
//            return null;
//        }
    }
}
