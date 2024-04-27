package com.yucox.pockettorocketto.View

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import com.yucox.pockettorocketto.Util.SynchroneScreen.FULLSCREEN
import com.yucox.pockettorocketto.Util.SynchroneScreen.LANDSCAPE
import com.yucox.pockettorocketto.Util.SynchroneScreen.NAVBAR
import com.yucox.pockettorocketto.Util.SynchroneScreen.PORTRAIT
import com.yucox.pockettorocketto.Util.Websites.XHAMSTER
import com.yucox.pockettorocketto.Util.Websites.XVIDEOS
import com.yucox.pockettorocketto.ViewModel.FavoriteViewModel
import com.yucox.pockettorocketto.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mainScope: CoroutineScope
    private lateinit var selectedSite: String
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val uiController = window.insetsController

        mainScope = CoroutineScope(Dispatchers.Main)

        viewModel.executeFilterList(this)

        sharedPreferences = getSharedPreferences("SavedActivity", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        viewModel.message.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(
                    this, it, Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.bookmarkBtn.setOnClickListener {
            viewModel.updateUrl(binding.webView.url.toString())
            viewModel.updateSiteName(selectedSite)
            viewModel.addFavoriteUrl()
        }

        viewModel.adList.observe(this) {
            if (!it.isNullOrEmpty()) {
                setChromeClient(uiController)
                val superWebViewClient = initWebClient()
                binding.webView.webViewClient = superWebViewClient

                selectedSite = intent.getStringExtra("url").toString()
                if (selectedSite == XHAMSTER) {
                    binding.webView.loadUrl(XHAMSTER)
                } else {
                    binding.webView.loadUrl(selectedSite)
                }
            }
        }

        binding.webView.settings.javaScriptEnabled = true

        binding.rotateScreenBtn.setOnClickListener {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                requestedOrientation = LANDSCAPE
            } else {
                requestedOrientation = PORTRAIT
            }
        }

    }

    private fun initWebClient(): WebViewClient {
        val superClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                fun getVideoName() {
                    view?.evaluateJavascript(
                        "(function() { " +
                                "var contentTitle = document.querySelector('[data-role=\"video-title\"] h1');" +
                                "return contentTitle.textContent.trim();" +
                                "})();"
                    ) { result ->
                        viewModel.updateName(result)
                    }
                }
                getVideoName()
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                val adList = viewModel.adList.value
                adList?.forEach {
                    if (url.contains(it)) {
                        return true
                    }
                }
                return false
            }

            override fun shouldInterceptRequest(
                view: WebView?, request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url.toString()

                val tempList = viewModel.adList.value

                tempList?.forEach { adHost ->
                    if (url.contains(adHost)) {
                        println(url)
                        return WebResourceResponse("text/plain", "utf-8", null)
                    }
                }

                mainScope.launch {
                    if (selectedSite == XHAMSTER || selectedSite.contains(XHAMSTER))
                        whatTheCosmeticsMrHamster(view)
                    else if (selectedSite == XVIDEOS)
                        whatTheCosmeticsXvideos(view)
                }

                return super.shouldInterceptRequest(view, request)
            }


        }
        return superClient
    }

    private fun whatTheCosmeticsXvideos(view: WebView?) {
        fun topBanner() {
            view?.evaluateJavascript(
                "document.querySelector('.ad-header-mobile-contener').style.display='none';",
                null
            );
        }
        topBanner()
    }

    private fun whatTheCosmeticsMrHamster(view: WebView?) {
        closeTrafficStars(view)
        popUpAdTop(view)
        downBanner(view)
        hideWatchMoreBanner(view)
        fullScreenAd(view)
        cssFilters(view)
        easyListSpecialized(view)
        blockSkipAd(view)
    }

    private fun blockSkipAd(view: WebView?) {
        view?.loadUrl(
            "javascript:(function() { " +
                    "document.querySelector('.xplayer-ads-block__skip').style.display = 'none'; " +
                    "})()"
        )
    }

    private fun easyListSpecialized(view: WebView?) {
        val myJavaScriptString = """
    javascript:(function() {
        var elements = document.querySelectorAll('._029ef-containerBottomSpot, ._80e65-containerBottomSpot, ._80e65-containerPauseSpot,
         .bottom-widget-section,
         .player-add-overlay,
         .promo-message,
         .sponsor,
         .video-view-ads,
         .wio-p,
         .wio-pcam-thumb,
         .wio-psp-b,
         .wio-xbanner,
         .wio-xspa,
         .wixx-ebanner,
         .wixx-ecam-thumb,
         .wixx-ecams-widget,
         .xplayer-b,
         .xplayer-banner,
         .yfd-fdcam-thumb,
         .yfd-fdcams-widget,
         .yfd-fdclipstore-bottom,
         .yfd-fdsp-b,
         .yld-mdcam-thumb,
         .yld-mdsp-b,
         .ytd-jcam-thumb,
         .ytd-jcams-widget,
         .ytd-jsp-a,
         .yxd-jcam-thumb,
         .yxd-jcams-widget,
         .yxd-jdbanner,
         .yxd-jdcam-thumb,
         .yxd-jdcams-widget,
         .yxd-jdplayer,
         .yxd-jdsp-b,
         .yxd-jdsp-l-tab,
         .yxd-jsp-a,
         .[class^="xplayer-banner"],
         .a[href^="https://flirtify.com/"],
         .div[class*="promoMessageBanner-"],
        .clipstore-bottom');
        elements.forEach(function(element) {
            element.style.display = 'none';
        });
    })();
""".trimIndent()
        view?.evaluateJavascript(myJavaScriptString, null)
    }

    private fun cssFilters(view: WebView?) {
        val css = """
       .ad,
    .adbanner,
    .ads-banner,
    .ads-bottom,
    .ads-manager,
    .ads/assets,
    .display-ads,
    .footerads,
    .housead,
    .page-ad,
    .page-peel,
    .PcmModule-Taboola,
    .peel-ads,
    .popexit,
    .popunder,
    .publicidad,
    .right-ad,
    .side-ad,
        .sidebar-ad {
    display: none !important;
}
""".trimIndent()

        val javascript = """
var style = document.createElement('style');
style.innerHTML = '$css';
document.head.appendChild(style);
"""
        view?.evaluateJavascript(javascript, null)
    }

    private fun fullScreenAd(view: WebView?) {
        view?.evaluateJavascript(
            "(function() { " +
                    "document.querySelector('.video-view-ads__container').style.display='none'; " +
                    "})()", null
        );

    }

    private fun hideWatchMoreBanner(view: WebView?) {
        view?.evaluateJavascript(
            "(function() { " +
                    "document.querySelector('#player-sponsor-link').style.display='none'; " +
                    "})()", null
        );

        view?.evaluateJavascript(
            "(function() { " +
                    "document.querySelector('.xplayer-ads-block, .xplayer-ads-block__link').style.display='none'; " +
                    "})()", null
        );


    }

    private fun downBanner(view: WebView?) {
        view?.evaluateJavascript(
            "document.querySelector('.auto-generated-banner-container').style.display='none';",
            null
        );


        val javascript = """
            (function() {
                var element = document.querySelector('.video-view-ads');
                if (element) {
                    element.style.display = 'none';
                }
                var element2 = document.querySelector('.video-view-ads--full-page');
                if (element2) {
                    element2.style.display = 'none';
                }
            })();
""".trimIndent()
        view?.evaluateJavascript("javascript:$javascript", null)

        //official website direction
        view?.evaluateJavascript(
            "document.querySelector('.CZjXl-QyGevunder-player-banner-wrapper').style.display='none';",
            null
        );
        view?.evaluateJavascript(
            "document.querySelector('.CZjXl-QyGevpremium-n-overlay').style.display='none';",
            null
        );

        view?.loadUrl(
            "javascript:(function() { " +
                    "var element = document.getElementsByClassName('xplayer-ads-block-companion')[0];" +
                    "element.style.display='none'; " +
                    "})()"
        )
    }

    fun closeTrafficStars(view: WebView?) {
        view?.evaluateJavascript(
            "document.querySelector('.pOK-sFxbts.traffic-stars.mntv').style.display='none';",
            null
        );

    }

    private fun popUpAdTop(view: WebView?) {
        view?.evaluateJavascript(
            "document.querySelector('._20a76da7').style.display='none';",
            null
        );

        view?.evaluateJavascript(
            "document.querySelector(\"[data-role='pOK-sFxbpromo']\").style.display='none';",
            null
        );

        view?.evaluateJavascript(
            "document.querySelector(\"[data-role='pOK-sFxbtop']\").style.display='none';",
            null
        );
        view?.evaluateJavascript(
            "document.querySelector('.CZjXl-QyGev CZjXl-QyGev--top, .CZjXl-QyGevno-ts-init').style.display='none';",
            null
        );

    }


    private fun setChromeClient(uiController: WindowInsetsController?) {
        val webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                binding.webView.visibility = View.GONE
                binding.customView.visibility = View.VISIBLE
                binding.customView.addView(view)
                binding.rotateScreenBtn.visibility = View.GONE
                uiController?.hide(FULLSCREEN + NAVBAR)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                binding.webView.visibility = View.VISIBLE
                binding.customView.visibility = View.GONE
                binding.rotateScreenBtn.visibility = View.VISIBLE
            }
        }
        binding.webView.webChromeClient = webChromeClient
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            val intent = Intent(
                this, SelectActivity::class.java
            )
            startActivity(intent)
            finish()
            super.onBackPressed()
        }
    }
}