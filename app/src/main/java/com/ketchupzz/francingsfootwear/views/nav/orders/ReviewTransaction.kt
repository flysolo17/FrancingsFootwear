package com.ketchupzz.francingsfootwear.views.nav.orders

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentReviewTransactionBinding
import com.ketchupzz.francingsfootwear.models.transaction.TransactionStatus
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.countItems
import com.ketchupzz.francingsfootwear.utils.toPHP
import com.ketchupzz.francingsfootwear.viewmodel.TransactionViewModel
import com.ketchupzz.francingsfootwear.views.adapters.ItemsAdapter
import com.ketchupzz.francingsfootwear.views.adapters.TransactionStatusAdapter


class ReviewTransaction : Fragment() {
    private lateinit var binding : FragmentReviewTransactionBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    private val args by navArgs<ReviewTransactionArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewTransactionBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction  = args.transaction
        binding.textTransactionID.text = transaction.id
        binding.textFullname.text = transaction.address.contacts?.name ?:""
        binding.textPhone.text = transaction.address.contacts?.phone ?:""
        binding.textAddress.text = transaction.address.name ?:""
        binding.textStatus.backgroundTintList  = ColorStateList.valueOf(setStatusBgColor(transaction.status))
        binding.textStatus.text = transaction.status.name
        binding.textItems.text = countItems(transaction.items).toString()
        binding.textShipping.text = transaction.shippingFee.toPHP()
        binding.textTotal.text = transaction.payment.total.toPHP()
        binding.textPayment.text = transaction.payment.method.name +" - " + transaction.payment.status.name
        binding.recyclerviewItems.apply {
            layoutManager  = LinearLayoutManager(view.context)
            adapter = ItemsAdapter(view.context,args.transaction.items)
        }
        binding.recyclerviewStatus.apply {
            layoutManager  = LinearLayoutManager(view.context)
            adapter = TransactionStatusAdapter(view.context,args.transaction.details.reversed())
        }
    }
    private fun setStatusBgColor(transactionStatus: TransactionStatus): Int {
        return when(transactionStatus) {
            TransactionStatus.PENDING -> ContextCompat.getColor(binding.root.context, R.color.pending)
            TransactionStatus.ACCEPTED -> ContextCompat.getColor(binding.root.context, R.color.accepted)
            TransactionStatus.ON_DELIVERY -> ContextCompat.getColor(binding.root.context, R.color.ondelivery)
            TransactionStatus.COMPLETE -> ContextCompat.getColor(binding.root.context, R.color.completed)
            TransactionStatus.CANCELLED -> ContextCompat.getColor(binding.root.context, R.color.cancelled)
            TransactionStatus.DECLINED -> ContextCompat.getColor(binding.root.context, R.color.declined)
        }
    }

}