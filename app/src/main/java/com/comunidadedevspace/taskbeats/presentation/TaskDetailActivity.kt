package com.comunidadedevspace.taskbeats.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {

    private var task: Task? = null
    private lateinit var btnDone: Button

    companion object{
        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent{
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, task)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Recuperar task
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        btnDone = findViewById<Button>(R.id.btn_done)

        if(task != null) {
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }

        btnDone.setOnClickListener{
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty()){
                if(task == null) {
                    addOrUpdateTask(0, title, desc, MainActivity.ActionType.CREATE)
                }else{
                    addOrUpdateTask(task!!.id, title, desc, MainActivity.ActionType.UPDATE)
                }
            }else{
                showMessage(it, "Fields are required")
            }
        }

        // Recuperar campo do XML
       // tvTitle = findViewById(R.id.tv_task_title_detail)

        // Setar um novo texto na tela
       // tvTitle.text = task?.title ?: "Adicione uma tarefa"
    }

    private fun addOrUpdateTask(
        id: Int,
        title:String,
        description:String,
        actionType: MainActivity.ActionType
    ){
        val task = Task(id, title, description)
        returnAction(task, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                if(task != null){
                    returnAction(task!!, MainActivity.ActionType.DELETE)
                }else{
                    showMessage(btnDone, "Item not found")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun returnAction(task: Task, actionType: MainActivity.ActionType){
        val intent = Intent()
            .apply {
                val taskAction = MainActivity.TaskAction(task, actionType.name)
                putExtra(TASK_ACTION_RESULT, taskAction)
            }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"

}