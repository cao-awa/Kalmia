package com.github.cao.awa.kalmia.message.digest;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializable;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.math.BigInteger;
import java.util.Map;

public class DigestData implements BytesValueConvertable, BytesSerializable<DigestData> {
    private static final Map<Byte, MessageDigger.DigestAlgorithm> idToType = ApricotCollectionFactor.hashMap();
    private static final Map<MessageDigger.DigestAlgorithm, Byte> typeToId = ApricotCollectionFactor.hashMap();

    static {
        idToType.put((byte) 0,
                     MessageDigger.Sha3.SHA_224
        );
        typeToId.put(MessageDigger.Sha3.SHA_224,
                     (byte) 0
        );

        idToType.put((byte) 1,
                     MessageDigger.Sha3.SHA_256
        );
        typeToId.put(MessageDigger.Sha3.SHA_256,
                     (byte) 1
        );

        idToType.put((byte) 2,
                     MessageDigger.Sha3.SHA_512
        );
        typeToId.put(MessageDigger.Sha3.SHA_512,
                     (byte) 2
        );

        idToType.put((byte) 3,
                     MessageDigger.MD5.MD_5
        );
        typeToId.put(MessageDigger.MD5.MD_5,
                     (byte) 3
        );
    }

    private MessageDigger.DigestAlgorithm type;
    private byte[] value;

    public DigestData() {

    }

    public DigestData(MessageDigger.DigestAlgorithm type, byte[] value) {
        this.type = type;
        this.value = value;
    }

    public static DigestData digest(MessageDigger.DigestAlgorithm type, byte[] data) {
        return new DigestData(type,
                              MessageDigger.digestToBytes(data,
                                                          type
                              )
        );
    }

    public MessageDigger.DigestAlgorithm type() {
        return this.type;
    }

    public byte[] bytes() {
        return this.value;
    }

    public String value10() {
        return new BigInteger(this.value).toString(10);
    }

    public static DigestData create(BytesReader reader) {
        return new DigestData().deserialize(reader);
    }

    @Override
    public byte[] serialize() {
        return BytesUtil.concat(new byte[]{typeToId.get(type())},
                                new byte[]{(byte) bytes().length},
                                bytes()
        );
    }

    @Override
    public DigestData deserialize(BytesReader reader) {
        MessageDigger.DigestAlgorithm type = idToType.get(reader.read());

        byte[] value = reader.read(reader.read());

        this.type = type;
        this.value = value;

        return this;
    }
}
