package com.github.cao.awa.kalmia.login

import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import org.checkerframework.framework.qual.Unused

object LoginCommon {
    @JvmStatic
    fun login(accessIdentity: LongAndExtraIdentity, router: RequestRouter) {
        Kalmia.SERVER.login(
            accessIdentity, router
        )

        router.accessIdentity(accessIdentity)
    }

    @JvmStatic
    fun logout(accessIdentity: LongAndExtraIdentity, router: RequestRouter) {
        Kalmia.SERVER.logout(
            accessIdentity, router
        )
    }
}
