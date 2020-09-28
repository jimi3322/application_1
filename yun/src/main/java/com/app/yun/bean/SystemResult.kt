package com.app.yun.bean

data class SystemResult(
//                        var courseId: Int = 0,
                        var id: Int = 0,
                        var name: String? = null,
//                        var order: Int = 0,
//                        var parentChapterId: Int = 0,
//                        var isUserControlSetTop: Boolean = false,
//                        var visible: Int = 0,
                        var children: List<ChildrenBean>? = null) {

    /**
     * children : [{"children":[],"courseId":13,"id":60,"name":"Android Studio相关","order":1000,"parentChapterId":150,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":169,"name":"gradle","order":1001,"parentChapterId":150,"userControlSetTop":false,"visible":1},{"children":[],"courseId":13,"id":269,"name":"官方发布","order":1002,"parentChapterId":150,"userControlSetTop":false,"visible":1}]
     * courseId : 13
     * id : 150
     * name : 开发环境
     * order : 1
     * parentChapterId : 0
     * userControlSetTop : false
     * visible : 1
     */



    /**
     * 标示当前 item 是否选中的状态
     */
    private var isSelected = false
    fun isSelected(b: Boolean): Boolean{
        return isSelected
    }

    fun setSelected(select: Boolean) {
        isSelected  = select
    }

    /*fun getChildren(): List<SystemResult.ChildrenBean>? {
        return children
    }*/
    fun getChildrens(): List<ChildrenBean>? {
        return children
    }


    data class ChildrenBean(
//                            var courseId: Int = 0,
//                            var id: Int = 0,
                            var name: String? = null
//                            var order: Int = 0,
//                            var parentChapterId: Int = 0,
//                            var isUserControlSetTop: Boolean = false,
//                            var visible: Int = 0
    ){
        /**
         * children : []
         * courseId : 13
         * id : 60
         * name : Android Studio相关
         * order : 1000
         * parentChapterId : 150
         * userControlSetTop : false
         * visible : 1
         */


//        var children: List<*>? = null
    }

}
