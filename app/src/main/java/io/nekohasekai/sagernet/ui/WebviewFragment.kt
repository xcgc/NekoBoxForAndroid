package io.nekohasekai.sagernet.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.database.DataStore
import io.nekohasekai.sagernet.databinding.LayoutWebviewBinding
import moe.matsuri.nb4a.utils.WebViewUtil

// Fragment必须有一个无参public的构造函数，否则在数据恢复的时候，会报crash

class WebviewFragment : ToolbarFragment(R.layout.layout_webview), Toolbar.OnMenuItemClickListener {

    lateinit var mWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // layout
        toolbar.setTitle(R.string.menu_dashboard)
        toolbar.inflateMenu(R.menu.yacd_menu)
        toolbar.setOnMenuItemClickListener(this)

        val binding = LayoutWebviewBinding.bind(view)

        // webview
        mWebView = binding.webview
        mWebView.settings.domStorageEnabled = true
        mWebView.settings.javaScriptEnabled = true
        mWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                WebViewUtil.onReceivedError(view, request, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        mWebView.loadUrl(DataStore.yacdURL)
    }

    @SuppressLint("CheckResult")
    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_set_url -> {
                MaterialDialog(requireContext()).show {
                    title(R.string.set_panel_url)
                    input(
                        prefill = DataStore.yacdURL,
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
                    ) { _, str ->
                        DataStore.yacdURL = str.toString()
                        mWebView.loadUrl(DataStore.yacdURL)
                    }
                    positiveButton(R.string.save)
                }
            }
            R.id.close -> {
                mWebView.onPause()
                mWebView.removeAllViews()
                mWebView.destroy()
            }
        }
        return true
    }
}
