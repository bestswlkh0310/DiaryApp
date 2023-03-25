package com.example.diaryapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), DialogInterface {
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var shows: SaveDialog? = null
    private val TAG: String = "로그"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 캘린더, 날짜 텍스트
        val calendar: CalendarView = findViewById(R.id.celendar)
        val date: TextView = findViewById(R.id.date)
        val drawer: SlidingUpPanelLayout = findViewById(R.id.drawer)

        today() // 오늘 날짜로 초기화
        drawer.addPanelSlideListener (PanelEventListener()) // 패널 이벤트 => 날짜에 맞는 내용 불러오기

        // 캘린더에서 날짜 선택 => 날짜 텍스트 변경
        calendar.setOnDateChangeListener { _, y, m, d ->
            Log.d(TAG, "$y ${m + 1} $d")
            date.text = "$y / ${m + 1} / $d"
            year = y
            month = m + 1
            day = d
        }
    }

    // 오늘 날짜 구하기
    private fun today() {
        val date: TextView = findViewById(R.id.date)
        val today: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        year = today.slice(0..3)?.toInt()
        month = today.slice(4..5)?.toInt()
        day = today.slice(6..7)?.toInt()
        date.text = "$year / $month / $day"
        Log.d(TAG, "$today - onCreate() called")
    }

    fun complete2(v: View) {
        Log.d(TAG, "MainActivity - onDialogClicked() called")

        val saveDialog = SaveDialog(this, this)
        saveDialog.show()
        shows = saveDialog
    }

    // 패널 이벤트
    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener {
        // 패널이 슬라이드 중일 때
        override fun onPanelSlide(panel: View?, slideOffset: Float) {}

        // 패널의 상태가 변했을 때
        override fun onPanelStateChanged(panel: View?,
                                         previousState: SlidingUpPanelLayout.PanelState?,
                                         newState: SlidingUpPanelLayout.PanelState?) {
            // 열림
            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                Log.d(TAG, "패널 열림")
                var textView: TextView = findViewById(R.id.text)
                try {
                    val file: FileInputStream? = openFileInput("$year$month$day.txt")
                    val dis: DataInputStream? = DataInputStream(file)
                    val fText: String? = dis?.readUTF()
                    Log.d(TAG, "$fText - 패널 열림")
                    textView.text = fText
                    dis?.close()
                } catch (e: java.lang.Exception) {
                    textView.text = ""
                }
                // 닫힘
            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                Log.d(TAG, "패널 닫힘")
            }
        }
    }

    override fun onYes() {
        shows!!.dismiss()
        var textView: TextView = findViewById(R.id.text)
        var text: String? = textView.text.toString()
        val fos: FileOutputStream = openFileOutput("$year$month$day.txt", MODE_PRIVATE)
        val dos: DataOutputStream = DataOutputStream(fos)

        dos.writeUTF(text)
        dos.flush()
        dos.close()
        Log.d(TAG, "$year$month$day 열림")
        //textView.text = ""
    }

    override fun onNo() {
        shows!!.dismiss()
        Log.d(TAG, "닫힘")
    }
}