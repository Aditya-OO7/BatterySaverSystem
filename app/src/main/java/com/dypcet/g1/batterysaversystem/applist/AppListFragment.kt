package com.dypcet.g1.batterysaversystem.applist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAppListBinding
import com.dypcet.g1.batterysaversystem.datasource.DataSource

class AppListFragment : Fragment() {

    private lateinit var viewBinding: FragmentAppListBinding
    private lateinit var viewModel: AppListViewModel
    private lateinit var adapter: AppListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_app_list, container, false)

        val application = requireActivity().application

        val dataSource = DataSource.getInstance(application)

        viewModel = AppListViewModel(dataSource, application)

        adapter = AppListAdapter(AppListener { app ->
            Toast.makeText(context, app.name, Toast.LENGTH_LONG).show()
            viewModel.onAppClicked(app.packageName!!)
        })
        viewBinding.appListView.adapter = adapter

        viewModel.apps.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.navigateToAppDetail.observe(viewLifecycleOwner, { packageName ->
            packageName?.let {
                this.findNavController().navigate(
                    AppListFragmentDirections.actionAppListFragmentToAppDetailActivity(packageName)
                )
                viewModel.onAppDetailNavigated()
            }
        })

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        setHasOptionsMenu(false)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter_all -> {
            Toast.makeText(context, "Filter All", Toast.LENGTH_LONG).show()
            viewModel.setFiltering(AppListFilterType.ALL_APPS)
            true
        }
        R.id.filter_userapps_only -> {
            Toast.makeText(context, "Filter User Apps", Toast.LENGTH_LONG).show()
            viewModel.setFiltering(AppListFilterType.USER_APPS_ONLY)
            true
        }
        R.id.filter_system_only -> {
            Toast.makeText(context, "Filter System Apps", Toast.LENGTH_LONG).show()
            viewModel.setFiltering(AppListFilterType.SYSTEM_APPS_ONLY)
            true
        }
        R.id.filter_active -> {
            Toast.makeText(context, "Filter Active Apps", Toast.LENGTH_LONG).show()
            viewModel.setFiltering(AppListFilterType.ACTIVE_APPS_ONLY)
            true
        }
        else -> false
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }
}