package com.github.kusa233.kalmia.server.network.http.asset

import java.io.File

class KalmiaBinaryAsset(file: File) : KalmiaAsset<ByteArray>(file, file.readBytes()) {

}