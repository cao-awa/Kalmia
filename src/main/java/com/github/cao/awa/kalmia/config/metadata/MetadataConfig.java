package com.github.cao.awa.kalmia.config.metadata;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.template.metadata.MetadataConfigTemplate;

@UseConfigTemplate(MetadataConfigTemplate.class)
public class MetadataConfig extends KalmiaConfig {
    @AutoConfig("version")
    public final ConfigEntry<Integer> configVersion = ConfigEntry.entry();
}
