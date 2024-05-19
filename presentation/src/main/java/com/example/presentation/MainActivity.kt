package com.example.presentation

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private lateinit var conf: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        conf = AppBarConfiguration(
            setOf(
                R.id.mainHolderFragment,
                R.id.infoFragment
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, conf)
        binding.navView.setupWithNavController(navController)

        loadIcon()
    }

    private fun loadIcon() {
        val userIcon = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.userIcon)
        Glide.with(this)
            .load("https://www.boredpanda.com/blog/wp-content/uploads/2015/06/pallas-cat-manul-10__880.jpg")
            .placeholder(R.drawable.user_icon_placeholder)
            .error(R.drawable.user_icon_error)
            .override(resources.getDimensionPixelSize(R.dimen.headerIconSize))
            .centerCrop()
            .transform(CircleCrop())
            .into(userIcon)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(conf) || super.onSupportNavigateUp()
    }

}