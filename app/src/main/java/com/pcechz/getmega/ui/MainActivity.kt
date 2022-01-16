package com.pcechz.getmega.ui

import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.pcechz.getmega.R
import com.pcechz.getmega.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

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