package com.dypcet.g1.batterysaversystem.appdetail

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAppDetailBinding
import com.dypcet.g1.batterysaversystem.datasource.DataSource

class AppDetailFragment : Fragment() {

    private val TAG = AppDetailFragment::class.java.simpleName

    private lateinit var viewBinding: FragmentAppDetailBinding
    private lateinit var viewModel: AppDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_app_detail, container, false
        )
        viewBinding.lifecycleOwner = this

        val args = AppDetailFragmentArgs.fromBundle(requireArguments())

        viewModel = AppDetailViewModel(
            this.requireActivity().application,
            DataSource.getInstance(requireActivity().application),
            args.packageName
        )

        viewBinding.viewModel = viewModel

        val detailList = HashMap<String, List<String>>()

        detailList["Permissions"] = viewModel.app.permissions ?: emptyList()
        detailList["Services"] = viewModel.app.services ?: emptyList()

        val titleList = arrayListOf("Permissions", "Services")

        val expandableListAdapter = MyExpandableListAdapter(titleList, detailList)
        viewBinding.expandableList.setAdapter(expandableListAdapter)

        viewModel.navigateToApp.observe(viewLifecycleOwner, {
            if (it) {
                val launchIntent = requireActivity().packageManager.getLaunchIntentForPackage(args.packageName)
                startActivity(launchIntent)
            }
        })

        viewModel.navigateToAppSettings.observe(viewLifecycleOwner, {
            if (it) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", args.packageName, null)
                startActivity(intent)
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val action = AppDetailFragmentDirections.actionAppDetailFragmentToAppListFragment()
            findNavController().navigate(action)
        }

        return viewBinding.root
    }
}