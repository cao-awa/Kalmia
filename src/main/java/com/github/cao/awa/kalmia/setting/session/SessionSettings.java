package com.github.cao.awa.kalmia.setting.session;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.setting.key.session.message.processor.SessionEnabledProcessorsSetting;

public class SessionSettings {
    public static final SessionEnabledProcessorsSetting ENABLED_PROCESSORS = new SessionEnabledProcessorsSetting(ApricotCollectionFactor.arrayList());
}
