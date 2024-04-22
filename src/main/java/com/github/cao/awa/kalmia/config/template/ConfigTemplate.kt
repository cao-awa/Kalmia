package com.github.cao.awa.kalmia.config.template

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.config.AutoConfig
import com.github.cao.awa.kalmia.config.KalmiaConfig

@Auto
open class ConfigTemplate<T : KalmiaConfig> {
    @Auto
    @AutoConfig
    private var config: T? = null
}
