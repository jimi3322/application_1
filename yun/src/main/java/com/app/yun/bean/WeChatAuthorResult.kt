package com.app.yun.bean

data class WeChatAuthorResults(
    val data: ArrayList<WeChatAuthorResult>
)
data class WeChatAuthorResult (
    var id: Int = 0,
    var name: String? = null,
    var order: Int = 0,
    var visible: Int = 0
)

