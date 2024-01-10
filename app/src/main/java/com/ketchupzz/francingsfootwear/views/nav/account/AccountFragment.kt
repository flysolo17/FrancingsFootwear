package com.ketchupzz.francingsfootwear.views.nav.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentAccountBinding
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.getImageTypeFromUri
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel


class AccountFragment : Fragment() {

    private lateinit var binding : FragmentAccountBinding
    private val authViewModel : AuthViewModel by activityViewModels()

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private  var customer : Customer? = null
    private lateinit var loadingDialog: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut().also {
                activity?.finish()
            }
        }

        binding.circularImageView.setOnClickListener {
            if (customer != null) {
                pickImageFromGallery()
            }  else {
                Toast.makeText(view.context,"No User Found",Toast.LENGTH_SHORT).show()
            }
        }
        observers()
        binding.buttonEditProfile.setOnClickListener {
            if (customer != null) {
                val directions = AccountFragmentDirections.actionNavigationAccountToUpdateAccount(
                    customer!!
                )
                findNavController().navigate(directions)
            }
        }

        binding.buttonChangePassword.setOnClickListener {
            if (customer != null) {
                val directions = AccountFragmentDirections.actionNavigationAccountToChangePasswordFragment(
                    customer!!
                )
                findNavController().navigate(directions)
            }
        }
        binding.buttonAddresses.setOnClickListener {
            if (customer != null) {
                val directions = AccountFragmentDirections.actionNavigationAccountToAddressesFragment(
                    customer!!
                )
                findNavController().navigate(directions)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    uploadImage(selectedImageUri)
                } else {
                    Toast.makeText(binding.root.context,"Unknown error",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }


    private fun uploadImage(uri: Uri) {
        val type =    binding.root.context.getImageTypeFromUri(uri).toString()
        authViewModel.changeProfile(customer?.id ?: "",uri, type) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Uploading profile....")

                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()

                    Toast.makeText(binding.root.context,it.data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun observers() {
        authViewModel.customer.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting User Profile")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    customer = it.data
                    binding.textFullname.text = customer?.name ?: "unknown user"
                    binding.textEmail.text = customer?.email ?: "unknown user"
                    Glide.with(binding.root.context)
                        .load(it.data.profile)
                        .error(R.drawable.profiles)
                        .into(binding.circularImageView)
                }
            }
        }
    }
}