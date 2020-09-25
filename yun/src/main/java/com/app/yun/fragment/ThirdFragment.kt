package com.app.yun.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.yun.R
import com.app.yun.dialog.BottomDialogFragment
import com.app.yun.dialog.DialogDFragment
import com.app.yun.dialog.TopDialogFragment
import com.app.yun.dialog.ViewDialogFragment
import kotlinx.android.synthetic.main.fragment_third.*


class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }
}