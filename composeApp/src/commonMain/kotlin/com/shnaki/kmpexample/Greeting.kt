package com.shnaki.kmpexample

/**
 * Simple shared business-logic class — same code runs on every platform.
 */
class Greeting {
    private val platform = getPlatform()

    fun greet(): String = "Hello, ${platform.name}! 🎉"
}
