package com.github.kusa233.kalmia.server.network.http.metadata

import com.github.cao.awa.cason.annotation.Field
import com.github.kusa233.kalmia.server.network.transport.TransportMetadata

data class HttpResponseMetadata(
    @Field("http_status")
    val status: Int?,
    @Field("http_version")
    val protocolVersion: String?
): TransportMetadata()