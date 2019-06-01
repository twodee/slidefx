package org.twodee.slidefx

import java.io.Closeable
import java.io.DataOutputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class LittleEndianDataOutputStream(stream: FileOutputStream) : Closeable {
  private val out: DataOutputStream = DataOutputStream(stream)
  private val bytes: ByteArray = ByteArray(4)
  private val buffer: ByteBuffer = ByteBuffer.wrap(bytes).apply {
    order(ByteOrder.LITTLE_ENDIAN)
  }

  fun write(s: String) {
    out.writeBytes(s)
  }

  fun write(value: Int) {
    buffer.putInt(0, value)
    out.write(bytes, 0, 4)
  }

  fun write(value: Short) {
    buffer.putShort(0, value)
    out.write(bytes, 0, 2)
  }

  fun write(shorts: ShortArray) {
    for (i in shorts.indices) {
      write(shorts[i])
    }
  }

  override fun close() {
    out.close()
  }
}