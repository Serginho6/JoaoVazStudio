package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.authentication.AuthenticationManager.getCurrentUserId
import com.comunidadedevspace.joaovazstudio.data.Task
import com.google.android.material.snackbar.Snackbar

class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var editTextVideoUrl: EditText
    private var youtubeVideoUrl: String? = null

    private var task: Task? = null
    private lateinit var btnDone: Button

    private val viewModel: ExerciseDetailViewModel by viewModels{
        ExerciseDetailViewModel.getVMFactory(application)
    }

    companion object{
        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent{
            val intent = Intent(context, ExerciseDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, task)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        editTextVideoUrl = findViewById(R.id.edt_task_video_url)

        // Recuperar task
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        val edtVideoId = findViewById<EditText>(R.id.edt_task_video_url)

        btnDone = findViewById<Button>(R.id.btn_done)

        if(task != null) {
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
            edtVideoId.setText(task!!.youtubeVideoId)
        }

        btnDone.setOnClickListener{
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()
            val videoId = edtVideoId.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty() && videoId.isNotEmpty()){
                youtubeVideoUrl = "https://www.youtube.com/watch?v=$videoId"
                if(task == null) {
                    addOrUpdateTask(0, title, desc, videoId, ActionType.CREATE)
                }else{
                    addOrUpdateTask(task!!.id, title, desc, videoId, ActionType.UPDATE)
                }
            }else{
                showMessage(it, "É necessário adicionar Exercício, Quantidade e ID do vídeo no Youtube")
            }
        }
    }

    private fun addOrUpdateTask(
        id: Int,
        title:String,
        description:String,
        videoId:String,
        actionType: ActionType
    ){
        val userId = getCurrentUserId()
        val task = Task(id, userId, title, description, videoId, isSelected = false)
        performAction(task, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_exercise_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                if(task != null){
                    performAction(task!!, ActionType.DELETE)
                }else{
                    showMessage(btnDone, "Item not found")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performAction(task: Task, actionType: ActionType){
        val taskAction = TaskAction(task, actionType.name)
        viewModel.execute(taskAction)
        finish()
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }
}