package com.yucox.pockettorocketto.Repository

import androidx.lifecycle.LiveData
import com.yucox.pockettorocketto.Model.Favorite
import org.mongodb.kbson.ObjectId

interface IFavoriteRepository {
    fun getData() : LiveData<List<Favorite>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun updateFavorite(favorite: Favorite)
    suspend fun deleteFavorite(id: ObjectId)
    fun filterByName(key : String) : LiveData<List<Favorite>>
}