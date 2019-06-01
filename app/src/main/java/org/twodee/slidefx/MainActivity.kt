package org.twodee.slidefx

import android.Manifest
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat.requestPermissions
import java.io.File
import java.io.FileOutputStream
import java.util.*

const val SAMPLES_PER_SECOND = 44100

class MainActivity : PermittedActivity() {
  private var player: MediaPlayer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.playButton).setOnClickListener {
      val samples = FloatArray(100000) { i ->
        val t = i / SAMPLES_PER_SECOND.toFloat()
        val factor = Math.sin(2 * Math.PI * t * 440).toFloat()
        factor
      }

      Log.d("FOO", Arrays.toString(samples))

      val outFile = File(Environment.getExternalStorageDirectory(), "sound.wav")
      val outStream = FileOutputStream(outFile)

      Log.d("FOO", "${Uri.fromFile(outFile)}")

      writeWav(outStream, SAMPLES_PER_SECOND, samples)
      player = MediaPlayer.create(this, Uri.fromFile(outFile))?.apply {
        start()
      }
    }

    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100, {}, {})
  }
}
