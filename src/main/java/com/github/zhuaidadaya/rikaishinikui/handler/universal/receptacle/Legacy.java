package com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle;

import com.github.cao.awa.apricot.annotation.Stable;

@Stable
public record Legacy<N, O>(N newly, O stale) {
}
