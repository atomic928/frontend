package com.example.hackathon2022.ui.home


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hackathon2022.AlertDialogFragment
import com.example.hackathon2022.databinding.FragmentHomeBinding

class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by activityViewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textX: TextView = binding.textX
        val textY: TextView = binding.textY
        val textZ: TextView = binding.textZ

        homeViewModel.acceleration.observe(viewLifecycleOwner) {
            textX.text = it[0].toString()
            textY.text = it[1].toString()
            textZ.text = it[2].toString()
            if (it[0] < 1) {
                homeViewModel.putBackgroundColor(Color.BLACK)
            } else if (it[0] < 4) {
                homeViewModel.putBackgroundColor(Color.BLUE)
            } else {
                val dialog = AlertDialogFragment()
                dialog.show(childFragmentManager, "sample")
                homeViewModel.putBackgroundColor(Color.RED)
            }
        }

        homeViewModel.backgroundColor.observe(viewLifecycleOwner) {
            binding.root.setBackgroundColor(it)
        }

        var textSpeed: TextView = binding.textSpeed

        homeViewModel.speed.observe(viewLifecycleOwner) {
            textSpeed.text = it.toString()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
