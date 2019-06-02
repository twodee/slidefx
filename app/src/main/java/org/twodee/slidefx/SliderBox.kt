package org.twodee.slidefx

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.NumberFormatException

class SliderBox @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, styleAttributes: Int = 0) : ConstraintLayout(context, attributes, styleAttributes) {
  private val label: TextView
  private val slider: SeekBar
  private val editor: EditText

  private var min: Float = 0f
  private var max: Float = 1f
  private var value: Float = 0.5f
  private var isEventing = false

  init {
    inflate(context, R.layout.sliderbox, this)

    label = findViewById(R.id.label)
    slider = findViewById(R.id.slider)
    editor = findViewById(R.id.editor)

    slider.min = 0
    slider.max = 1000

    attributes?.let {
      label.text = it.getAttributeValue(null, "label")
      min = it.getAttributeFloatValue(null, "min", min)
      max = it.getAttributeFloatValue(null, "max", max)
      value = it.getAttributeFloatValue(null, "value", value)
    }

    synchronizeEditor()
    synchronizeSlider()

    slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(p0: SeekBar?, tick: Int, p2: Boolean) {
        if (isEventing) return
        isEventing = true
        value = tick / slider.max.toFloat() * (max - min) + min
        synchronizeEditor()
        isEventing = false
      }

      override fun onStartTrackingTouch(p0: SeekBar?) {}
      override fun onStopTrackingTouch(p0: SeekBar?) {}
    })

    editor.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(p0: Editable?) {}
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        if (isEventing) return
        isEventing = true
        try {
          value = editor.text.toString().toFloat()
          value = Math.max(min, Math.min(max, value))
        } catch (e: NumberFormatException) {
          value = min
        }
        synchronizeSlider()
        isEventing = false
      }
    })
  }

  private fun synchronizeSlider() {
    slider.progress = ((value - min) / (max - min) * slider.max).toInt()
  }

  private fun synchronizeEditor() {
    editor.setText(value.toString())
  }
}