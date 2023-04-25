package com.github.cao.awa.kalmia.message.digest;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.convert.ByteArrayConvertable;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.math.BigInteger;
import java.util.Map;

public class MessageDigestData extends BytesValueConvertable implements ByteArrayConvertable {
    private static final Map<Integer, MessageDigger.DigestAlgorithm> idToType = ApricotCollectionFactor.newHashMap();
    private static final Map<MessageDigger.DigestAlgorithm, Integer> typeToId = ApricotCollectionFactor.newHashMap();

    static {
        idToType.put(0,
                     MessageDigger.Sha3.SHA_224
        );
        typeToId.put(MessageDigger.Sha3.SHA_224,
                     0
        );

        idToType.put(1,
                     MessageDigger.Sha3.SHA_256
        );
        typeToId.put(MessageDigger.Sha3.SHA_256,
                     1
        );

        idToType.put(2,
                     MessageDigger.Sha3.SHA_512
        );
        typeToId.put(MessageDigger.Sha3.SHA_512,
                     2
        );

        idToType.put(3,
                     MessageDigger.MD5.MD_5
        );
        typeToId.put(MessageDigger.MD5.MD_5,
                     3
        );
    }

    private final MessageDigger.DigestAlgorithm type;
    private final byte[] value;

    public MessageDigger.DigestAlgorithm type() {
        return this.type;
    }

    public byte[] value() {
        return this.value;
    }

    public String value10() {
        return new BigInteger(this.value).toString(10);
    }

    public MessageDigestData(MessageDigger.DigestAlgorithm type, byte[] value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{typeToId.get(type()).byteValue()},
                                Base256.tagToBuf(value().length),
                                value()
        );
    }

    public static MessageDigestData create(BytesReader reader) {
        MessageDigger.DigestAlgorithm type = idToType.get(reader.read());

        byte[] value = reader.read(Base256.tagFromBuf(reader.read(2)));

        return new MessageDigestData(type,
                                     value
        );
    }
}
