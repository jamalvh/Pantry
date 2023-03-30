package com.pantry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebActivity : AppCompatActivity() {
    private var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        var items = intent.getStringArrayListExtra("ITEM_SEARCH")

        var ingredients = ""

        var count = 0

        //convert from ArrayList to MutableArrayList to add to ListView
        if (items != null) {
            for (label in items) {
                ingredients += items[count]
                ingredients += "+"
                count++
            }
            ingredients += "recipes"
        }

        webView = findViewById<View>(R.id.webview) as WebView
        webView!!.webViewClient = WebViewClient()
        webView!!.loadUrl("https://www.google.com/search?q=$ingredients")
        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}