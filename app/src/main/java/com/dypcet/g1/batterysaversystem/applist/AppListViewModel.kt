package com.dypcet.g1.batterysaversystem.applist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dypcet.g1.batterysaversystem.datasource.DataSource
import com.dypcet.g1.batterysaversystem.models.InstalledApp
import kotlinx.coroutines.launch

class AppListViewModel(
    private val dataSource: DataSource,
    application: Application
) : AndroidViewModel(application) {

    var apps = MutableLiveData<List<InstalledApp>>()

    init {
        initializeAppList()
    }

    private fun initializeAppList() {
        viewModelScope.launch {
            apps.value = dataSource.getApps(AppListFilterType.ALL_APPS)
        }
    }
}