package me.laont.demo.kotlin.steganography

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

const val END = "000000000000000000000011"

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        when (val task = readln()) {
            "hide" -> hide()
            "show" -> show()
            "exit" -> break
            else -> println("Wrong task: $task")
        }
    }
    println("Bye!")
}

fun hide() {
    println("Input image file:")
    val inputPath = readln()
    println("Output image file:")
    val outputPath = readln()
    println("Message to hide:")
    val message = readln()
    println("Password:")
    val password = readln()

    val inputFile = File(inputPath)
    val outputFile = File(outputPath)

    if (!inputFile.exists()) {
        println("Can't read input file!")
        return
    }

    val messageBits = encrypt(stringToBits(message), stringToBits(password)) + END

    val image = ImageIO.read(inputFile)

    if (image.width * image.height < messageBits.length) {
        println("The input image is not large enough to hold this message.")
        return
    }

    for (i in messageBits.indices) {
        val x = i % image.width
        val y = i / image.width

        val pixel = Color(image.getRGB(x, y))

        val r = pixel.red
        val g = pixel.green
        val b = pixel.blue and 254 or messageBits[i].digitToInt()

        image.setRGB(x, y, Color(r, g, b).rgb)
    }

    ImageIO.write(image, "png", outputFile)

    println("Message saved in $outputPath image.")
}

fun show() {
    println("Input image file:")
    val inputPath = readln()
    println("Password:")
    val password = readln()

    val inputFile = File(inputPath)

    if (!inputFile.exists()) {
        println("Can't read input file!")
        return
    }

    val image = ImageIO.read(inputFile)

    var messageBits = ""

    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            messageBits += Color(image.getRGB(x, y)).blue.toString(2).last()
        }
    }

    val message = bitsToString(decrypt(messageBits.split(END)[0], stringToBits(password)))
    println("Message:")
    println(message)
}


fun stringToBits(message: String): String {
    return message.map {
        it.code.toString(2).padStart(8, '0')
    }.joinToString("")
}

fun bitsToString(bits: String): String {
    return bits.chunked(8).map {
        it.toInt(2).toChar()
    }.joinToString("")
}

fun encrypt(messageBits: String, passwordBits: String): String {
    val messageBytes = messageBits.chunked(8)
    val passwordBytes = passwordBits.chunked(8)

    var encrypted = ""

    for (i in messageBytes.indices) {
        val byte = messageBytes[i].toInt(2) xor passwordBytes[i % passwordBytes.size].toInt(2)
        encrypted += byte.toString(2).padStart(8, '0')
    }

    return encrypted
}

fun decrypt(messageBits: String, passwordBits: String): String {
    val messageBytes = messageBits.chunked(8)
    val passwordBytes = passwordBits.chunked(8)

    var decrypted = ""

    for (i in messageBytes.indices) {
        val byte = messageBytes[i].toInt(2) xor passwordBytes[i % passwordBytes.size].toInt(2)
        decrypted += byte.toString(2).padStart(8, '0')
    }

    return decrypted
}