package com.dypcet.g1.batterysaversystem.appdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dypcet.g1.batterysaversystem.R
import com.dypcet.g1.batterysaversystem.databinding.FragmentAppDetailBinding

class AppDetailFragment : Fragment() {

    private lateinit var viewBinding: FragmentAppDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_app_detail, container, false)

        viewBinding.applistButton.setOnClickListener {
            this.findNavController()
                .navigate(AppDetailFragmentDirections.actionAppDetailFragmentToAppListFragment())
        }

        return viewBinding.root
    }

}