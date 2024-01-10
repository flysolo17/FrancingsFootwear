package com.ketchupzz.francingsfootwear.views.nav.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentOrdersBinding
import com.ketchupzz.francingsfootwear.models.transaction.TransactionStatus
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.viewmodel.TransactionViewModel

const val POSITION = "position"
class OrdersFragment : Fragment() {

    private lateinit var binding : FragmentOrdersBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionsViewModel by activityViewModels<TransactionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachTabs()
    }

    private fun attachTabs() {
            val indicator = PurchasesTabAdapter(this,TransactionStatus.entries)
            TabLayoutMediator(binding.tabLayout,binding.pager2.apply { adapter = indicator },true) {tab,position ->
                tab.text =TransactionStatus.entries[position].toString().replace("_"," ")
            }.attach()
//        binding.tabLayout.getTabAt()!!.select()

    }
    class PurchasesTabAdapter(val fragment: Fragment,val statusList : List<TransactionStatus>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return statusList.size
        }
        override fun createFragment(position: Int): Fragment {
            val fragment = OrdersByStatusFragment()
            fragment.arguments = Bundle().apply {
                putInt(POSITION,position)
            }
            return fragment
        }
    }
}