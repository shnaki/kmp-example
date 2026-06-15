package com.shnaki.kmpexample

/**
 * Platform information — each target provides its own `actual` implementation.
 *
 * This is the core KMP pattern: write `expect` once in commonMain,
 * and provide `actual` in each platform-specific source set.
 */
interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
