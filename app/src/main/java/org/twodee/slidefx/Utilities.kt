package org.twodee.slidefx

import java.io.FileOutputStream

fun writeWav(fileStream: FileOutputStream, rate: Int, samples: FloatArray) {
  val outStream = LittleEndianDataOutputStream(fileStream).use {
    it.write("RIFF")
    it.write(36 + samples.size * 2)
    it.write("WAVE")
    it.write("fmt ")
    it.write(16)
    it.write(1.toShort())
    it.write(1.toShort())
    it.write(rate)
    it.write(rate * 2)
    it.write(2.toShort())
    it.write(16.toShort())
    it.write("data")
    it.write(samples.size * 1 * 16 / 8)
    val shorts = ShortArray(samples.size)
    samples.forEachIndexed { i, sample -> shorts[i] = (sample * 32767).toShort() }
    it.write(shorts)
  }
}