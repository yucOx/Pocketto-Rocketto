package com.yucox.pockettorocketto.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.yucox.pockettorocketto.Model.Favorite
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class FavoriteRepository(val realm: Realm) : IFavoriteRepository {
    override fun getData(): LiveData<List<Favorite>> {
        return realm.query<Favorite>().asFlow().map { it.list }.asLiveData()
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        realm.write { copyToRealm(favorite) }
    }

    override suspend fun updateFavorite(favorite: Favorite) {
        realm.write {
            val queriedFavorite =
                query<Favorite>(query = "saveName == $0", favorite.saveName).first().find()
            queriedFavorite?.saveName = favorite.saveName
        }
    }

    override suspend fun deleteFavorite(id: ObjectId) {
        realm.write {
            val queriedFavorite = query<Favorite>(query = "id == $0", id).first().find()
            queriedFavorite?.let { delete(it) }
        }
    }

    override fun filterByName(key: String): LiveData<List<Favorite>> {
        val queriedFavorites = realm.query<Favorite>("name CONTAINS[c] $0", key)
            .asFlow().map {
                it.list.toList()
            }.asLiveData()

        return queriedFavorites
    }
}