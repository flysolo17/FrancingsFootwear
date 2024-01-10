package com.ketchupzz.francingsfootwear.views.nav.shop.widgets

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
import androidx.navigation.fragment.navArgs
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentPaymentBinding
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.getImageTypeFromUri
import com.ketchupzz.francingsfootwear.utils.toPHP
import com.ketchupzz.francingsfootwear.viewmodel.TransactionViewModel


class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    private val args by navArgs<PaymentFragmentArgs>()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var imageReceipt : Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        binding.textPaymentTotal.text = args.transaction.payment.total.toPHP()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUploadReceipt.setOnClickListener {
            pickImageFromGallery()
        }
        binding.buttonSubmit.setOnClickListener {
            if (imageReceipt == null) {
                Toast.makeText(binding.root.context,"Please upload your receipt.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            transactionViewModel.transactionRepository.uploadReceipt(args.transaction.id,imageReceipt!!,args.transaction.payment,view.context.getImageTypeFromUri(imageReceipt!!) ?: ".jpg") {
                when(it) {
                    is UiState.FAILED -> {
                        loadingDialog.closeDialog()
                        Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                    }
                    is UiState.LOADING -> {
                        loadingDialog.showDialog("Uploading receipt...")
                    }
                    is UiState.SUCCESS -> {
                        loadingDialog.closeDialog()
                        Toast.makeText(view.context,it.data,Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }
                }
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
                    imageReceipt = selectedImageUri
                    binding.imageReceipt.setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(binding.root.context,"Unknown error", Toast.LENGTH_SHORT).show()
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
}