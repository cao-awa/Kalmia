package com.github.cao.awa.kalmia.setting.key.session.message.processor

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.setting.Setting
import java.util.*

class SessionEnabledProcessorsSetting(private val processors: List<UUID> = ApricotCollectionFactor.arrayList()) :
    Setting() {
    fun processors(): List<UUID> = this.processors

    override fun settingKey(): String = "enabled-message-processor"
}