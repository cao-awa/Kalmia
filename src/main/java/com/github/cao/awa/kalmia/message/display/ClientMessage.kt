package com.github.cao.awa.kalmia.message.display

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.message.Message

class ClientMessage(
    private val identity: LongAndExtraIdentity,
    private val sessionIdentity: PureExtraIdentity,
    private val seq: Long,
    private val content: ClientMessageContent
) {
    companion object {
        @JvmStatic
        fun create(json: JSONObject): ClientMessage {
            val identity = LongAndExtraIdentity.create(json.getJSONObject("identity"))
            val sessionIdentity = PureExtraIdentity.create(json.getString("session_identity"))
            val seq = json.getLong("seq")
            val content = ClientMessageContent.create(json.getJSONObject("content"))

            return ClientMessage(
                identity,
                sessionIdentity,
                seq,
                content
            )
        }

        @JvmStatic
        fun create(sessionIdentity: PureExtraIdentity, seq: Long, message: Message): ClientMessage {
            return ClientMessage(
                message.identity(),
                sessionIdentity,
                seq,
                message.display()
            )
        }
    }

    fun sessionIdentity(): PureExtraIdentity = this.sessionIdentity

    fun seq(): Long = this.seq

    fun sourceContent(): String = this.content.sourceContent()

    fun coverContent(): String = this.content.coverContent()

    fun sender(): LongAndExtraIdentity = this.content.sender()

    fun content(): ClientMessageContent = this.content

    fun identity(): LongAndExtraIdentity = this.identity

    fun timestamp(): Long = this.identity.longValue()

    fun export(): JSONObject {
        return JSONObject()
            .fluentPut("identity", this.identity.toJSON())
            .fluentPut("session_identity", this.sessionIdentity.toString())
            .fluentPut("seq", this.seq)
            .fluentPut("content", this.content.export())
    }
}