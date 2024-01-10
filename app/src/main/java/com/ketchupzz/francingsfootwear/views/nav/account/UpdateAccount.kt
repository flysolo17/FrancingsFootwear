package com.ketchupzz.francingsfootwear.views.nav.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentUpdateAccountBinding
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel


class UpdateAccount : Fragment() {
    private lateinit var binding : FragmentUpdateAccountBinding
    private lateinit var loadingDialog: LoadingDialog
    private val args by navArgs<UpdateAccountArgs>()
    private val authViewModel by activityViewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateAccountBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        binding.inputFullname.setText(args.customer.name)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.buttonSave.setOnClickListener {
            val name = binding.inputFullname.text.toString()
           if (name.isEmpty()) {
               binding.layoutFullname.error ="Please enter name"
               return@setOnClickListener
           }
           updateAccount(args.customer.id ?: "",name)
       }
    }

    private fun updateAccount(uid: String, name: String) {
        authViewModel.editProfile(uid,name) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Updating Account...")
                }
                is UiState.SUCCESS ->{
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }


}