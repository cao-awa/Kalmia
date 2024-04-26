package com.github.cao.awa.kalmia.config.global;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.template.global.KalmiaGlobalConfigTemplate;

@UseConfigTemplate(KalmiaGlobalConfigTemplate.class)
public class KalmiaGlobalConfig extends KalmiaConfig {
    @AutoConfig("enabled_translation")
    public final ConfigEntry<Boolean> enabledTranslation = ConfigEntry.entry();
}
