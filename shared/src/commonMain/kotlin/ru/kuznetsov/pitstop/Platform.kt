package ru.kuznetsov.pitstop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform