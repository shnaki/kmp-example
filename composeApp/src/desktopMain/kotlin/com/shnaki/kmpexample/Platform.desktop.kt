package com.shnaki.kmpexample

class DesktopPlatform : Platform {
    override val name: String =
        "Desktop · Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = DesktopPlatform()
