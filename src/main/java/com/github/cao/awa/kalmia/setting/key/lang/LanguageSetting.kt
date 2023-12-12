package com.github.cao.awa.kalmia.setting.key.lang

import com.github.cao.awa.kalmia.setting.Setting

class LanguageSetting(private val languageKey: String) : Setting() {
    fun languageKey(): String = this.languageKey

    override fun settingKey(): String = "lang"
}