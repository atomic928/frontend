package com.example.hackathon2022.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.hackathon2022.SensorViewModel
import com.example.hackathon2022.databinding.FragmentMapBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val viewModel: SensorViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView
    private val binding get() = _binding!!
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setFragmentResultListener("stringData") { key, bundle ->
            val result = bundle.getString("itemString")
            coroutineScope.launch {
                val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                    result?.let { getOriginalBitmap(it) }
                }

                val originalBitmap = originalDeferred.await()
                val ivMap: ImageView = binding.ivMap
                ivMap.setImageBitmap(originalBitmap)
            }
        }



        return root
    }

    private fun getOriginalBitmap(URL: String): Bitmap =
        URL(URL).openStream().use {
            BitmapFactory.decodeStream(it)
        }

}