package com.ketchupzz.francingsfootwear.views.nav.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentMessagesBinding
import com.ketchupzz.francingsfootwear.utils.LoadingDialog


class MessagesFragment : Fragment() {

    private lateinit var binding : FragmentMessagesBinding
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}