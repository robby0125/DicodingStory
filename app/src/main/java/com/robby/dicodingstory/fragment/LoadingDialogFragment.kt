package com.robby.dicodingstory.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.robby.dicodingstory.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {
    private var binding: FragmentLoadingDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        isCancelable = false
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingMessage = arguments?.getString(EXTRA_LOADING_MESSAGE)
        if (loadingMessage != null) {
            binding?.tvLoadingMessage?.text = loadingMessage
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_LOADING_MESSAGE = "extra_loading_message"
    }
}