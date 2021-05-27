package com.sunrisingappdev.kmunittestissue

expect fun platformName(): String

fun createApplicationScreenMessage() : String {
    return "Kotlin Rocks on ${platformName()}!"
}
