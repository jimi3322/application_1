package com.control.controlxq.activity.kotlinlearn

import java.security.spec.MGF1ParameterSpec
import kotlin.math.max

fun main(){
    println("hello lotlin")

    val canValue:Int = 10//val不可变var可变，优先使用val声明，可变变量易修改出错
    var cantValue:Int = 10

    fun largrNumber(num1: Int,num2:Int):Int{
        return max(num1,num2)
    }

    fun large(num1:Int,num2:Int):Int=if (num1>num2) num1 else num2

    fun getScore(name: String):Int = when (name){
        "TOM" -> 86
        else -> 0
    }

    //集合
    val list = listOf("xx","qq")
    val list2 = mutableListOf("xx")
    list2.add("qq")

    val set = setOf("xx")

    val hashMap = hashMapOf("xx" to 1,"qq" to 2)
    val numHashMap = hashMap["xx"]
    val hashMap2 = HashMap<String,Int>()
    hashMap2["xx"]=1
    hashMap2["qq"]=2
    for ((name,age) in hashMap2){
        println("name is:"+name+"------"+"age is:"+age)
        println("name is $name,    age is $age")
    }

    fun getTextNull(text:String?) = text?.length?:0     //空机制运用  content！！表示非空断言

    fun doStudy(study:Study?){                      //let与？.配合使用进行空指针检查
        study?.let{
            it.readBooks()
            it.dohomework()
        }
    }

    val lists = listOf("wo","xiang","daren")                       //with函数  run:   val sentence = StringBuilder().run{}
    val sentence = with(StringBuilder()){                            //与apply相似   val sentence = StringBuilder().apply{}
        append("开始")
        for (list in lists){
            append(list)
        }
        append("结束")
        toString()
    }
    println(sentence)
}