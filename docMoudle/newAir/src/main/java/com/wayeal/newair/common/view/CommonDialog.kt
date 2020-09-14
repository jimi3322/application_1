package com.wayeal.newair.common.view


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import com.wayeal.newair.R


/**
 * @Describe 通用弹窗
 */

class CommonDialog(context: Context?, themeResId: Int) : Dialog(context!!, themeResId) {
    class Builder(private var context: Context) {
        private var title_1: String? = null
        private var message: String? = null
        private var positiveButtonContent: String? = null
        private var negativeButtonContent: String? = null
        private var positiveButtonListener: DialogInterface.OnClickListener? = null         //确定
        private var negativeButtonListener: DialogInterface.OnClickListener? = null         //取消
        private var contentView: View? = null
        private var color: Int = 0
        private var bold: Boolean = false   //内容是否粗体
        private var withOffSize: Float = 0.toFloat()
        private var heightOffSize: Float = 0.toFloat()

        fun setTitle(title: String): Builder {
            this.title_1 = title
            return this
        }

        fun setTitle(title: Int): Builder {
            this.title_1 = context.getText(title) as String
            return this
        }

        fun setMessage(message: Int): Builder {
            this.message = context.getText(message) as String
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setMessageColor(color: Int): Builder {
            this.color = color
            return this
        }

        fun setMessageBold(bold: Boolean):Builder {
            this.bold = bold
            return this
        }

        fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            this.positiveButtonContent = text
            this.positiveButtonListener = listener
            return this
        }

        fun setPositiveButton(textId: Int, listener: DialogInterface.OnClickListener): Builder {
            this.positiveButtonContent = context.getText(textId) as String
            this.positiveButtonListener = listener
            return this
        }

        fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            this.negativeButtonContent = text
            this.negativeButtonListener = listener
            return this
        }

        fun setNegativeButton(textId: Int, listener: DialogInterface.OnClickListener): Builder {
            this.negativeButtonContent = context.getText(textId) as String
            this.negativeButtonListener = listener
            return this
        }

        fun setContentView(v: View): Builder {
            this.contentView = v
            return this
        }

        fun setWith(v: Float): Builder {
            this.withOffSize = v
            return this
        }

        fun setContentView(v: Float): Builder {
            this.heightOffSize = v
            return this
        }

        @SuppressLint("InflateParams")
        fun create(): CommonDialog {
            /**
             * 初始化Dialog
             */
            val dialog = CommonDialog(
                context,
                R.style.custom_dialog2
            )

            /**
             * 初始化Dialog的布局页面
             */
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogLayoutView = inflater.inflate(
                R.layout.base_common_dialog,
                null
            )
            dialog.addContentView(
                dialogLayoutView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )

            if (!TextUtils.isEmpty(message)) {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_title) as TextView).text =
                    message
            }

            if (color != 0) {
                val viewById = dialogLayoutView.findViewById<View>(R.id.tv_dialog_title) as TextView
                viewById.setTextColor(color)
            }

            if (bold){
                val view = dialogLayoutView.findViewById<View>(R.id.tv_dialog_title) as TextView
                view.paint.isFakeBoldText = true
            }

            if (!TextUtils.isEmpty(title_1)) {
                dialogLayoutView.findViewById<View>(R.id.title).visibility = View.VISIBLE
                (dialogLayoutView.findViewById<View>(R.id.title) as TextView).text = title_1
            }else{
                dialogLayoutView.findViewById<View>(R.id.title).visibility = View.GONE
            }

            if (!TextUtils.isEmpty(positiveButtonContent)) {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_pos) as TextView).text =
                    positiveButtonContent
                if (positiveButtonListener != null) {
                    (dialog.findViewById<View>(R.id.tv_dialog_pos) as TextView)
                        .setOnClickListener { positiveButtonListener!!.onClick(dialog, -1) }
                }
            } else {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_pos) as TextView).visibility =
                    View.GONE
                dialogLayoutView.findViewById<View>(R.id.tv_dialog_pos).visibility = View.GONE
            }

            if (!TextUtils.isEmpty(negativeButtonContent)) {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_neg) as TextView).text =
                    negativeButtonContent
                if (negativeButtonListener != null) {
                    (dialogLayoutView
                        .findViewById<View>(R.id.tv_dialog_neg) as TextView)
                        .setOnClickListener { negativeButtonListener!!.onClick(dialog, -2) }
                }
            } else {
                (dialogLayoutView.findViewById<View>(R.id.tv_dialog_neg) as TextView).visibility =
                    View.GONE
            }
            /**
             * 将初始化完整的布局添加到dialog中
             */
            dialog.setContentView(dialogLayoutView)
            /**
             * 禁止点击Dialog以外的区域时Dialog消失
             */
            dialog.setCanceledOnTouchOutside(false)

            val window = dialog.window
            val context = this.context as Activity
            val windowManager = context.windowManager
            val defaultDisplay = windowManager.defaultDisplay
            val attributes = window!!.attributes

            if (withOffSize.toDouble() != 0.0) {
                attributes.width = (defaultDisplay.width * withOffSize).toInt()
            } else {
                attributes.width = (defaultDisplay.width * 0.77).toInt()
            }
            if (heightOffSize.toDouble() != 0.0) {

                attributes.height = (defaultDisplay.height * heightOffSize).toInt()
            }
            window.attributes = attributes
            return dialog
        }
    }
}
