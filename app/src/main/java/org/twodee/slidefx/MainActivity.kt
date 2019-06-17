package org.twodee.slidefx

import android.Manifest
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.app.ActivityCompat.requestPermissions
import java.io.File
import java.io.FileOutputStream
import java.util.*

const val SAMPLES_PER_SECOND = 44100

class MainActivity : PermittedActivity() {
  private var player: MediaPlayer? = null
  private lateinit var playButton: MenuItem

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100, {}, {})
  }

  private fun play() {
    val p = player

    if (p == null) {
      playButton.setIcon(R.drawable.pause)
      val samples = generate()

      val outFile = File(Environment.getExternalStorageDirectory(), "sound.wav")
      val outStream = FileOutputStream(outFile)

      Log.d("FOO", "${Uri.fromFile(outFile)}")

      writeWav(outStream, SAMPLES_PER_SECOND, samples)
      player = MediaPlayer.create(this, Uri.fromFile(outFile))?.apply {
        start()
        isLooping = true
      }
    } else {
      playButton.setIcon(R.drawable.play)
      p.stop()
      p.release()
      player = null
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.actionbar, menu)
    playButton = menu.findItem(R.id.playButton)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.playButton -> {
      play()
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  private fun lookup(p: Float) = Math.sin(p * 2 * Math.PI).toFloat()

  private fun generate(): FloatArray {
    val chunkSize = 10000
    val samples = FloatArray(chunkSize * 4)
    var p = 0f

    fun fill(frequency: Float, from: Int, to: Int) {
      var speed = frequency / SAMPLES_PER_SECOND
      from.until(to).forEach { i ->
        speed += 0.00001f
        p += speed
        if (p >= 1f) {
          p -= 1f
        }
        samples[i] = lookup(p)
      }
    }

    fill(440f, chunkSize * 0, chunkSize * 1)
    fill(1000f, chunkSize * 1, chunkSize * 2)
    fill(617f, chunkSize * 2, chunkSize * 3)
    fill(440f * 4, chunkSize * 3, chunkSize * 4)

    return samples
  }
}
