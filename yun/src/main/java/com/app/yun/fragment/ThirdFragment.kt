package com.app.yun.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.myapplication.utils.CommonUtil
import com.app.yun.BuildConfig
import com.app.yun.R
import com.app.yun.base.BaseFragment
import com.app.yun.dialog.*
import com.wayeal.thirdpartyoperation.view.Pic
import kotlinx.android.synthetic.main.dialog_buttom.*
import kotlinx.android.synthetic.main.fragment_third.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ThirdFragment : BaseFragment() {
    val REQUEST_PICK_IMAGE = 11101
    val REQUEST_CODE_TAKE_PHOTO = 11104
    //拍照图片的path
    private var cameraImagePath: String? = null
    //拍照的图片URI
    private var camearImageUri: Uri? = null
    var pictureList: ArrayList<Pic> = ArrayList()

    //相片内容的adapter
    private var mAdapter: PicMgrAdapter? = null
    //当前正在处理图片的pos
    private var mCurrentHandlePicPosition = 0

    override fun getLayoutResId(): Int {
        return R.layout.fragment_third
    }

    override fun initView(rootView: View?) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("xq", "onViewCreated: ")

        top_dialog.setOnClickListener {
            TopDialogFragment().show(fragmentManager!!, "dialog_top")
        }
        view_dialog.setOnClickListener {
            ViewDialogFragment().show(fragmentManager!!, "view_dialog")
        }
        d_dialog.setOnClickListener {
            DialogDFragment().show(fragmentManager!!, "d_dialog")
        }
        buttom_dialog.setOnClickListener {
            BottomDialogFragment().show(fragmentManager!!, "buttom_dialog")
        }

        //初始化相片recyclerview
        initPictureRecyclerView()
    }

    override fun initData() {

    }


    /**
     * 初始化RV，这里注意几点，处理头部控制收起和展开 处理图片添加 处理图片删除
     * */
    private fun initPictureRecyclerView() {
        formPicName.text = "相册"
        PicName_title_view.visibility=View.VISIBLE
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL

        formPicList.addItemDecoration(
            SpaceItemDecoration(
                right = 20
            )
        )
        formPicList.layoutManager = manager
        val adapter = PicMgrAdapter(
            mContext,
            200
        )
        adapter.setProportion(1.0f)
        formPicList.adapter = adapter

        Log.i("xq", "开始pictureList: "+pictureList)

        adapter.setPicClickListener(object : PicMgrAdapter.PicClickListener {
            override fun onPicClick(view: View?, pos: Int) {
                val intent = Intent(context, ShowPictureActivity::class.java)
                intent.putExtra(ShowPictureActivity.CURRENT_POSITION, pos)
                pictureList?.let {
                    CommonDataManager.addImages(pictureList)
                    startActivity(intent)
                }
            }

            override fun onAddClick(view: View?) {
                openCamera()
            }

            override fun onDeletePic(pos: Int) {
                try{
//                    pictureList.removeAt(pos)
                    Log.i("xq", "删除的位置:$pos,删除pictureList:$pictureList")
                }catch (e:java.lang.Exception){

                }
            }
        })

        adapter.list = pictureList
    }


    /**
     * 判断app有没有相机权限，若没有，动态申请
     * */
    private fun openCamera(){
        val mPermissionList = arrayOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA)
        val storagePermission = ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraPermission = ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.CAMERA)
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity()!!, mPermissionList, REQUEST_PICK_IMAGE)
            } else {
                Toast.makeText(getActivity()!!, "请在设置中打开存储权限", Toast.LENGTH_LONG).show()
            }
        }else if (cameraPermission != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity()!!, mPermissionList, REQUEST_PICK_IMAGE)
            } else {
                Toast.makeText(getActivity()!!, "请在设置中打开相机权限", Toast.LENGTH_LONG).show()
            }
        }else{
            takePhoto()
        }
    }

    /**
     * 拍照
     * */
    private fun takePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp"
        val file = File(Environment.getExternalStorageDirectory().path, "${imageFileName}.jpg")
        cameraImagePath = file.path
        val uri: Uri
        uri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(getActivity()!!,
                BuildConfig.APPLICATION_ID+".fileprovider",file)
        } else {
            Uri.fromFile(file)
        }

        Log.i("xq", "file: "+file)
        camearImageUri = uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            handleCameraPic(cameraImagePath!!)
        }
    }

    /**
     * 处理拍照结果，拍照只有一张图片，所以这里直接加到mDataList中即可
     * */
    private fun handleCameraPic(imagePath: String) {
        Log.d("xq", "展示相机或者裁剪的图片 path = $imagePath")
        var bitmap: Bitmap? = null
        try {
            val file = File(imagePath)
            CommonUtil.compressBmpFileToTargetSize(file, 1000 * 1024)
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(imagePath)
            }
        } catch (e: Exception) {
        }
        val pic = Pic(id = UUID.randomUUID().toString(), path = imagePath, bitmap = bitmap)
        pictureList?.add(pic)
        Log.i("xq", "添加pictureList: "+pictureList)
        mAdapter?.notifyDataSetChanged()
    }
}