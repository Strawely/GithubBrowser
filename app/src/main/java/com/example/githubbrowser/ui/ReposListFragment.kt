package com.example.githubbrowser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubbrowser.viewmodel.ReposListViewModel
import com.example.githubbrowser.model.RepositoryDto
import com.example.githubbrowser.databinding.FragmentReposListBinding
import javax.inject.Inject

class ReposListFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, factory)[ReposListViewModel::class.java]
    }

    private val adapter by lazy { ReposListAdapter(viewModel::onItemClick) }
    private lateinit var binding: FragmentReposListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).appComponent.inject(this)
        binding = FragmentReposListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        viewModel.reposDataSource.observe(viewLifecycleOwner, Observer(::render))
        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer(::renderError))
    }

    private fun render(data: PagedList<RepositoryDto>) {
        adapter.submitList(data)
    }

    private fun renderError(isError: Boolean) {
        binding.recycler.isVisible = !isError
        binding.textError.isVisible = isError
    }

    companion object {
        fun newInstance() = ReposListFragment()
    }
}