package com.dypcet.g1.batterysaversystem.applist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    private val _navigateToAppDetail = MutableLiveData<String?>()
    val navigateToAppDetail
        get() = _navigateToAppDetail

    init {
        setFiltering(AppListFilterType.ALL_APPS)
    }

    fun setFiltering(requestType: AppListFilterType) {
        apps.value = dataSource.getApps(requestType)
    }

    fun onAppClicked(packageName: String) {
        _navigateToAppDetail.value = packageName
    }

    fun onAppDetailNavigated() {
        _navigateToAppDetail.value = null
    }
}