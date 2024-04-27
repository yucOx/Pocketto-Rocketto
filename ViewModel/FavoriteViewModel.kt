package com.yucox.pockettorocketto.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yucox.pockettorocketto.Model.Favorite
import com.yucox.pockettorocketto.R
import com.yucox.pockettorocketto.Repository.IFavoriteRepository
import com.yucox.pockettorocketto.Repository.IFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val _favoriteRepository: IFavoriteRepository,
    private val _repositoryApp: IFirebaseRepository
) : ViewModel() {
    private val _adList = MutableLiveData<ArrayList<String>>()
    private val _versionControl = MutableLiveData<Boolean>()
    private val _message = MutableLiveData<String>()

    private val _saveName = MutableLiveData<String>()
    private val _url = MutableLiveData<String>()
    private val _siteName = MutableLiveData<String>()
    private val _objectId = MutableLiveData<String>(null)


    val adList: LiveData<ArrayList<String>> get() = _adList
    val message: LiveData<String> get() = _message
    val versionControl: LiveData<Boolean> get() = _versionControl
    var data: LiveData<List<Favorite>>? = null

    init {
        viewModelScope.launch {
            data = _favoriteRepository.getData()
        }
        checkAppVersion()
    }

    fun addFavoriteUrl() {
        viewModelScope.launch(Dispatchers.IO) {
            data?.value?.forEach {
                if (_saveName.value?.contains(it.toString()) == true) {
                    _message.value = "Already saved"
                    _message.value = ""
                    return@launch
                }
            }
            if (!_url.value.isNullOrEmpty() && _url.value != "null") {
                val favTemp = Favorite().apply {
                    url = this@FavoriteViewModel._url.value
                    saveName = this@FavoriteViewModel._saveName.value
                }
                _favoriteRepository.insertFavorite(favTemp)
                withContext(Dispatchers.Main) {
                    _message.value = "Saved successfully"
                    _message.value = ""
                }
            } else {
                withContext(Dispatchers.Main) {
                    _message.value = "Try again after 2 - 3 seconds."
                    _message.value = ""
                }
            }
        }
    }

    fun changeFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteRepository.updateFavorite(Favorite().apply {
                saveName = this@FavoriteViewModel._saveName.value
            })
        }
    }

    fun deleteFavorite() {
        viewModelScope.launch {
            if (_objectId.value?.isNotEmpty() == true) {
                _favoriteRepository.deleteFavorite(ObjectId(_objectId.value!!))
            }
        }
    }

    fun executeFilterList(context: Context) {
        val adListTemp = ArrayList<String>()
        val fileName = context.resources.openRawResource(R.raw.filterlist)
        fileName.bufferedReader().use {
            it.forEachLine { line ->
                adListTemp.add(line)
            }
        }
        _adList.value = adListTemp
    }

    private fun checkAppVersion() {
        viewModelScope.launch {
            val result = _repositoryApp.checkAppVersion()
            _versionControl.value = result
        }
    }

    fun updateName(name: String) {
        this@FavoriteViewModel._saveName.value = name
    }

    fun updateUrl(url: String) {
        this@FavoriteViewModel._url.value = url
    }

    fun updateObjectId(id: String) {
        this@FavoriteViewModel._objectId.value = id
    }

    fun updateSiteName(siteName: String) {
        this@FavoriteViewModel._siteName.value = siteName
    }
}