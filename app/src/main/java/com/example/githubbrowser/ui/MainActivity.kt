package com.example.githubbrowser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubbrowser.R
import com.example.githubbrowser.Router

class MainActivity : AppCompatActivity() {
    val router by lazy { Router(R.id.container, supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        router.navigateReposList()
    }
}