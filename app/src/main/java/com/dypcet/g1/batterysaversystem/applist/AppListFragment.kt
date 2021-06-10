package com.dypcet.g1.batterysaversystem.applist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAppListBinding
import com.dypcet.g1.batterysaversystem.datasource.DataSource

class AppListFragment : Fragment() {

    private lateinit var viewBinding: FragmentAppListBinding
    private lateinit var adapter: AppListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_app_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = DataSource(application.applicationContext)

        val viewModel = AppListViewModel(dataSource, application)

        adapter = AppListAdapter()
        viewBinding.appListView.adapter = adapter

        viewModel.apps.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

//        viewBinding.detailButton.setOnClickListener {
//            this.findNavController().navigate(AppListFragmentDirections.actionAppListFragmentToAppDetailFragment())
//        }

        return viewBinding.root
    }
}