package com.dypcet.g1.batterysaversystem.datasource

import android.annotation.SuppressLint
import android.app.Application
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.dypcet.g1.batterysaversystem.applist.AppListFilterType
import com.dypcet.g1.batterysaversystem.models.InstalledApp

class DataSource(val application: Application) {

    private val TAG = DataSource::class.java.simpleName

    @SuppressLint("QueryPermissionsNeeded")
    private val packageList =
        application.packageManager?.getInstalledPackages(PackageManager.GET_PERMISSIONS)

    private val usm =
        application.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    companion object {
        @Volatile
        private var INSTANCE: DataSource? = null

        fun getInstance(application: Application): DataSource {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = DataSource(application)
                    INSTANCE = instance
                }

                return instance
            }
        }
    }


    fun getApps(flag: AppListFilterType): List<InstalledApp> {
        val appList = ArrayList<InstalledApp>()
        return when (flag) {
            AppListFilterType.ALL_APPS -> {
                appList.clear()
                for (pkg in packageList!!) {
                    appList.add(
                        getInstalledAppFromPkg(pkg)
                    )
                }
                appList
            }
            AppListFilterType.USER_APPS_ONLY -> {
                appList.clear()
                for (pkg in packageList!!) {
                    if (pkg.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) == 0) {
                        appList.add(
                            getInstalledAppFromPkg(pkg)
                        )
                    }
                }
                appList
            }
            AppListFilterType.SYSTEM_APPS_ONLY -> {
                appList.clear()
                for (pkg in packageList!!) {
                    if (pkg.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) != 0) {
                        appList.add(
                            getInstalledAppFromPkg(pkg)
                        )
                    }
                }
                appList
            }
            AppListFilterType.ACTIVE_APPS_ONLY -> {
                appList.clear()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (pkg in packageList!!) {
                        if (!usm.isAppInactive(pkg.packageName)) {
                            appList.add(
                                getInstalledAppFromPkg(pkg)
                            )
                        }
                    }
                }
                appList
            }
        }
    }

    private fun getInstalledAppFromPkg(pkg: PackageInfo): InstalledApp {
        return InstalledApp(
            name = application.packageManager?.let {
                pkg.applicationInfo.loadLabel(it).toString()
            },
            packageName = pkg.packageName,
            version = pkg.versionName,
            icon = pkg.applicationInfo.loadIcon(application.packageManager)
        )
    }

    fun getAppWithDetails(packageName: String): InstalledApp {

        val packageInfo = application.packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        )
        val app = getInstalledAppFromPkg(packageInfo)
        try {

//            val regex = "[A-Z_]+\$".toRegex()
            val grantedPermissions = ArrayList<String>()
            for (i in packageInfo.requestedPermissionsFlags.indices) {
                if (packageInfo.requestedPermissionsFlags[i].and(PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    grantedPermissions.add(packageInfo.requestedPermissions[i])
                }
            }
            app.permissions = grantedPermissions

            val serviceList = ArrayList<String>()
            application.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SERVICES
            ).services.forEach {
                serviceList.add(it.name)
            }

            app.services = serviceList
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Exception Occurred while fetching app detail info." + e.printStackTrace()
                    .toString()
            )
        }

        return app
    }
}