package com.github.cao.awa.kalmia.message.display

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity

class ClientMessageContent(
    private val sender: LongAndExtraIdentity,
    private val sourceContent: String,
    private val coverContent: String
) {
    companion object {
        @JvmStatic
        fun create(json: JSONObject): ClientMessageContent {
            val sender = LongAndExtraIdentity.create(json.getJSONObject("sender"))
            val sourceContent = json.getString("source_content")
            val coverContent = json.getString("cover_content")

            return ClientMessageContent(
                sender,
                sourceContent,
                coverContent
            )
        }
    }

    fun sourceContent(): String = this.sourceContent

    fun coverContent(): String = this.coverContent

    fun sender(): LongAndExtraIdentity = this.sender

    fun export(): JSONObject {
        return JSONObject()
            .fluentPut("sender", this.sender.toJSON())
            .fluentPut("source_content", this.sourceContent)
            .fluentPut("cover_content", this.coverContent)
    }
}