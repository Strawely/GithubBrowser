package com.example.githubbrowser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubbrowser.R
import com.example.githubbrowser.Router
import com.example.githubbrowser.di.AppComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    val appComponent by lazy {
        AppComponent.createComponent(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appComponent.inject(this)
        router.navigateReposList()
    }
}