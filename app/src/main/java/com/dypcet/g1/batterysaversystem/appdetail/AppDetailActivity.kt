package com.dypcet.g1.batterysaversystem.appdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ActivityNavigator
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.dypcet.g1.batterysaversystem.MainActivity
import com.dypcet.g1.batterysaversystem.R

class AppDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)
        findNavController(R.id.app_detail_host_fragment).apply {
            setGraph(
                R.navigation.app_detail_nav,
                intent.extras
            )
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        findNavController(R.id.app_detail_host_fragment).apply {
            navigate(AppDetailFragmentDirections.actionAppDetailFragment2ToMainActivity())
        }
        return true
    }
}