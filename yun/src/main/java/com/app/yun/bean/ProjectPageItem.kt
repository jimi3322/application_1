package com.app.yun.bean

import androidx.fragment.app.Fragment

class ProjectPageItem {

    var id: Int = 0
    var name: String? = null
    var fragment: Fragment? = null

    constructor() {}

    constructor(id: Int, name: String, fragment: Fragment) {
        this.id = id
        this.name = name
        this.fragment = fragment
    }
}
