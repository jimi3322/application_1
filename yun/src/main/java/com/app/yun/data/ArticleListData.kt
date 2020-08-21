package com.app.yun.data

data class ArticleListData(var data: ArticleListDatas? = null)
data class ArticleListDatas(var datas: ArrayList<Artical>)
data class Artical(
    var author: String
    ,var title:String
)