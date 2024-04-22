package com.github.cao.awa.kalmia.config.network.router;

import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.annotations.config.UseConfigTemplate;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.config.metadata.MetadataConfig;
import com.github.cao.awa.kalmia.config.template.network.router.RequestRouterConfigTemplate;

@UseConfigTemplate(RequestRouterConfigTemplate.class)
public class RequestRouterConfig extends KalmiaConfig {
    @AutoConfig("metadata")
    public final ConfigEntry<MetadataConfig> metadata = ConfigEntry.entry();
    @AutoConfig("compress_threshold")
    public final ConfigEntry<Integer> compressThreshold = ConfigEntry.entry();
}
