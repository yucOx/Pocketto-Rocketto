package com.yucox.pockettorocketto.View

import ListFavoritesAdapter
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yucox.pockettorocketto.R
import com.yucox.pockettorocketto.Util.Websites.XHAMSTER
import com.yucox.pockettorocketto.Util.Websites.XNXX
import com.yucox.pockettorocketto.Util.Websites.XVIDEOS
import com.yucox.pockettorocketto.ViewModel.FavoriteViewModel
import com.yucox.pockettorocketto.databinding.ActivitySelectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectBinding
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val _viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val enterAnim = AnimationUtils.loadAnimation(
            this,
            androidx.appcompat.R.anim.abc_slide_in_top
        )
        val exitAnimation = AnimationUtils.loadAnimation(
            this,
            androidx.appcompat.R.anim.abc_slide_out_top
        )

        _viewModel.data?.observe(this) {
            if (it.isNotEmpty()) {
                val adapter = ListFavoritesAdapter(
                    this@SelectActivity,
                    it.toMutableList()
                ) { deleteId ->
                    _viewModel.updateObjectId(deleteId)
                    _viewModel.deleteFavorite()
                }
                binding.favoriteRecycler.adapter = adapter
                binding.favoriteRecycler.layoutManager = LinearLayoutManager(
                    this,
                    RecyclerView.HORIZONTAL,
                    false
                )
            }
        }

        _viewModel.versionControl.observe(this) {
            if (!it) {
                binding.versionControlIv.startAnimation(enterAnim)
                binding.versionControlIv.setImageResource(R.drawable.new_version_avaible)
            } else {
                binding.versionControlIv.startAnimation(enterAnim)
                binding.versionControlIv.setImageResource(R.drawable.version_approved)

                mainScope.launch {
                    delay(1500)
                    binding.versionControlIv.startAnimation(exitAnimation)
                    binding.versionControlIv.visibility = View.GONE
                }

                binding.buttonsConst.visibility = View.VISIBLE


                binding.xHamsterBtn.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("url", XHAMSTER)
                    startActivity(intent)
                    finish()
                }

                binding.xVideosBtn.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("url", XVIDEOS)
                    startActivity(intent)
                    finish()
                }

                binding.xnxxBtn.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("url", XNXX)
                    startActivity(intent)
                    finish()
                }

                binding.cloudFlareDownload.setOnClickListener {
                    val uri =
                        Uri.parse("https://play.google.com/store/apps/details?id=com.cloudflare.onedotonedotonedotone")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _viewModel.viewModelScope.cancel()
        mainScope.cancel()
    }
}