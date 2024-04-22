package com.github.cao.awa.kalmia.annotations.config;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.apricot.annotations.auto.Auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Auto
@Stable
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoConfigTemplate {
    /**
     * 配置模板文件路径
     *
     * @return 配置模板文件路径
     */
    String value();
}
