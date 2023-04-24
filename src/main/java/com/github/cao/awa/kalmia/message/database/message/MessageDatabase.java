package com.github.cao.awa.kalmia.message.database.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.number.encode.ShouldSkipped;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.database.DatabaseProvide;
import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MessageDatabase {
    private final KeyValueDatabase database;

    public MessageDatabase(String path) throws Exception {
        this.database = DatabaseProvide.kv(path);
    }

    public void operation(@ShouldSkipped byte[] sid, BiConsumer<Long, Message> action) {
        seqAll(sid,
               seq -> {
                   action.accept(seq,
                                 get(sid,
                                     SkippedBase256.longToBuf(seq)
                                 )
                   );
               }
        );
    }

    public void operation(@ShouldSkipped byte[] sid, long from, long to, BiConsumer<Long, Message> action) {
        seqAll(sid,
               from,
               to,
               seq -> {
                   action.accept(seq,
                                 get(sid,
                                     SkippedBase256.longToBuf(seq)
                                 )
                   );
               }
        );
    }

    public Message get(@ShouldSkipped byte[] sid, @ShouldSkipped byte[] seq) {
        byte[] msgBytes = this.database.get(key(sid,
                                                seq
        ));
        return Message.create(msgBytes);
    }

    public void delete(@ShouldSkipped byte[] sid, @ShouldSkipped byte[] seq) {
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

    public void seqAll(@ShouldSkipped byte[] sid, Consumer<Long> action) {
        byte[] seqByte = this.database.get(sid);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        count++;

        if (count > 0) {
            for (long seq = 0; ; seq++) {
                if (seq > count) {
                    break;
                }
                action.accept(seq);
            }
        }
    }

    public void seqAll(@ShouldSkipped byte[] sid, long from, long to, Consumer<Long> action) {
        long count = curSeq(sid);

        if (count > 0) {
            long endIndex = Math.min(to,
                                     count
            );
            for (long seq = from; ; seq++) {
                if (seq > endIndex) {
                    break;
                }
                action.accept(seq);
            }
        }
    }

    public long seq(@ShouldSkipped byte[] sid) {
        byte[] seqByte = this.database.get(sid);

        return seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));
    }

    public long curSeq(@ShouldSkipped byte[] sid) {
        byte[] seqByte = this.database.get(sid);

        return seqByte == null ? 0 : SkippedBase256.readLong(new BytesReader(seqByte)) + 1;
    }

    public void deleteAll(@ShouldSkipped byte[] sid) {
        seqAll(sid,
               seq -> {
                   delete(sid,
                          SkippedBase256.longToBuf(seq)
                   );
               }
        );
    }

    public byte[] key(@ShouldSkipped byte[] sid, @ShouldSkipped byte[] seq) {
        return BytesUtil.concat(sid,
                                seq
        );
    }

    public Set<Long> search(@ShouldSkipped byte[] sid, String target) {
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

    public long send(@ShouldSkipped byte[] sid, Message msg) {
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
