package com.comunidadedevspace.joaovazstudio.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.comunidadedevspace.joaovazstudio.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        val floatActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

//        floatActionButton.setOnClickListener {
//            selectTrainFile()
//        }

        val exerciseListFragment = ExerciseListFragment.newInstance()
        val moreFragment = MoreFragment.newInstance()

        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, exerciseListFragment)
            setReorderingAllowed(true)
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragment = when(menuItem.itemId){
                R.id.train -> exerciseListFragment
                R.id.more -> moreFragment
                else -> exerciseListFragment
            }

            supportFragmentManager.commit {
                replace(R.id.fragment_container_view, fragment)
                setReorderingAllowed(true)
            }

            true

        }
    }
}