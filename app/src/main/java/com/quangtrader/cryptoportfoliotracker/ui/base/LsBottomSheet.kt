package com.quangtrader.cryptoportfoliotracker.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class LsBottomSheet<VB : ViewBinding>(
    val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
): BottomSheetDialogFragment() {

    lateinit var binding: VB
    abstract fun isFull(): Boolean
    abstract fun viewCreated()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewCreated()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            if (isFull()){
                fun setupFullHeight(bottomSheet: View) {
                    val layoutParams = bottomSheet.layoutParams
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    bottomSheet.layoutParams = layoutParams
                }
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
                parentLayout?.let {
                    val behaviour = BottomSheetBehavior.from(parentLayout)
                    setupFullHeight(parentLayout)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    behaviour.skipCollapsed = true
                    behaviour.isDraggable = false
                }
            }
        }
        return dialog
    }
}