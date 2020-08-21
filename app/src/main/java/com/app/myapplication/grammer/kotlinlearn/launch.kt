package com.app.myapplication.grammer.kotlinlearn

import kotlinx.coroutines.*

fun main(){
    GlobalScope.launch {
        println("GlobalScope创建的是顶层携程")
    }
    runBlocking {
        launch {
            println("launch1 start")
            print()
            println("launch1 stop")
        }
        launch {
            println("launch2 start")
            println("launch2 stop")
        }
    }
}

suspend fun print() = coroutineScope{
    launch {
        println("suspend关键字可以让外部方法可以被协程调用")
        delay(1000)
        println("coroutineScope关键字可以让外部方法开启协程")
    }
}