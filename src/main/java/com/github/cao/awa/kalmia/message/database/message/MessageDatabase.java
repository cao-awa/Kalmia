package com.github.cao.awa.kalmia.message.database.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MessageDatabase {
    private final DB database;

    public MessageDatabase(String path) throws IOException {
        this.database = new Iq80DBFactory().open(new File(path),
                                                 new Options().createIfMissing(true)
                                                              .writeBufferSize(1048560 * 16)
                                                              .compressionType(CompressionType.SNAPPY)
        );
    }

    public void operation(byte[] sid, BiConsumer<Long, Message> action) {
        byte[] seqByte = this.database.get(sid);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq,
                              get(sid,
                                  SkippedBase256.longToBuf(seq)
                              )
                );
            }
        }
    }

    public void operation(byte[] sid, long from, long to, BiConsumer<Long, Message> action) {
        byte[] seqByte = this.database.get(sid);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        count++;

        if (count > 0) {
            long endIndex = Math.min(to,
                                     count
            );
            for (long seq = from; seq < endIndex; seq++) {
                action.accept(seq,
                              get(sid,
                                  SkippedBase256.longToBuf(seq)
                              )
                );
            }
        }
    }

    public Message get(byte[] sid, byte[] seq) {
        byte[] msgBytes = this.database.get(key(sid,
                                                seq
        ));
        return Message.create(msgBytes);
    }

    public void delete(byte[] sid, byte[] seq) {
        Message source = get(sid,
                             seq
        );
        this.database.put(key(sid,
                              seq
                          ),
                          new DeletedMessage(source.getSender(),
                                             source.getDigest()
                          ).toBytes()
        );
    }

    public void seqAll(byte[] sid, Consumer<Long> action) {
        byte[] seqByte = this.database.get(sid);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq);
            }
        }
    }

    public void deleteAll(byte[] sid) {
        seqAll(sid,
               seq -> {
                   delete(sid,
                          SkippedBase256.longToBuf(seq)
                   );
               }
        );
    }

    public byte[] key(byte[] sid, byte[] seq) {
        return BytesUtil.concat(sid,
                                seq
        );
    }

    public Set<Long> search(byte[] sid, String target) {
        Set<Long> result = ApricotCollectionFactor.newHashSet();

        operation(sid,
                  (seq, msg) -> {
//            if (msg.contains(target)) {
//                result.add(seq);
//            }
                  }
        );

        return result;
    }

    public long send(byte[] sid, Message msg) {
        // TODO
        Kalmia.SERVER.sessionManager();

        byte[] seqByte = this.database.get(sid);

        long seq = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        long nextSeq = seq + 1;

        byte[] nextSeqByte = SkippedBase256.longToBuf(nextSeq);

        this.database.put(key(sid,
                              nextSeqByte
                          ),
                          msg.toBytes()
        );

        this.database.put(sid,
                          nextSeqByte
        );

        return nextSeq;
    }
}
