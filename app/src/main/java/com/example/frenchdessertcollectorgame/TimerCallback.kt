package com.example.frenchdessertcollectorgame

interface TimerCallback {
    fun getTime(): Long
    fun addTime(additionalTime: Long)
}