package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val devLinkImageView = findViewById<ImageView>(R.id.dev_link_image)

        devLinkImageView.setOnClickListener {
            val url = getString(R.string.dev_linktree)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}
