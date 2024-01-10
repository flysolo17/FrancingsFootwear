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
import com.ketchupzz.francingsfootwear.databinding.FragmentCreateAddressBinding
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.models.customer.Contacts
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel


class CreateAddressFragment : Fragment() {

    private lateinit var binding : FragmentCreateAddressBinding
    private lateinit var loadingDialog: LoadingDialog
    private val authViewModel by activityViewModels<AuthViewModel>()
    private val args by navArgs<CreateAddressFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAddressBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSaveAddress.setOnClickListener {
            val  address = binding.inputAddress.text.toString()
            val fullname = binding.inputFullname.text.toString()
            val phone = binding.inputPhone.text.toString()
            val isCheck = binding.checkboxDefault.isChecked
            if (address.isEmpty()) {
                binding.layoutAddress.error ="Enter address"
                return@setOnClickListener
            }
            if (fullname.isEmpty()) {
                binding.layoutFullname.error ="Enter fullname"
                return@setOnClickListener
            }
            if (phone.isEmpty() && phone.length != 10 && !phone.startsWith("9")) {
                binding.inputPhoneLayout.error = "invalid phone"
                return@setOnClickListener
            }
            val addresses = Addresses(
                name = address,
                Contacts(
                    name = fullname,
                    phone = "+63${phone}"
                ),
                default = isCheck
            )
            saveAddress(args.customer.id!!,addresses)
        }
    }

    private fun saveAddress(uid: String, addresses: Addresses) {
        if (addresses.default == true) {
            args.customer.addresses.forEach {
                it.default = false
            }
        }
        args.customer.addresses.add(addresses)
        authViewModel.createAddress(uid, args.customer.addresses) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Creating address..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            }
        }
    }


}