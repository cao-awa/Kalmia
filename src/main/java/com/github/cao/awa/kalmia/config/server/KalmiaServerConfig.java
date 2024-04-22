package com.github.cao.awa.kalmia.config.server;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.metadata.MetadataConfig;
import com.github.cao.awa.kalmia.config.server.bootstrap.network.KalmiaServerBootstrapNetworkConfig;
import com.github.cao.awa.kalmia.config.server.translation.KalmiaTranslationServerConfig;
import com.github.cao.awa.kalmia.config.template.server.KalmiaServerConfigTemplate;

@UseConfigTemplate(KalmiaServerConfigTemplate.class)
public class KalmiaServerConfig extends KalmiaConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("network")
    public final ConfigEntry<KalmiaServerBootstrapNetworkConfig> network = ConfigEntry.entry();
    @AutoConfig("translation")
    public final ConfigEntry<KalmiaTranslationServerConfig> translation = ConfigEntry.entry();
    @AutoConfig("name")
    public final ConfigEntry<String> name = ConfigEntry.entry();
    @AutoConfig("use_cipher")
    public final ConfigEntry<String> useCipher = ConfigEntry.entry();
}
