package com.app.yun.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class DialogDFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(getActivity())
        builder.setTitle("提示")
            .setMessage("重写onCreateDialog其他就和Dialog设置方式相同，可以设置view或者dialog中设置setContentView和其他事件")
            .setPositiveButton("确定", null)
            .setNegativeButton("取消", null)
        return builder.create()
    }
}