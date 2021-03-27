package com.example.Ras

import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipDrawable

class MissingActivity : AppCompatActivity() {

    var lastLength: Int = 0
    val SPACE: Char = 32.toChar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing)

        val chip = ChipDrawable.createFromResource(this, R.xml.standalone_chip)
        var editText: EditText? = null

        editText = findViewById(R.id.editText)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastLength = s!!.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (lastLength > s!!.length) return
                if (s[s.length - 1] == SPACE) {
                    chip.setBounds(0, 0, chip.intrinsicWidth, chip.intrinsicHeight)
                    val span = ImageSpan(chip)
                    val text = editText.text!!
                    text.setSpan(span, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        editText.addTextChangedListener(textWatcher)

    }
}