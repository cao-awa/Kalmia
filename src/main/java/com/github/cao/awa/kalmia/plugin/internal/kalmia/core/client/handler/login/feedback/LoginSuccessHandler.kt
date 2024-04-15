package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.feedback

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.util.encryption.Crypto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.constant.KalmiaConstant
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginSuccessEventHandler
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.network.packet.Packet
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState
import com.github.cao.awa.kalmia.resource.upload.ResourceUpload
import com.github.cao.awa.kalmia.session.communal.CommunalSession
import com.github.cao.awa.modmdo.annotation.platform.Client

import java.io.File
import java.nio.charset.StandardCharsets
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class LoginSuccessHandler : LoginSuccessEventHandler {
    @Auto
    @Client
    override fun handle(router: RequestRouter, packet: LoginSuccessPacket) {
        println("---Login success---")
        println("UID: " + packet.accessIdentity())
        println("Token: " + Mathematics.radix(packet.token(), 36))

        router.accessIdentity(packet.accessIdentity())

        router.setStates(RequestState.AUTHED)

//        try {
//            router.send(SelectMessagePacket(CommunalSession.TEST_COMMUNAL_IDENTITY, 0, 114514))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        try {
//            router.send(
//                SendMessagePacket(
//                    CommunalSession.TEST_COMMUNAL_IDENTITY,
//                    KalmiaEnv.testKeypairIdentity0,
//                    "Awa".toByteArray(StandardCharsets.UTF_8),
//                    KalmiaEnv.testKeypairIdentity1,
//                    byteArrayOf(1),
//                    false
//                ).receipt(Packet.createReceipt())
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        try {
//            val source: ByteArray = "Awa123".toByteArray(StandardCharsets.UTF_8)
//
//            val msg: ByteArray = Crypto.ecEncrypt(
//                source, Kalmia.CLIENT.getPublicKey(KalmiaEnv.testKeypairIdentity0, true) as ECPublicKey
//            )
//            val sign: ByteArray =
//                Crypto.ecSign(source, Kalmia.CLIENT.getPrivateKey(KalmiaEnv.testKeypairIdentity1, true) as ECPrivateKey)
//
//            router.send(
//                SendMessagePacket(
//                    CommunalSession.TEST_COMMUNAL_IDENTITY,
//                    KalmiaEnv.testKeypairIdentity0,
//                    msg,
//                    KalmiaEnv.testKeypairIdentity1,
//                    sign,
//                    false
//                ).receipt(Packet.createReceipt())
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        try {
//            router.executor().execute({ ResourceUpload.upload(File("E:\\Codes\\Java\\Kalmia\\res\\a.jpg"), router) })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        try {
//            val source: ByteArray = "Test message #{TIME}".toByteArray(StandardCharsets.UTF_8)
//
//            val sign: ByteArray =
//                Crypto.ecSign(source, Kalmia.CLIENT.getPrivateKey(KalmiaEnv.testKeypairIdentity1, true) as ECPrivateKey)
//
//            router.send(
//                SendMessagePacket(
//                    CommunalSession.TEST_COMMUNAL_IDENTITY,
//                    KalmiaConstant.UNMARKED_PURE_IDENTITY,
//                    source,
//                    KalmiaEnv.testKeypairIdentity1,
//                    sign,
//                    false
//                )
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        try {
//            val source: ByteArray = "Test message #{TIME}".toByteArray(StandardCharsets.UTF_8)
//
//            router.send(
//                SendMessagePacket(
//                    CommunalSession.TEST_COMMUNAL_IDENTITY,
//                    KalmiaConstant.UNMARKED_PURE_IDENTITY,
//                    source,
//                    KalmiaConstant.UNMARKED_PURE_IDENTITY,
//                    byteArrayOf(),
//                    false
//                ).receipt(Packet.createReceipt())
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
    }
}

