package com.ketchupzz.francingsfootwear.views.nav.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentMessagesBinding
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.models.messages.Messages
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import com.ketchupzz.francingsfootwear.viewmodel.MessagesViewModel


class MessagesFragment : Fragment() {

    private lateinit var binding : FragmentMessagesBinding
    private lateinit var loadingDialog: LoadingDialog
    private val messagesViewModel by activityViewModels<MessagesViewModel>()
    private val authViewModel by activityViewModels<AuthViewModel>()
    private var customer : Customer ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMessages.setOnClickListener {
            val message = binding.inputMessages.text.toString()
            if (customer == null) {
                Toast.makeText(view.context,"Please login",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (message.isEmpty()) {
                Toast.makeText(view.context,"enter message",Toast.LENGTH_SHORT).show()
                 return@setOnClickListener
            }
            val messages = Messages(
                senderID = customer?.id ?: "",
                receiverID = "P1t0Wwrtm1Vg40Mrx6TLp2t79Sl2",
                message = message,
            )
            sendMessage(messages)
        }

        observers()
    }


    private fun observers() {
        authViewModel.customer.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting profile..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    this.customer = it.data
                }
            }
        }
        messagesViewModel.messages.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting all messages!!")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    val messagesAdapter = MessageAdapter(binding.root.context,it.data, customer!!)
                    val linearLayoutManager = LinearLayoutManager(binding.root.context)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    binding.recyclerviewMessages.apply {
                        layoutManager = linearLayoutManager
                        adapter = messagesAdapter
                    }
                }

            }
        }
    }
    private fun sendMessage(messages: Messages) {
        messagesViewModel.messagesRepository.sendMessage(messages) {
            when(it) {
                is UiState.FAILED -> {
                    binding.buttonMessages.isClickable = true
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    binding.buttonMessages.isClickable = false
                }
                is UiState.SUCCESS -> {
                    binding.buttonMessages.isClickable = true
                    binding.inputMessages.setText("")
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}