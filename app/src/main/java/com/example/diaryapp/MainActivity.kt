package com.example.diaryapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.diaryapp.databinding.ActivityMainBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), DialogInterface {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    private val shows: SaveDialog by lazy { SaveDialog(this, this) }
    private val tag: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initCalendar()
        initCommit()
    }

    private fun initCalendar() {
        // 오늘 날짜 구하기
        val today: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        year = today.slice(0..3).toInt()
        month = today.slice(4..5).toInt()
        day = today.slice(6..7).toInt()
        val todayText = "$year / $month / $day"
        binding.date.text = todayText
        Log.d(tag, "$today - onCreate() called")

        // drawer listener 설정
        binding.drawer.addPanelSlideListener(PanelEventListener())

        // 날짜 선택
        binding.calendar.setOnDateChangeListener { _, y, m, d ->
            Log.d(tag, "$y ${m + 1} $d")
            val text = "$y / ${m + 1} / $d"
            binding.date.text = text
            year = y
            month = m + 1
            day = d
        }
    }

    private fun initCommit() {
        binding.completeBtn.setOnClickListener {
            Log.d(tag, "MainActivity - onDialogClicked() called")
            shows.show()
        }
    }

    // 패널 이벤트
    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener {
        override fun onPanelSlide(panel: View?, slideOffset: Float) {}

        override fun onPanelStateChanged(panel: View?,
                                         previousState: SlidingUpPanelLayout.PanelState?,
                                         newState: SlidingUpPanelLayout.PanelState?) {
            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                Log.d(tag, "panel open")
                val textView: TextView = findViewById(R.id.text)
                try {
                    val file: FileInputStream? = openFileInput("$year$month$day.txt")
                    val dis = DataInputStream(file)
                    val fText: String? = dis.readUTF()
                    textView.text = fText
                    dis.close()

                    Log.d(tag, "$fText - panel open success")
                } catch (e: java.lang.Exception) {
                    textView.text = ""
                }
            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                Log.d(tag, "panel close")
            }
        }
    }

    override fun onYes() {
        shows.dismiss()
        val file: FileOutputStream = openFileOutput("$year$month$day.txt", MODE_PRIVATE)
        val dos = DataOutputStream(file)
        dos.writeUTF(binding.text.toString())
        dos.flush()
        dos.close()
        Log.d(tag, "$year$month$day 열림")
    }

    override fun onNo() {
        shows.dismiss()
        Log.d(tag, "닫힘")
    }
}