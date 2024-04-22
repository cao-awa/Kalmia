package com.github.cao.awa.kalmia.config.template.server;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.config.AutoConfigTemplate;
import com.github.cao.awa.kalmia.config.server.KalmiaServerConfig;
import com.github.cao.awa.kalmia.config.template.ConfigTemplate;

@Auto
@AutoConfigTemplate("KalmiaServerConfigTemplate")
public class KalmiaServerConfigTemplate extends ConfigTemplate<KalmiaServerConfig> {
}
