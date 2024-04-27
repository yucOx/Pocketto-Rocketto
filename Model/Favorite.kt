package com.yucox.pockettorocketto.Model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Favorite : RealmObject{
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var siteName: String? = null
    var saveName : String? = null
    var url : String? = null
    //var urls: RealmList<String> = realmListOf()
    var timestamp : RealmInstant = RealmInstant.now()
}

