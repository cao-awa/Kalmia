package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function;

import com.github.cao.awa.apricot.anntation.Stable;

import java.io.Serializable;

@Stable
public interface ExceptingRunnable extends Serializable {
    void apply() throws Exception;
}
