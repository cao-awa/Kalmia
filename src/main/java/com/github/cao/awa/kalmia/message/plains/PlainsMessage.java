package com.github.cao.awa.kalmia.message.plains;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.digest.DigestData;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class PlainsMessage extends Message {
    private long sender;
    private String msg;
    private DigestData digest;

    @Auto
    public PlainsMessage() {

    }

    public PlainsMessage(String msg, long sender, DigestData digest) {
        this.msg = msg;
        this.sender = sender;
        this.digest = digest;
    }

    public String msg() {
        return this.msg;
    }

    public long getSender() {
        return sender;
    }

    public PlainsMessage(String msg, long sender) {
        this.msg = msg;
        this.sender = sender;
        this.digest = new DigestData(MessageDigger.Sha3.SHA_512,
                                     Mathematics.toBytes(MessageDigger.digest(msg,
                                                                              MessageDigger.Sha3.SHA_512
                                                         ),
                                                         16
                                     )
        );
    }

    public static PlainsMessage create(BytesReader reader) {
//        try {
//            return KalmiaEnv.serializeFramework.create(new PlainsMessage(),
//                                                       reader
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
        if (reader.read() == 0) {
            String msg;

            long sender = SkippedBase256.readLong(reader);

            int msgLength = Base256.tagFromBuf(reader.read(2));
            msg = new String(reader.read(msgLength),
                             StandardCharsets.UTF_8
            );

            DigestData digestData = DigestData.create(reader);

            return new PlainsMessage(msg,
                                     sender,
                                     digestData
            );
        } else {
            return null;
        }
    }

    @Override
    public DigestData digest() {
        return this.digest;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(BytesUtil.arrau(0),
                                SkippedBase256.longToBuf(this.sender),
                                Base256.tagToBuf(this.msg.length()),
                                this.msg.getBytes(StandardCharsets.UTF_8),
                                this.digest.serialize()
        );
    }
}
