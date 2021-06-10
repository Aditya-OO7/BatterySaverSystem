package com.dypcet.g1.batterysaversystem.datasource

import android.annotation.SuppressLint
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.dypcet.g1.batterysaversystem.applist.AppListFilterType
import com.dypcet.g1.batterysaversystem.models.InstalledApp
import java.util.concurrent.TimeUnit

class DataSource(val applicationContext: Context) {

    @SuppressLint("QueryPermissionsNeeded")
    private val packageList =
        applicationContext.packageManager?.getInstalledPackages(PackageManager.GET_PERMISSIONS)

    private val usm =
        applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    companion object {
        @Volatile
        private var INSTANCE: DataSource? = null

        fun getInstance(applicationContext: Context): DataSource {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = DataSource(applicationContext)
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
                    if (pkg.applicationInfo.flags.and(PackageManager.MATCH_SYSTEM_ONLY) == 0) {
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
                    if (pkg.applicationInfo.flags.and(PackageManager.MATCH_SYSTEM_ONLY) != 0) {
                        appList.add(
                            getInstalledAppFromPkg(pkg)
                        )
                    }
                }
                appList
            }
            AppListFilterType.APPS_USED_LAST_10MIN -> {
                appList.clear()
                val usageEvents = mutableListOf<UsageStats>()

                val endTime = System.currentTimeMillis()
                val beginTime = endTime - 6_00_000L
                usageEvents.clear()

                try {
                    usm.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY,
                        beginTime,
                        endTime
                    ).apply {
                        usageEvents.addAll(this.sortedBy { it.lastTimeUsed })
                    }
                } catch (ignored: Exception) {
                }

                val lastTenMin =
                    usageEvents.filter { endTime - it.lastTimeUsed < 600.toMillisFromSeconds() }
                for (pkg in packageList!!) {
                    for (stat in lastTenMin) {
                        if (pkg.packageName == stat.packageName) {
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
            name = applicationContext.packageManager?.let {
                pkg.applicationInfo.loadLabel(it).toString()
            },
            version = pkg.versionName,
            icon = pkg.applicationInfo.loadIcon(applicationContext.packageManager)
        )
    }

    private fun Int.toMillisFromSeconds(): Long = TimeUnit.SECONDS.toMillis(this.toLong())
}