package com.example.githubbrowser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubbrowser.Router
import com.example.githubbrowser.viewmodel.ReposListViewModel
import com.example.githubbrowser.model.RepositoryDto
import com.example.githubbrowser.databinding.FragmentReposListBinding

class ReposListFragment : Fragment() {

    private val viewModel by lazy { ReposListViewModel((activity as MainActivity).router) }
    private val adapter by lazy { ReposListAdapter(viewModel::onItemClick) }
    private lateinit var binding: FragmentReposListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReposListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.layoutManager = LinearLayoutManager(requireContext())
        binding.root.adapter = adapter
        viewModel.reposDataSource.observe(viewLifecycleOwner, Observer(::render))
    }

    private fun render(data: PagedList<RepositoryDto>) {
        adapter.submitList(data)
    }

    companion object {
        fun newInstance() = ReposListFragment()
    }
}