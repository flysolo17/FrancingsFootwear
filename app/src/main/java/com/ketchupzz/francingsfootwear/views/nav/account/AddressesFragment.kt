package com.ketchupzz.francingsfootwear.views.nav.account

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentAddressesBinding
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import com.ketchupzz.francingsfootwear.views.adapters.AddressesAdapter
import com.ketchupzz.francingsfootwear.views.adapters.OnAddressAdapterClicked


class AddressesFragment : Fragment() , OnAddressAdapterClicked {

    private lateinit var binding : FragmentAddressesBinding
    private  val authViewModel by activityViewModels<AuthViewModel>()
    private lateinit var loadingDialog: LoadingDialog

    private val args by navArgs<AddressesFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressesBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        binding.recyclerviewAddresses.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter  = AddressesAdapter(binding.root.context,args.customer.addresses,this@AddressesFragment)
        }
        attachSwipeToDelete()
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCreateAddress.setOnClickListener {
            val  directions = AddressesFragmentDirections.actionAddressesFragmentToCreateAddressFragment(args.customer)
            findNavController().navigate(directions)
        }
    }

    override fun updateDefaultAddress(position: Int) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Update Default Address")
            .setMessage("Are you sure you want to make this address as default ?")
            .setPositiveButton("Yes") { dialog, _ ->
                args.customer.addresses.forEach {
                    it.default = false
                }
                args.customer.addresses[position].default = true
                updateDefaultAddress(args.customer.id!!, args.customer.addresses).also {
                    dialog.dismiss()
                }
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()

            }.show()
    }

    private fun updateDefaultAddress(uid : String ,addresses: List<Addresses>) {
        authViewModel.createAddress(uid, addresses) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Updating Default address..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(context,"Successfully Updated", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun attachSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Do nothing, as we don't support dragging
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                showDeleteAddressDialog(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewAddresses)
    }


    private fun showDeleteAddressDialog(position: Int) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Delete Address")
            .setMessage("Are you sure you want to delete this address?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteAddress(position)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                binding.recyclerviewAddresses.adapter?.notifyItemChanged(position)
            }
            .show()
    }

    private fun deleteAddress(position: Int) {
        args.customer.addresses.removeAt(position)
        deletingAddress(args.customer.id!!,args.customer.addresses)
        binding.recyclerviewAddresses.adapter?.notifyItemRemoved(position)
    }


    private fun deletingAddress(uid : String ,addresses: List<Addresses>) {
        authViewModel.createAddress(uid, addresses) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Deleting address..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()

                    Toast.makeText(context,"Successfully Deleted", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }


}