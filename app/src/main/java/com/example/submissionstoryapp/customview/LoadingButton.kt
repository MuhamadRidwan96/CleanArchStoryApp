package com.example.submissionstoryapp.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.submissionstoryapp.databinding.ViewLoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: ViewLoadingButtonBinding =
        ViewLoadingButtonBinding.inflate(LayoutInflater.from(context), this)

    private var isLoading: Boolean = false

    fun setLoading(loading: Boolean) {
        isLoading = loading
        binding.loadingAnimation.visibility = if (loading) View.VISIBLE else View.GONE
        binding.actionButton.text = if (loading) "" else "Masuk"
        binding.actionButton.isEnabled = !loading
    }

    fun setUpload(loading: Boolean) {
        isLoading = loading
        binding.loadingAnimation.visibility = if (loading) View.VISIBLE else View.GONE
        binding.actionButton.text = if (loading) "" else "Unggah"
        binding.actionButton.isEnabled = !loading
    }

    fun setUpSignUp(loading: Boolean) {
        isLoading = loading
        binding.loadingAnimation.visibility = if (loading) View.VISIBLE else View.GONE
        binding.actionButton.text = if (loading) "" else "Daftar"
        binding.actionButton.isEnabled = !loading
    }
    fun setOnClickListener(listener: (View) -> Unit) {
        binding.actionButton.setOnClickListener { if (!isLoading) listener(it) }
    }
}
