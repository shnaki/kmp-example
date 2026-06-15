package com.shnaki.kmpexample

class WasmJsPlatform : Platform {
    override val name: String = "Web · Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmJsPlatform()
