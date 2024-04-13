package com.github.cao.awa.kalmia.language.manager

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.io.IOUtil
import com.github.cao.awa.kalmia.constant.KalmiaConstant
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.io.File
import java.io.FileReader
import java.util.Arrays
import java.util.Objects

class LanguageTranslationManager {
    private val translations: MutableMap<String, MutableMap<String, String>> = ApricotCollectionFactor.hashMap()

    fun load() {
        val resDir = File(KalmiaConstant.LANGUAGE_TRANSLATION_RESOURCE_PATH)
        for (langDir in Objects.requireNonNull(resDir.listFiles())) {
            for (langFile in Objects.requireNonNull(langDir.listFiles())) {
                var langName: String = langFile.name
                if (langName.endsWith(".json")) {
                    langName = langName.replace(".json", "")

                    translations.compute(langName) { _, translationKeyMap ->
                        return@compute translationKeyMap ?: ApricotCollectionFactor.hashMap()
                    }

                    try {
                        val json: JSONObject = JSONObject.parse(IOUtil.read(FileReader(langFile)))

                        val translationMap: MutableMap<String, String> =
                            translations[langName] ?: throw RuntimeException()

                        loadInner(json, translationMap)
                    } catch (e: Exception) {
                        LOGGER.error("Failed to load language '{}' at '{}'", langName, langFile.getAbsolutePath(), e)
                    }
                }
            }
        }
    }

    private fun loadInner(json: JSONObject, translationMap: MutableMap<String, String>) {
        json.forEach { key, value ->
            loadInner(key, value, translationMap)
        }
    }

    private fun loadInner(key: String, value: Any, translationMap: MutableMap<String, String>) {
        if (value is JSONObject) {
            loadInner(value, translationMap)
            return
        }
        if (value is JSONArray) {
            value.forEach { o ->
                if (o is JSONObject) {
                    loadInner(key, o, translationMap)
                    return
                }
                if (o is JSONArray) {
                    loadInner(key, o, translationMap)
                    return
                }
                throw IllegalArgumentException("Language array can only contains JSONObject or JSONArray object")
            }
            return
        }
        translationMap[key] = value.toString()
    }

    fun translation(languageKey: String, translationKey: String, vararg args: Any): String {
        val translationKeyMap: Map<String, String> = translations[languageKey] ?: return translationKey

        val translationValue: String = translationKeyMap[translationKey] ?: return translationKey

        // TODO
        return translationValue.formatted(Arrays.stream(args).map { o -> translation(languageKey, o.toString()) }
            .toArray())
    }

    companion object {
        private val LOGGER: Logger = LogManager.getLogger("LanguageTranslationManager")
    }
}
