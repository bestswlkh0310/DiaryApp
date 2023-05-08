package com.example.diaryapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.example.diaryapp.databinding.DialogBinding

class SaveDialog(context: Context,
                 private val dialogInterface: DialogInterface
): Dialog(context), View.OnClickListener {
    private val binding: DialogBinding by lazy { DialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            dialogBtn.setOnClickListener(this@SaveDialog)
            binding.dialogBtn1.setOnClickListener(this@SaveDialog)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_btn -> dialogInterface.onYes()
            R.id.dialog_btn1 -> dialogInterface.onNo()
        }
    }
}