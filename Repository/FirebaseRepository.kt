package com.yucox.pockettorocketto.Repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.yucox.pockettorocketto.Util.Version
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository(private val _database: FirebaseDatabase) : IFirebaseRepository {
    override suspend fun checkAppVersion(): Boolean {
        val refVersion = _database.getReference("version")
        return suspendCoroutine { Continuation ->
            refVersion.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val versionNumber = snapshot.getValue<String>()
                    if (versionNumber.equals(Version.version)) {
                        Continuation.resume(true)
                    } else {
                        Continuation.resume(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Continuation.resume(false)
                }
            })
        }
    }
}