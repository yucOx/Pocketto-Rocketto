package com.yucox.pockettorocketto.Repository

interface IFirebaseRepository {
    suspend fun checkAppVersion(): Boolean
}