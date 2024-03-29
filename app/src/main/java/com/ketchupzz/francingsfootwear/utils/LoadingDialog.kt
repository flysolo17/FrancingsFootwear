package com.ketchupzz.francingsfootwear.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ketchupzz.francingsfootwear.R

class LoadingDialog(private val context: Context) {

    var alertDialog: AlertDialog ? = null
    fun showDialog(title : String){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
        val view : View = LayoutInflater.from(context).inflate(R.layout.dialog_loading,null)
        view.findViewById<TextView>(R.id.textTitle).text = title
        materialAlertDialogBuilder.setView(view)
        materialAlertDialogBuilder.setCancelable(false)
        alertDialog = materialAlertDialogBuilder.create()
        alertDialog?.show()
    }
    fun closeDialog(){
        alertDialog?.dismiss()
    }
}