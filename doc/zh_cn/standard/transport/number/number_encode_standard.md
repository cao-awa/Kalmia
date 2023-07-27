# 数字编码
在任何数字传输时，均将其转换为Base256编码

# 字符编码

将一个数字转换为字节数组，在不使用编码跳过时且兼容编码跳过时需要在最前面加 [-1] 表示

## 实现
```java
public static byte[] longToBuf(long l, byte[] buf) {
    buf[0] = (byte) (l >>> 56);
    buf[1] = (byte) (l >>> 48);
    buf[2] = (byte) (l >>> 40);
    buf[3] = (byte) (l >>> 32);
    buf[4] = (byte) (l >>> 24);
    buf[5] = (byte) (l >>> 16);
    buf[6] = (byte) (l >>> 8);
    buf[7] = (byte) (l);
    return buf;
}

public static long longFromBuf(byte[] buf) {
    return ((buf[0] & 0xFFL) << 56) +
           ((buf[1] & 0xFFL) << 48) +
           ((buf[2] & 0xFFL) << 40) +
           ((buf[3] & 0xFFL) << 32) +
           ((buf[4] & 0xFFL) << 24) +
           ((buf[5] & 0xFFL) << 16) +
           ((buf[6] & 0xFFL) << 8) +
           ((buf[7] & 0xFFL));
}
```

在转为字节数组时（longToBuf），将一个数字的指定部分（8bit一组）右移至最高位，强制转换为byte时只会保留那8位

在还原时（longFromBuf），将各个字节转为long类型，然后左移到原先的位置后全部相加，即可得到原数字

## 长度
longToBuf 生成长度为 8 的字节数组

intToBuf 生成长度为 4 的字节数组

tagToBuf 生成长度为 2 的字节数组

# 编码跳过
可以使用SkippedBase256.java创建一个经过压缩的Base256编码数字

\
例如数字3L（long）由Base256.java编码为：[0, 0, 0, 0, 0, 0, 0, 3]

编码跳过会使其变为：[1, 3]

如数字456会由 [0, 0, 0, 0, 0, 0, 1, -56] 压缩至：[2, 1, -56]

\
使用编码跳过压缩的数字必须由SkippedBase256.java解码，不可以再使用Base256.java
