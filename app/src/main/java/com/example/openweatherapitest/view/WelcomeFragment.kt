package com.example.openweatherapitest.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.openweatherapitest.MainActivity
import com.example.openweatherapitest.R
import com.example.openweatherapitest.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var bindings: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentWelcomeBinding.inflate(layoutInflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings.progressbarButton.setButtonMode(getString(R.string.start)) {
            bindings.progressbarButton.setButtonIndeterminateMode()
            Handler(Looper.getMainLooper()).postDelayed(
                { (requireActivity() as MainActivity).goToMeteoFragment() },
                1000
            )

        }
    }
}