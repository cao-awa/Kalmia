package com.github.cao.awa.kalmia.framework.serialize.json.test;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class TestSss {
    @AutoData
    private boolean testBoolean = true;
    @AutoData
    private byte testByte = 123;
    @AutoData
    private char testChar = 'b';
    @AutoData
    private short testShort = 12345;
    @AutoData
    private int testInt = 114514;
    @AutoData
    private long testLong = 1919810;
    @AutoData
    private double testDouble = 123.5D;
    @AutoData
    private float testFloat = 114.514F;
    @AutoData
    private String testString = "awa";
    @AutoData
    private BigInteger testBigInteger = new BigInteger("1111111111111111111111111111111111111111111111111111111111111111111111111111111");
    @AutoData
    private BigDecimal testBigDecimal = new BigDecimal("11111111111111111111111111111111111111111111111111111111111111111111111111111111111.2222222222333333333333333");
    @AutoData
    private TestSerializable testSerializable = new TestSerializable("awa");
    @AutoData
    private List<Integer> testList = EntrustEnvironment.operation(ApricotCollectionFactor.arrayList(),
                                                                  list -> {
                                                                      list.add(123);
                                                                      list.add(456);
                                                                      list.add(789);
                                                                  }
    );

    public TestSss(JSONObject json) {
        try {
            KalmiaEnv.JSON_SERIALIZE_FRAMEWORK.create(this,
                                                      json
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TestSss() {

    }

    public static void main(String[] args) {
        KalmiaEnv.setupFrameworks();

        TestSss sss = new TestSss();

        try {
            JSONObject serialized = KalmiaEnv.JSON_SERIALIZE_FRAMEWORK.payload(sss);
            System.out.println(serialized.toString(JSONWriter.Feature.PrettyFormat));

            TestSss deserialized = new TestSss(serialized);

            serialized = KalmiaEnv.JSON_SERIALIZE_FRAMEWORK.payload(deserialized);

            System.out.println("----");
            System.out.println(serialized.toString(JSONWriter.Feature.PrettyFormat));
        } catch (Exception e) {

        }
    }
}
