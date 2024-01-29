package com.ketchupzz.francingsfootwear.views.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentCheckoutBinding
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.models.transaction.Items
import com.ketchupzz.francingsfootwear.models.transaction.Payment
import com.ketchupzz.francingsfootwear.models.transaction.PaymentMethods
import com.ketchupzz.francingsfootwear.models.transaction.PaymentStatus
import com.ketchupzz.francingsfootwear.models.transaction.TransactionStatus
import com.ketchupzz.francingsfootwear.models.transaction.Transactions
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.calculateShippingFee
import com.ketchupzz.francingsfootwear.utils.computeItemSubtotal
import com.ketchupzz.francingsfootwear.utils.countItems
import com.ketchupzz.francingsfootwear.utils.generateRandomNumber
import com.ketchupzz.francingsfootwear.utils.toPHP
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import com.ketchupzz.francingsfootwear.viewmodel.TransactionViewModel
import com.ketchupzz.francingsfootwear.views.adapters.ItemsAdapter
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.PaymentAdapter
import java.util.Date


class CheckoutFragment : Fragment() {

    private lateinit var binding : FragmentCheckoutBinding
    private lateinit var loadingDialog: LoadingDialog
    private val args by navArgs<CheckoutFragmentArgs>()
    private val authViewModel by activityViewModels<AuthViewModel>()
    private var customer : Customer ? =null
    private var defaultAddress : Addresses ? = null
    private  var shippingFee : Double = 0.00
    private val transactionViewModel by activityViewModels<TransactionViewModel>()
    private lateinit var paymentAdapter : PaymentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       shippingFee =  calculateShippingFee(countItems(args.items.toList()))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        paymentAdapter = PaymentAdapter(binding.root.context, listOf(PaymentMethods.COD,PaymentMethods.GCASH))
        binding.recyclerviewItems.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = ItemsAdapter(binding.root.context,args.items.toList())
        }
        binding.recyclerviewPayments.apply {
            adapter = paymentAdapter
        }
        binding.textShipping.text = shippingFee.toPHP()
        binding.textSubtotal.text = (computeItemSubtotal(args.items.toList())).toPHP()
        binding.textItems.text = countItems(args.items.toList()).toString()
        binding.textTotal.text = computeItemSubtotal(args.items.toList()).toPHP()
        binding.textTransactionTotal.text = (computeItemSubtotal(args.items.toList())  + shippingFee).toPHP()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()

        binding.layoutAddress.setOnClickListener {
            if (customer != null) {
                val directions = CheckoutFragmentDirections.actionCheckoutFragmentToAddressesFragment(customer!!)
                findNavController().navigate(directions)
            }
        }

        binding.buttonOrderNow.setOnClickListener {
            if (defaultAddress == null) {
                Toast.makeText(view.context,"Please add address",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val total = computeItemSubtotal(args.items.toList())  + shippingFee
            val payment = Payment(total = total, method = PaymentMethods.entries[paymentAdapter.getSelected()] , receipt = "")
            val transactions = Transactions(
                customerID = customer?.id ?: "",
                items = args.items.toList(),
                shippingFee = shippingFee,
                payment = payment,
                address = defaultAddress!!,)
            createTransaction(transactions)
        }

    }
    private fun createTransaction(transactions: Transactions) {
        transactionViewModel.transactionRepository.createTransaction(transactions) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Creating order..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    if (paymentAdapter.getSelected() == 1) {
                        val directions= CheckoutFragmentDirections.actionCheckoutFragmentToPaymentFragment(transactions)
                        findNavController().navigate(directions)
                    } else {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }
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
                    loadingDialog.showDialog("Getting user info..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    customer = it.data
                    defaultAddress  = customer?.addresses?.find { it.default == true }
                    displayCurrentAddress(defaultAddress)
                }
            }
        }
    }
    private fun displayCurrentAddress(address: Addresses?) {
        address?.let {
            binding.textFullname.text = it.contacts?.name ?: "--no-name--"
            binding.textPhone.text = it.contacts?.phone ?: "--no-phone--"
            binding.textAddress.text= it.name ?: "no default address yet"
            return
        }
        binding.textFullname.text = "--no-name--"
        binding.textPhone.text = "--no-phone--"
        binding.textAddress.text= "no default address yet"
    }
}