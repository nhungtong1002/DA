package com.example.applearndriver.base

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.applearndriver.databinding.LayoutStateViewBinding

class ViewStateController private constructor(inflater: LayoutInflater) {

    enum class ViewState {
        GONE, INFO, LOADING;

        fun isLoading() = this == LOADING
    }

    private lateinit var bindToParent: () -> ViewGroup

    constructor(inflater: LayoutInflater, parent: (() -> ViewGroup)) : this(inflater) {
        bindToParent = parent
    }

    private val binding: LayoutStateViewBinding by lazy { LayoutStateViewBinding.inflate(inflater) }

    var currentState: ViewState = ViewState.GONE
        private set

    private var TAG: String = "ViewStateController"

    private fun updateState(state: ViewState) {
        Log.d(TAG,"updateState: $state")
        bind(bindToParent())
        currentState = state
        binding.root.isGone = state == ViewState.GONE
        binding.progressLoading.isVisible = state == ViewState.LOADING
        binding.layoutError.isVisible = state == ViewState.INFO
    }

    private fun bindTo(view: ConstraintLayout) {
        val layoutParams = ConstraintLayout.LayoutParams(
            0, 0
        )
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        binding.root.background = view.background
        try {
            if (binding.root.parent != null) {
                (binding.root.parent as? ViewGroup)?.removeView(binding.root)
            }
            view.addView(binding.root, layoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindTo(view: FrameLayout) {
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        binding.root.background = view.background
        try {
            if (binding.root.parent != null) {
                (binding.root.parent as? ViewGroup)?.removeView(binding.root)
            }
            view.addView(binding.root, layoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bind(parent: ViewGroup) {
        when (parent) {
            is ConstraintLayout -> bindTo(parent)
            is FrameLayout -> bindTo(parent)
        }
    }

    fun setTagForDebug(tag: String) = apply { TAG = tag }

    fun setParentView(parent: () -> ViewGroup) {
        bindToParent = parent
    }

    fun createInfo(): InfoBuilder {
        return InfoBuilder(this)
    }

    fun createLoading(): LoadingBuilder {
        return LoadingBuilder(this)
    }

    fun gone() {
        updateState(ViewState.GONE)
    }

    fun goneLoading(){
        if (currentState.isLoading()){
            gone()
        }
    }

    class InfoBuilder(private val viewStateController: ViewStateController) {
        val binding get() = viewStateController.binding

        init {
            binding.tvTitle.isVisible = false
            binding.ivContentImage.isVisible = false
            binding.btnPrimary.isVisible = false
        }
        fun setImage(@DrawableRes imgRes: Int?) = apply {
            binding.ivContentImage.isVisible = imgRes != null
            if (imgRes != null) {
                binding.ivContentImage.setImageResource(imgRes)
            }
        }

        fun setTitle(@StringRes strRes: Int?) = apply {
            binding.tvTitle.isVisible = strRes != null
            if (strRes != null) {
                binding.tvTitle.setText(strRes)
            }
        }

        fun setTitle(title: CharSequence?) = apply {
            binding.tvTitle.isVisible = title != null
            if (title != null) {
                binding.tvTitle.text = title
            }
        }

        fun setTitleColor(@ColorRes color: Int) = apply {
            binding.tvTitle.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    color
                )
            )
        }

        fun setButtonLabel(@StringRes strRes: Int?) = apply {
            binding.btnPrimary.isVisible = strRes != null
            if (strRes != null) {
                binding.tvButtonPrimary.setText(strRes)
            }
        }


        fun setButtonLabel(label: CharSequence?) = apply {
            binding.btnPrimary.isVisible = label != null
            if (label != null) {
                binding.tvButtonPrimary.text = label
            }
        }


        fun setOnButtonClickListener(listener: () -> Unit) = apply {
            binding.btnPrimary.setOnClickListener { listener() }
        }

        fun show() {
            viewStateController.updateState(ViewState.INFO)
        }
    }

    inner class LoadingBuilder(private val viewStateController: ViewStateController) {
        fun setLoadingColor(@ColorRes colorRes: Int) = apply {
            binding.progressLoading.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, colorRes))
        }
        fun show() {
            if (viewStateController.currentState != ViewState.LOADING) {
                viewStateController.updateState(ViewState.LOADING)
            }
        }
    }
}