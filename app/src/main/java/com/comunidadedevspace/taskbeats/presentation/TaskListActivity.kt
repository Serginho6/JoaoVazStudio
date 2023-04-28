package com.comunidadedevspace.taskbeats.presentation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.AppDataBase
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var ctnContent: LinearLayout

    //Adapter
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter(::onListItemClicked)
    }

    lateinit var dataBase : AppDataBase

    private val dao by lazy {
        dataBase.taskDao()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
            val task: Task = taskAction.task

            when (taskAction.actionType) {
                ActionType.DELETE.name -> deleteById(task.id)
                ActionType.CREATE.name -> insertIntoDataBase(task)
                ActionType.UPDATE.name -> updateIntoDataBase(task)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        ctnContent = findViewById(R.id.ctn_content)

        //RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail(null)
        }
    }

    override fun onStart() {
        super.onStart()

        dataBase = (application as TaskBeatsApplication).dataBase

        Log.d("SérgioTeste", dataBase.toString())
        listFromDataBase()
    }

    // CRUD = Create, Read, Update and Delete

    //CREATE
    private fun insertIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDataBase()
        }
    }

    //READ
    private fun listFromDataBase() {
        CoroutineScope(IO).launch {
            val myDataBaseList: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseList)

            CoroutineScope(Main).launch {
                if (myDataBaseList.isEmpty()) {
                    ctnContent.visibility = View.VISIBLE
                } else {
                    ctnContent.visibility = View.GONE
                }
            }
        }
    }

    //UPDATE
    private fun updateIntoDataBase(task: Task) {
        CoroutineScope(IO).launch {
            dao.update(task)
            listFromDataBase()
        }
    }

    //DELETE ALL
    private fun deleteAll() {
        CoroutineScope(IO).launch {
            dao.deleteAll()
            listFromDataBase()
        }
    }

    //DELETE BY ID
    private fun deleteById(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listFromDataBase()
        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)
    }

    private fun openTaskListDetail(task: Task?) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_task -> {
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // CRUD - Create, Read, Update e Delete
    enum class ActionType {
        DELETE,
        UPDATE,
        CREATE,
    }

    data class TaskAction(
        val task: Task,
        val actionType: String
    ) : Serializable

}