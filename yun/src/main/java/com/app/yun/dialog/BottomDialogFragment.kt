package com.app.yun.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.myapplication.utils.CommonUtil
import com.app.yun.R
import com.app.yun.photo.PhotoActivity
import com.wayeal.thirdpartyoperation.view.Pic
import kotlinx.android.synthetic.main.dialog_buttom.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BottomDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //去掉dialog的标题，需要在setContentView()之前
        this.getDialog()?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = this.getDialog()?.getWindow()
        //去掉dialog默认的padding
        window?.getDecorView()?.setPadding(0, 0, 0, 0)
        val lp = window?.getAttributes()
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置dialog的位置在底部
        lp?.gravity = Gravity.BOTTOM
        //设置dialog的动画
        lp?.windowAnimations = R.style.BottomDialogAnimation
        window?.setAttributes(lp)
        window?.setBackgroundDrawable(ColorDrawable())

        return inflater.inflate(R.layout.dialog_buttom, null)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel.setOnClickListener {
            dismiss()
        }
    }




}