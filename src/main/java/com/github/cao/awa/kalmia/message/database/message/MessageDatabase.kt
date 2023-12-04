package com.github.cao.awa.kalmia.message.database.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped;
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase;
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MessageDatabase {
    private final KeyValueBytesDatabase database;

    public MessageDatabase(String path) throws Exception {
        this.database = DatabaseProviders.bytes(path);
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
        if (msgBytes == null) {
            return null;
        }
        return EntrustEnvironment.trys(() -> Message.create(msgBytes),
                                       () -> new UnknownMessage(msgBytes)
        );
    }

    public void delete(@ShouldSkipped byte[] sid, @ShouldSkipped byte[] seq) {
        Message source = get(sid,
                             seq
        );
        this.database.put(key(sid,
                              seq
                          ),
                          new DeletedMessage(source.getSender(),
                                             source.digest()
                          ).toBytes()
        );
    }

    public void seqAll(@ShouldSkipped byte[] sid, Consumer<Long> action) {
        byte[] seqByte = this.database.get(sid);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));

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

    public boolean present(byte[] key) {
        return this.database.get(key) != null;
    }

    public long seq(@ShouldSkipped byte[] sid) {
        byte[] seqByte = this.database.get(sid);

        return seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));
    }

    public long curSeq(@ShouldSkipped byte[] sid) {
        byte[] seqByte = this.database.get(sid);

        return seqByte == null ? 0 : SkippedBase256.readLong(BytesReader.of(seqByte)) + 1;
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
        Set<Long> result = ApricotCollectionFactor.hashSet();

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
        byte[] seqByte = this.database.get(sid);

        long seq = seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));

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
