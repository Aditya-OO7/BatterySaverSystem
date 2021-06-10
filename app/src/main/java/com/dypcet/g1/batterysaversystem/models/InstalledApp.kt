package com.dypcet.g1.batterysaversystem.models

import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import com.dypcet.g1.batterysaversystem.MainActivity
import com.dypcet.g1.batterysaversystem.R

data class InstalledApp(
    var name: String? = "-",
    var version: String? = "-1",
    var icon: Drawable? = ActivityCompat.getDrawable(MainActivity().applicationContext, R.drawable.ic_launcher_background)
)