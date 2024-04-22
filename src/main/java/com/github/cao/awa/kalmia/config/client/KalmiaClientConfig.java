package com.github.cao.awa.kalmia.config.client;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.client.bootstrap.network.KalmiaClientBootstrapNetworkConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.metadata.MetadataConfig;
import com.github.cao.awa.kalmia.config.template.client.KalmiaClientConfigTemplate;

@UseConfigTemplate(KalmiaClientConfigTemplate.class)
public class KalmiaClientConfig extends KalmiaConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("network")
    public final ConfigEntry<KalmiaClientBootstrapNetworkConfig> network = ConfigEntry.entry();
    @AutoConfig("use_cipher")
    public final ConfigEntry<String> useCipher = ConfigEntry.entry();
}
