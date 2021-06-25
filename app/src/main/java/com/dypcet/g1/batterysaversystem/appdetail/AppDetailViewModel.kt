package com.dypcet.g1.batterysaversystem.appdetail

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.dypcet.g1.batterysaversystem.datasource.DataSource
import com.dypcet.g1.batterysaversystem.models.InstalledApp

class AppDetailViewModel(
    application: Application,
    dataSource: DataSource,
    packageName: String
) : AndroidViewModel(application) {

    private val TAG = AppDetailViewModel::class.java.simpleName

    private val _app = dataSource.getAppWithDetails(packageName)
    val app = _app

}