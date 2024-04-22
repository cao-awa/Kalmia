package com.github.cao.awa.kalmia.config.constant;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.template.constants.KalmiaConstantsConfigTemplate;

@UseConfigTemplate(KalmiaConstantsConfigTemplate.class)
public class KalmiaConstantsConfig extends KalmiaConfig {
    @AutoConfig("true")
    public final ConfigEntry<Boolean> trueValue = ConfigEntry.entry();
    @AutoConfig("false")
    public final ConfigEntry<Boolean> falseValue = ConfigEntry.entry();
    @AutoConfig("k")
    public final ConfigEntry<Integer> k = ConfigEntry.entry();
    @AutoConfig("k16")
    public final ConfigEntry<Integer> k16 = ConfigEntry.entry();
    @AutoConfig("int_max")
    public final ConfigEntry<Integer> intMax = ConfigEntry.entry();
    @AutoConfig("long_max")
    public final ConfigEntry<Long> longMax = ConfigEntry.entry();
    @AutoConfig("server_port")
    public final ConfigEntry<Integer> serverPort = ConfigEntry.entry();
    @AutoConfig("ciphers_directory")
    public final ConfigEntry<String> ciphersDirectory = ConfigEntry.entry();
}
