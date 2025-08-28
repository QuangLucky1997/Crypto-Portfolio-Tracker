package com.quangtrader.cryptoportfoliotracker.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.quangtrader.cryptoportfoliotracker.R


abstract class BaseDialog<V : ViewBinding>() : DialogFragment() {

    lateinit var binding: V

    abstract val _binding: (LayoutInflater, ViewGroup?, Boolean) -> V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.dialog?.window?.setBackgroundDrawableResource(R.color.backgroundDialogOrange)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = _binding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()

    }

    abstract fun onViewCreated()
    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.apply {
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(R.color.trans)
            }
        }
    }


}