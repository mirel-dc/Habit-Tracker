package com.example.habittracker

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    CallbackListener {

    private lateinit var drawerLayout: DrawerLayout

    private var callback: CallbackListener? = null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)

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
        callback?.onCallback()
        when (item.itemId) {
            R.id.mainHolderFragment -> navController.navigate(R.id.action_infoFragment_to_mainHolderFragment)
            R.id.infoFragment -> navController.navigate(R.id.action_mainHolderFragment_to_infoFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun setCallback(callback: CallbackListener) {
        this.callback = callback
    }

    override fun onCallback() {
        Log.d(TAG, "onCallback")
        callback?.onCallback()
    }
}

//Колбек посылается из Фрагмента Создания в Активити
//После этого, Активити посылает колбек в фрагмент со списком, чтобы обновить данные

//Пробовал посылать колбек напрямую из фрагмента во фрагмент - не получилось
//Либо не мог присвоить колбек, т.к. единственный общий родитель - активити,
//Либо не мог найти сам фрагмент в другом, т.к. он создаётся во ViewPager

//Как я понял, легко чинится с viewModel, но в фрагментах оставил костыль так
interface CallbackListener {
    fun onCallback()
}
