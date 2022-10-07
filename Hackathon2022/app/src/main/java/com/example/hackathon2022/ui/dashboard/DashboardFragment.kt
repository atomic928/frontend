package com.example.hackathon2022.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hackathon2022.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(){

    private var _binding: FragmentDashboardBinding? = null
    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val ivMap: ImageView = binding.ivMap

        dashboardViewModel.map.observe(viewLifecycleOwner) {
            ivMap.setImageBitmap(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}