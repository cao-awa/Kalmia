package com.github.cao.awa.kalmia.message.unknown;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.digest.DigestData;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class UnknownMessage extends Message {
    private static final byte[] HEADER = new byte[]{0};
    @AutoData
    private byte[] msgBytes;
    @AutoData
    private DigestData digestData;

    public UnknownMessage() {

    }

    public UnknownMessage(byte[] msgBytes) {
        this.msgBytes = msgBytes;
        this.digestData = new DigestData(MessageDigger.Sha3.SHA_512,
                                         Mathematics.toBytes(MessageDigger.digest(msgBytes,
                                                                                  MessageDigger.Sha3.SHA_512
                                                             ),
                                                             16
                                         )
        );
    }

    public UnknownMessage(byte[] gid, byte[] msgBytes) {
        super(gid);
        this.msgBytes = msgBytes;
        this.digestData = new DigestData(MessageDigger.Sha3.SHA_512,
                                         Mathematics.toBytes(MessageDigger.digest(msgBytes,
                                                                                  MessageDigger.Sha3.SHA_512
                                                             ),
                                                             16
                                         )
        );
    }

    @Override
    public DigestData digest() {
        return this.digestData;
    }

    public byte[] details() {
        return this.msgBytes;
    }

    @Override
    public long sender() {
        return - 1;
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    public static UnknownMessage create(BytesReader reader) {
        if (reader.read() == 0) {
            byte[] gid = reader.read(24);

            byte[] data = reader.all();

            return new UnknownMessage(gid,
                                      data
            );
        } else {
            return null;
        }
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(header(),
                                globalId(),
                                this.msgBytes
        );
    }
}
