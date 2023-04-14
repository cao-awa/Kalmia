package com.github.cao.awa.apricot.io.bytes.reader;

import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class BytesReader {
    private final byte[] bytes;
    private int cursor = 0;

    public BytesReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getCursor() {
        return this.cursor;
    }

    public byte[] reverseRound(int round, int length) {
        byte[] bytes = read(length);
        byte[] result = new byte[round];

        if (length >= 0) {
            System.arraycopy(
                    bytes,
                    0,
                    result,
                    round - length,
                    length
            );
        }
        return result;
    }

    public byte[] read(int length) {
        if (length == 0) {
            return BytesUtil.EMPTY;
        }
        if (length + this.cursor > this.bytes.length) {
            return new byte[length];
        } else {
            byte[] result = new byte[length];
            System.arraycopy(
                    this.bytes,
                    this.cursor,
                    result,
                    0,
                    result.length
            );
            this.cursor += length;
            return result;
        }
    }

    public BytesReader next(byte target) {
        while (read() == target) {
        }
        this.cursor--;
        return this;
    }

    public BytesReader skip(int length) {
        this.cursor += length;
        return this;
    }

    public boolean readable(int length) {
        return this.bytes.length >= this.cursor + length;
    }

    public int read() {
        return this.bytes.length > this.cursor ? this.bytes[this.cursor++] : - 1;
    }

    public BytesReader reset() {
        this.cursor = 0;
        return this;
    }

    public void back(int range) {
        this.cursor -= range;
    }

    public byte[] all() {
        return read(this.bytes.length - this.cursor);
    }
}
