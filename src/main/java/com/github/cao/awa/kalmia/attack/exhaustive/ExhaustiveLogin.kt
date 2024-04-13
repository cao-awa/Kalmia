package com.github.cao.awa.kalmia.attack.exhaustive

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter

object ExhaustiveLogin {
    private val invalidRouters: MutableList<RequestRouter> = ApricotCollectionFactor.timedList(1500)

    @JvmStatic
    fun validate(router: RequestRouter): Boolean {
        return if (invalidRouters.contains(router)) {
            false
        } else {
            invalidRouters.add(router)
            true
        }
    }
}
