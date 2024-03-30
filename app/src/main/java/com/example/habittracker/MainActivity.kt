package com.example.habittracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.habittracker.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val drawerLayout: DrawerLayout = binding.drawerLayout//findViewById(R.id.drawer_layout)
        val navView: NavigationView = binding.navView//findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)

        //Todo nav controller back button
        //https://developer.android.com/guide/navigation/integrations/ui#appbarconfiguration
        //AppBarConfiguration

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainHolderFragment -> navController.navigate(R.id.action_infoFragment_to_mainHolderFragment)
            R.id.infoFragment -> navController.navigate(R.id.action_mainHolderFragment_to_infoFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
