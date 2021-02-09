package com.wizeline.bookchallenge.utils

import android.content.Context
import android.graphics.Bitmap
import com.wizeline.bookchallenge.logic.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

suspend fun Context.saveFile(fileName: String, bitmap: Bitmap) =  withContext(Dispatchers.IO){
  val stream = ByteArrayOutputStream()
  bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
  val byteArray = stream.toByteArray()
  val fileDir = File(filesDir, "/emojis")
  if (!fileDir.exists()) {
    fileDir.mkdirs()
  }
  val fileToSave = File(fileDir, fileName)
  if (fileToSave.createNewFile()) {
    val outputStream = BufferedOutputStream(FileOutputStream(fileToSave))
    outputStream.use {
      it.write(byteArray)
    }
  }
  return@withContext
}

suspend fun Context.readFiles(): List<HistoryItem> =  withContext(Dispatchers.IO){
  val historyItems = ArrayList<HistoryItem>()
  val fileDirectory = File(filesDir, "/emojis")
  if (!fileDirectory.exists()) {
    return@withContext emptyList<HistoryItem>()
  }
  for (file in (fileDirectory.listFiles { filename -> filename.endsWith(".png") } as Array<File>)) {

    historyItems.add(HistoryItem(file.absolutePath, file.name))
  }
  return@withContext historyItems
}
