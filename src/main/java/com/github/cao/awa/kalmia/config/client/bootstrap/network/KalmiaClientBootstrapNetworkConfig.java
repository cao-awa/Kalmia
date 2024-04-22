package com.github.cao.awa.kalmia.config.client.bootstrap.network;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.metadata.MetadataConfig;
import com.github.cao.awa.kalmia.config.template.client.bootstarp.network.KalmiaClientBootstrapNetworkConfigTemplate;

@UseConfigTemplate(KalmiaClientBootstrapNetworkConfigTemplate.class)
public class KalmiaClientBootstrapNetworkConfig extends KalmiaConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("connect_host")
    public final ConfigEntry<String> connectHost = ConfigEntry.entry();
    @AutoConfig("connect_port")
    public final ConfigEntry<Integer> connectPort = ConfigEntry.entry();
    @AutoConfig("use_epoll")
    public final ConfigEntry<Boolean> useEpoll = ConfigEntry.entry();
}
