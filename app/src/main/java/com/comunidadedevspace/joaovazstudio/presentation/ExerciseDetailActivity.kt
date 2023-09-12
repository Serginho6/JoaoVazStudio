package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.authentication.AuthenticationManager.getCurrentUserId
import com.comunidadedevspace.joaovazstudio.data.Task
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

class ExerciseDetailActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST_CODE = 123
    private val CROP_IMAGE_REQUEST_CODE = 124

    private var selectedImageBitmap: Bitmap? = null
    private lateinit var selectedImageUri: Uri

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

        // Recuperar task
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        val imgTaskSelector = findViewById<ImageButton>(R.id.img_task_selector)
        val btnRemoveImage = findViewById<ImageButton>(R.id.btn_remove_image)

        btnDone = findViewById<Button>(R.id.btn_done)

        if(task != null) {
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }

//        // Verifica se a tarefa tem uma imagem armazenada e a exibe no ImageButton
        if (task != null && task?.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(task?.image, 0, task?.image!!.size)
            imgTaskSelector.setImageBitmap(bitmap)
            selectedImageBitmap = bitmap

            btnRemoveImage.setOnClickListener {
                // Remove a imagem selecionada e defina o ImageView para o placeholder
                imgTaskSelector.setImageResource(R.drawable.no_img_placeholder)
                selectedImageBitmap = null
                selectedImageUri = Uri.EMPTY
            }
        }

        btnDone.setOnClickListener{
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty()){
                if(task == null) {
                    addOrUpdateTask(0, title, desc, ActionType.CREATE)
                }else{
                    addOrUpdateTask(task!!.id, title, desc, ActionType.UPDATE)
                }
            }else{
                showMessage(it, "É necessário adicionar Exercício e Quantidade")
            }
        }

        imgTaskSelector.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data ?: return

            // Inicie a atividade de recorte
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(selectedImageUri, "image/*")

            // Configurar proporção de aspecto e outras configurações de corte, se necessário.
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputX", 256)
            cropIntent.putExtra("outputY", 256)
            cropIntent.putExtra("scale", true)
            cropIntent.putExtra("return-data", true)

            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE)
        } else if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val extras = data.extras
            if (extras != null) {
                val croppedBitmap = extras.getParcelable<Bitmap>("data")

                val imgTaskSelector = findViewById<ImageButton>(R.id.img_task_selector)
                imgTaskSelector.setImageBitmap(croppedBitmap)
                selectedImageBitmap = croppedBitmap // Armazena a imagem selecionada
            }
        }
    }

    private fun addOrUpdateTask(
        id: Int,
        title:String,
        description:String,
        actionType: ActionType
    ){
        val imageByteArray = selectedImageBitmap?.let { bitmap ->
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }

        val userId = getCurrentUserId()

        val task = Task(id, userId, title, description, image = imageByteArray, isSelected = false)
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

    val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"

}