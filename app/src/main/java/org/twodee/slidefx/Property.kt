package org.twodee.slidefx

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class Property @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, styleAttributes: Int = 0) : ConstraintLayout(context, attributes, styleAttributes) {
  private val label: TextView
  private val slider: SeekBar
  private val editor: EditText

  init {
    inflate(context, R.layout.sliderbox, this)

    label = findViewById(R.id.label)
    slider = findViewById(R.id.slider)
    editor = findViewById(R.id.editor)

    attributes?.let {
      label.text = it.getAttributeValue(null, "label")
    }
  }
}