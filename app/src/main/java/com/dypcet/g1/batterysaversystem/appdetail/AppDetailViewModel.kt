package com.dypcet.g1.batterysaversystem.appdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dypcet.g1.batterysaversystem.datasource.DataSource

class AppDetailViewModel(
    application: Application,
    dataSource: DataSource,
    packageName: String
) : AndroidViewModel(application) {

    private val _app = dataSource.getAppWithDetails(packageName)
    val app = _app

    private val _navigateToApp = MutableLiveData<Boolean>()
    val navigateToApp = _navigateToApp

    private val _navigateToAppSettings = MutableLiveData<Boolean>()
    val navigateToAppSettings = _navigateToAppSettings

    fun openApp() {
        _navigateToApp.value = true
    }

    fun closeApp() {
        _navigateToAppSettings.value = true
    }
}