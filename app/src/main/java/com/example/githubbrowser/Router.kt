package com.example.githubbrowser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.githubbrowser.ui.RepoInfoFragment
import com.example.githubbrowser.ui.ReposListFragment
import javax.inject.Inject
import javax.inject.Named

class Router @Inject constructor(
    @Named("CONTAINER_ID") private val containerId: Int,
    private val fragmentManager: FragmentManager
) {
    fun navigateReposList() {
        val fragment = ReposListFragment.newInstance()
        navigate(fragment)
    }

    fun navigateRepoInfo(navigateIntent: RepoNavigateIntent) {
        val fragment = RepoInfoFragment.newInstance(navigateIntent)
        navigate(fragment, true)
    }

    private fun navigate(fragment: Fragment, addToBackStack: Boolean = false) {
        fragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            if (addToBackStack) addToBackStack(fragment.javaClass.simpleName)
            commit()
        }
    }
}

data class RepoNavigateIntent(
    val avatarUrl: String,
    val login: String,
    val name: String,
    val commitUrl: String
)