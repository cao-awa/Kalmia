package com.github.cao.awa.kalmia.config.server.bootstrap.network;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.metadata.MetadataConfig;
import com.github.cao.awa.kalmia.config.template.server.bootstarp.network.KalmiaServerBootstrapNetworkConfigTemplate;

@UseConfigTemplate(KalmiaServerBootstrapNetworkConfigTemplate.class)
public class KalmiaServerBootstrapNetworkConfig extends KalmiaConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("bind_host")
    public final ConfigEntry<String> bindHost = ConfigEntry.entry();
    @AutoConfig("bind_port")
    public final ConfigEntry<Integer> bindPort = ConfigEntry.entry();
    @AutoConfig("use_epoll")
    public final ConfigEntry<Boolean> useEpoll = ConfigEntry.entry();
}
