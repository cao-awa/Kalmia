package com.github.cao.awa.kalmia.config.server.translation;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.metadata.MetadataConfig;
import com.github.cao.awa.kalmia.config.template.server.KalmiaServerConfigTemplate;

@UseConfigTemplate(KalmiaServerConfigTemplate.class)
public class KalmiaTranslationServerConfig extends KalmiaConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("enabled")
    public final ConfigEntry<Boolean> enabled = ConfigEntry.entry();
}
