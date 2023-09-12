package com.comunidadedevspace.taskbeats.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.comunidadedevspace.taskbeats.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val floatActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        floatActionButton.setOnClickListener {
            openTaskListDetail()
        }

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

    private fun openTaskListDetail() {
        val intent = ExerciseDetailActivity.start(this, null)
        startActivity(intent)
    }
}