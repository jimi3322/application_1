package com.app.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.widget.RelativeLayout
import com.app.common.adapter.SelectCitysAdapter
import com.app.common.domain.SelectInfo
import com.app.common.utils.UIUtil
import com.app.wayee.common.R
import kotlinx.android.synthetic.main.selelct_input_layout.view.*
import kotlinx.android.synthetic.main.gis_select_pop_layout.view.*


class SingleSelectListsView : RelativeLayout{
    private var mContext:Context?=null
    private var popWindow = PopupWindow()
    private var popView : View? = null
    private var mShowList= ArrayList<SelectInfo>()
    private var selectedInfo: SelectInfo?=null
    private var  mAdapter : SelectCitysAdapter?=null
    private var mSelectListener: OnSelectedListener?=null
    private var mCancel = true
    constructor(context:Context):super(context){
        mContext = context
        init(context)
    }
    constructor(context: Context,attrs: AttributeSet): super(context,attrs,0){
        mContext = context
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        init(context)
    }

    /**
     * 初始化工作
     */
    fun init(context: Context){
        LayoutInflater.from(context).inflate(R.layout.selelct_input_layout,this,true)
        popView = LayoutInflater.from(mContext).inflate(R.layout.gis_select_pop_layout,null)
        popWindow?.apply {
            contentView = popView
            isOutsideTouchable = true//必须设置背景
            isTouchable = true
            setBackgroundDrawable(BitmapDrawable())
        }
        popView?.lists?.setOnItemClickListener { parent, view, position, id ->
            var  currentSelectedInfo  =  mShowList?.get(position)
            if(currentSelectedInfo.key == selectedInfo?.key){
                //当前选择的项和上次选择的项重复，则变为取消操作
                if(!mCancel){
                    return@setOnItemClickListener
                }
                currentSelectedInfo.isSelected = false
                selectResult.text = ""
                selectedInfo = null
            } else{
                //选择当前项
                currentSelectedInfo.isSelected = true
                if(selectedInfo!=null){
                    selectedInfo?.isSelected = false
                }
                selectedInfo = currentSelectedInfo
                selectResult.text = currentSelectedInfo.title
            }

            mAdapter?.notifyDataSetChanged()
            popWindow.dismiss()
            mSelectListener?.onSelected(selectedInfo)
        }


        this.setOnClickListener {

            //android 7.0 当页面剩余的高度 少于 pop的高度  pop就会上移  导致位置错误
            val location1 = IntArray(2)
            selectResult.getLocationInWindow(location1) //获取在当前窗口内的绝对坐标
            var calHeigh = 0
            if(mAdapter?.list?.size!=null){
                calHeigh = mAdapter?.list?.size!! * UIUtil.INSTANCE.DipToPixels(28.5f)
            }
            var selectHeight = UIUtil.INSTANCE.getmScreenHeight() - location1[1] - selectResult.measuredHeight
            if(selectHeight > calHeigh){
                selectHeight = calHeigh
            }
            popWindow?.apply {
                width = this@SingleSelectListsView.measuredWidth
                height = selectHeight
                showAsDropDown(this@SingleSelectListsView)
            }
        }
    }

    /**
     * 已经选择的项是否能取消
     */
    fun setIsCancel(can:Boolean){
        mCancel = can
    }

    fun setSelectList(selects:List<SelectInfo>?){
        if(selects==null){
            return
        }
//        if(mShowList?.indices!=null){
//            for(index in mShowList.indices){
//                mShowList?.get(index).isSelected = false
//            }
//        }
//
        mShowList.clear()
        mShowList.addAll(selects)
        if(mAdapter== null){
            mAdapter = SelectCitysAdapter(mContext,mShowList)
            popView?.lists?.adapter = mAdapter
        }else{
            mAdapter?.apply {
                list?.clear()
                list?.addAll(selects)
                notifyDataSetChanged()
            }
        }
    }

    //判断可选项是否为空
    fun isEmpty():Boolean{
        return mShowList==null || mShowList?.isEmpty()
    }

    /**
     * 設置提示文案
     */
    fun setHintText(hint :String?){
        selectResult.hint = hint
    }

    /**
     * 設置提示文案
     */
    fun setResultText(text :String?){
        if(!TextUtils.isEmpty(text)){
            selectResult.text = text
        }else{
            selectResult.text = ""
        }

    }

    /**
     * 設置提示文案字体大小
     */
    fun setResultSize(size:Float){
        selectResult.textSize = size
    }

    /**
     * 設置文字颜色
     */
    fun setResultColor(color:Int){
        selectResult.setTextColor(color)
    }


    /**
     * 设置选择项
     */
    fun setSelected(selectItem: SelectInfo?){
        selectedInfo?.isSelected = false
        selectedInfo = null
        if(mAdapter?.list?.indices !=null){
            for(index in mAdapter?.list?.indices!!){
                val item = mAdapter?.list!![index] as SelectInfo
                item.isSelected = item?.key == selectItem?.key
                if(item.isSelected){
                    selectedInfo = item
                }
            }
            mAdapter?.notifyDataSetChanged()
        }
        setResultText(selectItem?.title)
    }
    fun getSelected():SelectInfo?{
        return selectedInfo
    }
    fun setOnSelectedListener(listener: OnSelectedListener){
        mSelectListener = listener
    }

    //选择回调
    interface OnSelectedListener{
        fun onSelected(selectInfo:SelectInfo?)
    }

}