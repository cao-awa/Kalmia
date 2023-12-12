package com.github.cao.awa.kalmia.language.manager;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Objects;

public class LanguageTranslationManager {
    private static final Logger LOGGER = LogManager.getLogger("LanguageTranslationManager");
    private final Map<String, Map<String, String>> translations = ApricotCollectionFactor.hashMap();

    public void load() {
        File resDir = new File(KalmiaConstant.LANGUAGE_TRANSLATION_RESOURCE_PATH);
        for (File langDir : Objects.requireNonNull(resDir.listFiles())) {
            for (File langFile : Objects.requireNonNull(langDir.listFiles())) {
                String langName = langFile.getName();
                if (langName.endsWith(".json")) {
                    langName = langName.replace(".json",
                                                ""
                    );

                    this.translations.compute(langName,
                                              (languageKey, translationKeyMap) -> {
                                                  if (translationKeyMap == null) {
                                                      translationKeyMap = ApricotCollectionFactor.hashMap();
                                                  }
                                                  return translationKeyMap;
                                              }
                    );

                    try {
                        JSONObject json = JSONObject.parse(IOUtil.read(new FileReader(langFile)));

                        Map<String, String> translationKeyMap = this.translations.get(langName);

                        json.forEach((key, value) -> {
                            translationKeyMap.put(
                                    key,
                                    value.toString()
                            );
                        });
                    } catch (Exception e) {
                        LOGGER.error("Failed to load language '{}' at '{}'",
                                     langName,
                                     langFile.getAbsolutePath()
                        );
                    }
                }
            }
        }
    }

    public String translation(String languageKey, String translationKey, Object... args) {
        Map<String, String> translationKeyMap = this.translations.get(languageKey);
        if (translationKeyMap == null) {
            return translationKey;
        }

        String translationValue = translationKeyMap.get(translationKey);

        return translationValue == null ? translationKey : translationValue.formatted(args);
    }
}
