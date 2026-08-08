package com.example.farmassistant

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.VisionImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private lateinit var txtStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val edtX = findViewById<EditText>(R.id.edtX)
        val edtY = findViewById<EditText>(R.id.edtY)
        txtStatus = findViewById(R.id.txtStatus)

        btnStart.setOnClickListener {
            val x = edtX.text.toString()
            val y = edtY.text.toString()
            if (x.isEmpty() || y.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tọa độ trước!", Toast.LENGTH_SHORT).show()
            } else {
                txtStatus.text = "Trạng thái: Đang theo dõi thời gian thu hoạch..."
            }
        }
    }

    private fun processScreenshot(bitmap: Bitmap) {
        val image = VisionImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                if (text.contains("Thu hoạch sau")) {
                    val timeString = parseTime(text)
                    txtStatus.text = "Thời gian còn lại: $timeString"
                }
            }
    }

    private fun parseTime(text: String): String {
        val regex = """\d{2}:\d{2}:\d{2}""".toRegex()
        return regex.find(text)?.value ?: "00:00:00"
    }
}
