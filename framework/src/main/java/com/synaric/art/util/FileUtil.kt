package com.synaric.art.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import java.io.*
import java.util.*

class FileUtil {

    companion object {

        private val cachedWriteRequest = WeakHashMap<String, String>()

        /**
         * 写入内部文件，如果存在同名的文件，将会被覆盖。
         * @receiver Context
         * @param type String 子目录
         * @param fileName String 文件名
         * @param content String 文件内容
         * @return Unit
         */
        fun Context.saveFileToInternalFile(
            type: String,
            fileName: String,
            content: String) {
            val parent = File("$filesDir/$type")
            if (!parent.exists() || !parent.isDirectory) {
                parent.mkdirs()
            }
            val file = File(parent, fileName)
            // 覆盖老文件
            if (file.exists() && file.isFile) {
                file.delete()
            }
            file.createNewFile()

            val uri = Uri.fromFile(file)
            try {
                contentResolver.openFileDescriptor(uri, "w")?.use {
                    BufferedWriter(FileWriter(it.fileDescriptor)).use { fos ->
                        fos.write(content)
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        /**
         * 写入SD卡文件，如果存在同名的文件，将会被覆盖。
         * @receiver Context
         * @param root String 根目录
         * @param type String 子目录
         * @param fileName String 文件名
         * @param content String 文件内容
         * @return Unit
         */
        fun Context.saveFileToSD(
            root: String,
            type: String,
            fileName: String,
            content: String,
            onCreateFile: (uri: Uri, filename: String) -> Unit
        ) {
            val uri = createUri(root, type, fileName)
            try {
                DocumentsContract.deleteDocument(contentResolver, uri)
            } catch (e: Exception) {
            }
            cachedWriteRequest[uri.toString()] = content
//            alterDocument(applicationContext, uri, content)
            onCreateFile(uri, fileName)
        }

        /**
         * 读取SD卡文件
         * @receiver Context
         * @param root String 根目录
         * @param type String 子目录
         * @param fileName String 文件名
         * @return String 文件内容
         */
        fun Context.readFromSD(
            root: String,
            type: String,
            fileName: String
        ): String {
            val uri = createUri(root, type, fileName)
            return readTextFromUri(applicationContext, uri)
        }


        private fun createUri(
            root: String,
            type: String,
            fileName: String,
        ): Uri {
            val fn =
                "${Environment.getExternalStorageDirectory().canonicalPath}/$root/$type/$fileName"
            return Uri.fromFile(File(fn))
        }

        fun createFile(context: Activity, pickerInitialUri: Uri, fileName: String) {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/text"
                putExtra(Intent.EXTRA_TITLE, fileName)
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
            }
            context.startActivityForResult(intent, 101)
        }

        fun alterDocument(context: Context, uri: Uri, content: String) {
            val contentResolver = context.contentResolver
            try {
                contentResolver.openFileDescriptor(uri, "w")?.use {
                    BufferedWriter(FileWriter(it.fileDescriptor)).use { fos ->
                        fos.write(content)
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun readTextFromUri(context: Context, uri: Uri): String {
            val stringBuilder = StringBuilder()
            val contentResolver = context.contentResolver
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                }
            }
            return stringBuilder.toString()
        }
    }
}