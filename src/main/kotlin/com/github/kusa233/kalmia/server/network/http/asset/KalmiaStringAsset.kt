package com.github.kusa233.kalmia.server.network.http.asset

import java.io.File
import java.nio.charset.StandardCharsets

class KalmiaStringAsset(file: File) : KalmiaAsset<String>(file, file.readText(StandardCharsets.UTF_8)) {

}