package com.example.diaryapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class SaveDialog(context: Context,
                 dialogInterface: DialogInterface)
                : Dialog(context), View.OnClickListener {

    private var dialogInterface: DialogInterface? = null
    private val TAG: String = "로그"

    init {
        this.dialogInterface = dialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog)

        val saveBtn: Button = findViewById(R.id.dialog_btn)
        val cancelBtn: Button = findViewById(R.id.dialog_btn1)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        saveBtn.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_btn -> {
                this.dialogInterface?.onYes()
            }
            R.id.dialog_btn1 -> {

                this.dialogInterface?.onNo()
            }
        }
    }
}