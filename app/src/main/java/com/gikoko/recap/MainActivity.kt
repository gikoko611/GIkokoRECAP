package com.gikoko.recap

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    companion object {
        // Website URL ပြောင်းချင်ရင် ဒီကြောင့်ပဲ ပြောင်းပါ
        const val HOME_URL = "https://trustmeaipro.org"
    }

    // App အပြင်မှာ ဖွင့်ရမယ့် link တွေ
    private val externalHosts = listOf(
        "facebook.com", "m.me", "wa.me", "api.whatsapp.com",
        "t.me", "youtube.com", "youtu.be"
    )

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    private lateinit var fileUploadLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progressBar = findViewById(R.id.progressBar)

        // Video file upload (site မှ file chooser ဖွင့်ရန်)
        fileUploadLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val files = WebChromeClient.FileChooserParams.parseResult(
                result.resultCode, result.data
            )
            fileUploadCallback?.onReceiveValue(files ?: arrayOf())
            fileUploadCallback = null
        }

        // Swipe down = refresh
        swipeRefresh.setOnRefreshListener { webView.reload() }

        // Back button = အရင် page ပြန်သွား
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) webView.goBack() else finish()
            }
        })

        setupWebView()

        if (savedInstanceState == null) {
            webView.loadUrl(HOME_URL)
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            mediaPlaybackRequiresUserGesture = false // video autoplay
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        // Login မှာ cookie မပျောက်အောင်
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                val url = request.url
                val scheme = url.scheme ?: return false

                // mailto:, tel:, intent:// link တွေ app အပြင်မှာဖွင့်
                if (scheme == "mailto" || scheme == "tel" || scheme == "intent") {
                    openExternal(url.toString())
                    return true
                }

                // Facebook / WhatsApp / Telegram / YouTube link တွေ app အပြင်မှာဖွင့်
                if (scheme == "http" || scheme == "https") {
                    val host = url.host?.lowercase() ?: return false
                    if (externalHosts.any { host == it || host.endsWith("." + it) }) {
                        openExternal(url.toString())
                        return true
                    }
                }
                // ကျန်တဲ့ link အားလုံး WebView ထဲမှာပဲ ဖွင့်မယ်
                return false
            }

            override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onReceivedError(
                view: WebView, request: WebResourceRequest, error: WebResourceError
            ) {
                if (request.isForMainFrame) {
                    // အင်တာနက်ပြတ်ရင် မြန်မာ error page ပြမယ်
                    view.loadUrl("file:///android_asset/error.html")
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                progressBar.visibility =
                    if (newProgress < 100) View.VISIBLE else View.GONE
            }

            // ★ Video file upload အတွက် — မပါရင် video ရွေးခလုတ် အလုပ်မလုပ်ပါ
            override fun onShowFileChooser(
                view: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                fileUploadCallback?.onReceiveValue(null)
                fileUploadCallback = filePathCallback
                return try {
                    fileUploadLauncher.launch(fileChooserParams.createIntent())
                    true
                } catch (e: ActivityNotFoundException) {
                    fileUploadCallback = null
                    Toast.makeText(
                        this@MainActivity,
                        "File manager မတွေ့ပါ",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            }
        }

        // ★ Video download အတွက်
        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
            downloadFile(url, userAgent, contentDisposition, mimeType)
        }
    }

    private fun openExternal(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "ဖွင့်စရာ app မတွေ့ပါ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadFile(
        url: String, userAgent: String, contentDisposition: String?, mimeType: String?
    ) {
        try {
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setMimeType(mimeType)
                val cookies = CookieManager.getInstance().getCookie(url)
                if (cookies != null) addRequestHeader("Cookie", cookies)
                addRequestHeader("User-Agent", userAgent)
                val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                setTitle(fileName)
            }
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(
                this,
                "Download စတင်ပါသည် (Downloads ဖိုလ်ဒါထဲ)",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            openExternal(url)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }
}
