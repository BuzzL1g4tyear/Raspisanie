package com.example.Ras

import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipDrawable
import kotlinx.android.synthetic.main.activity_missing.*

class MissingActivity : AppCompatActivity() {

    var lastLength: Int = 0
    var finishRow: String = ""
    val SPACE: Char = 32.toChar()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing)

        var editText: EditText? = null

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastLength = s!!.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (lastLength > s!!.length) return
                if (s[s.length - 1] == SPACE) {
                    val chip = ChipDrawable.createFromResource(this@MissingActivity, R.xml.standalone_chip)

                    chip.text = finishRow
                    chip.setBounds(0, 0, chip.intrinsicWidth, chip.intrinsicHeight)

                    val span = ImageSpan(chip)
                    val text = editText?.text!!
                    text.setSpan(span, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                } else {
                    finishRow = s.toString()
                    Log.d("MyLog", "finishRow")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editText?.addTextChangedListener(textWatcher)
    }

}