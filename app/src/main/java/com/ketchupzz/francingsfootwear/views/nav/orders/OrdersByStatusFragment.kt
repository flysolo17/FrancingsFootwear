package com.ketchupzz.francingsfootwear.views.nav.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentOrdersByStatusBinding
import com.ketchupzz.francingsfootwear.models.transaction.TransactionStatus
import com.ketchupzz.francingsfootwear.models.transaction.Transactions
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.TransactionViewModel

class OrdersByStatusFragment : Fragment() ,TransactionClickListener{
    private var position: Int ? = null
    private lateinit var binding : FragmentOrdersByStatusBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersByStatusBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionViewModel.transactions.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(view.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting All Transactions")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    val transactions = it.data.filter { it.status == TransactionStatus.entries[position ?: 0] }
                    binding.layoutNoOrder.visibility = if (transactions.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                    binding.recyclerviewTransactions.apply {
                        layoutManager = LinearLayoutManager(view.context)
                        adapter = TransactionAdapter(view.context,transactions,this@OrdersByStatusFragment)
                    }
                }
            }
        }

    }

    override fun cancelTransaction(transactions: Transactions) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Cancel Order")
            .setMessage("Are you sure you want to cancel your order with id ${transactions.id}")
            .setPositiveButton("Confirm") { dialog,_ ->
                cancelTransactions(transactionID = transactions.id).also {
                    dialog.dismiss()
                }
            }.setNegativeButton("Cancel") { dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    override fun viewTransaction(transactions: Transactions) {
        val directions = OrdersFragmentDirections.actionNavigationOrdersToReviewTransaction(transactions)
        findNavController().navigate(directions)
    }

    override fun payWithGCash(transactions: Transactions) {
        val directions = OrdersFragmentDirections.actionNavigationOrdersToPaymentFragment(transactions)
        findNavController().navigate(directions)
    }

    private fun cancelTransactions(transactionID : String) {
        transactionViewModel.transactionRepository.cancelTransaction(transactionID) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Cancelling order..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}