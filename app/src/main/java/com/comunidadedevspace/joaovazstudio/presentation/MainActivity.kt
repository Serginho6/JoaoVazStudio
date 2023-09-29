package com.comunidadedevspace.joaovazstudio.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.comunidadedevspace.joaovazstudio.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var moreFragment: MoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        val trainSelectedFragment = TrainSelectedFragment.newInstance()

        val currentUserId = intent.getLongExtra("currentUserId", -1L)

        moreFragment = MoreFragment.newInstance()
        moreFragment.setCurrentUserId(currentUserId)

        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, trainSelectedFragment)
            setReorderingAllowed(true)
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragment = when(menuItem.itemId){
                R.id.train -> trainSelectedFragment
                R.id.more -> moreFragment
                else -> trainSelectedFragment
            }

            supportFragmentManager.commit {
                replace(R.id.fragment_container_view, fragment)
                setReorderingAllowed(true)
            }

            true

        }
    }
}