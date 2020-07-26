package com.example.githubbrowser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubbrowser.R
import com.example.githubbrowser.RepoNavigateIntent
import com.example.githubbrowser.databinding.FragmentRepoInfoBinding
import com.example.githubbrowser.viewmodel.CommitInfo
import com.example.githubbrowser.viewmodel.RepoInfoViewModel
import javax.inject.Inject

private const val ARG_AVATAR = "avatar"
private const val ARG_LOGIN = "login"
private const val ARG_NAME = "name"
private const val ARG_COMMIT = "commit"

class RepoInfoFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, factory)[RepoInfoViewModel::class.java]
    }
    private var navigateIntent: RepoNavigateIntent? = null
    private lateinit var binding: FragmentRepoInfoBinding
    private val adapter by lazy { RepoShaAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).appComponent.inject(this)
        binding = FragmentRepoInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            navigateIntent = RepoNavigateIntent(
                it.getString(ARG_AVATAR) ?: "",
                it.getString(ARG_LOGIN) ?: "",
                it.getString(ARG_NAME) ?: "",
                it.getString(ARG_COMMIT) ?: ""
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateIntent?.let { viewModel.init(it) }
        binding.recyclerParentsSha.adapter = adapter
        viewModel.lastCommitLiveData.observe(viewLifecycleOwner, Observer(::render))
        binding.toolbar.title = navigateIntent?.name
        binding.toolbar.setNavigationOnClickListener {
            requireFragmentManager().popBackStack()
        }
        binding.textAuthorLogin.text = navigateIntent?.login
        Glide.with(requireContext())
            .load(navigateIntent?.avatarUrl)
            .into(binding.imgAvatar)
    }

    private fun render(commitInfo: CommitInfo) {
        binding.textCommitMsg.text = commitInfo.msg
        binding.textCommitAuthorName.text = commitInfo.author
        binding.textDate.text = commitInfo.date
        adapter.update(commitInfo.parents)
    }

    companion object {
        fun newInstance(navigateIntent: RepoNavigateIntent): RepoInfoFragment {
            return RepoInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_AVATAR, navigateIntent.avatarUrl)
                    putString(ARG_LOGIN, navigateIntent.login)
                    putString(ARG_NAME, navigateIntent.name)
                    putString(ARG_COMMIT, navigateIntent.commitUrl)
                }
            }
        }
    }
}