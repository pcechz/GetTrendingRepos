package com.pcechz.getmega.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.pcechz.getmega.R
import com.pcechz.getmega.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = sqrt((xInches * xInches + yInches * yInches).toDouble())
        requestedOrientation = if (diagonalInches >= 6.5) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }








        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, HomeFragment.newInstance(), HomeFragment.TAG)
                .commitNow()
        }
        binding.menuToolbar.setOnClickListener {
            showMenu()
        }

    }



    private fun showMenu() {
        val popupMenu = PopupMenu(this, binding.menuToolbar)
        popupMenu.inflate(R.menu.menu_main)
        popupMenu.setOnMenuItemClickListener {
            val fragment =
                supportFragmentManager.findFragmentByTag(HomeFragment.TAG) as HomeFragment
            when (it.itemId) {
                R.id.menu_name -> fragment?.sortByNames()
                //R.id.action_stars -> fragment?.sortByStars()
            }
            true
        }
        // show popup
        popupMenu.show()
    }


}