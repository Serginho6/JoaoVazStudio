package com.comunidadedevspace.joaovazstudio.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.presentation.fragments.ExerciseListSelectedFragment
import com.comunidadedevspace.joaovazstudio.presentation.fragments.MoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var currentTrainId: Int = -1
    private lateinit var moreFragment: MoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUserId = intent.getLongExtra("currentUserId", -1L)
        currentTrainId = intent.getIntExtra("currentTrainId", -1)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        val exerciseListSelectedFragment = ExerciseListSelectedFragment.newInstance(currentTrainId, currentUserId)

        moreFragment = MoreFragment.newInstance()
        moreFragment.setCurrentUserId(currentUserId)

        supportFragmentManager.commit {
            replace(R.id.fragment_container_view, exerciseListSelectedFragment)
            setReorderingAllowed(true)
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragment = when(menuItem.itemId){
                R.id.train -> exerciseListSelectedFragment
                R.id.more -> moreFragment
                else -> exerciseListSelectedFragment
            }

            supportFragmentManager.commit {
                replace(R.id.fragment_container_view, fragment)
                setReorderingAllowed(true)
            }

            true

        }
    }
}