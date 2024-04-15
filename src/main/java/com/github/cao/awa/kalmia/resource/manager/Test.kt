package com.github.cao.awa.kalmia.resource.manager

import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.apricot.util.io.IOUtil

import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val manager = ResourcesManager("res")

        val output = ByteArrayOutputStream()
        try {
            val fileOutput: FileOutputStream = FileOutputStream("res/b.txt");

            manager.getShardedResource("A.txt", 16384) { _, bytes, _ ->
                try {
                    output.write(bytes)
                    fileOutput.write(bytes)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            fileOutput.close()

            println(MessageDigger.digest(output.toByteArray(), MessageDigger.Sha3.SHA_512))

            var read: ByteArray = IOUtil.readBytes(BufferedInputStream(FileInputStream("res/A.txt")))
            println(MessageDigger.digest(read, MessageDigger.Sha3.SHA_512))

            read = IOUtil.readBytes(FileInputStream("res/b.txt"))
            println(MessageDigger.digest(read, MessageDigger.Sha3.SHA_512))
        } catch (_: Exception) {

        }
    }
}
