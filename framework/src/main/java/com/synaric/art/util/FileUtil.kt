package com.synaric.art.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class FileUtil {

    companion object {

        private val cachedWriteRequest = WeakHashMap<String, Uri>()

        /**
         * 写入内部文件，如果存在同名的文件，将会被覆盖。
         * @param context Context
         * @param type String 子目录
         * @param fileName String 文件名
         * @param content String 文件内容
         * @return Unit
         */
        fun saveFileToInternalFile(
            context: Context,
            type: String,
            fileName: String,
            content: String
        ) {
            val file = createFileToInternalFile(context, type, fileName)
            val uri = Uri.fromFile(file)
            try {
                context.contentResolver.openFileDescriptor(uri, "w")?.use {
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

        fun createFileToInternalFile(
            context: Context,
            type: String,
            fileName: String,
        ): File {
            val parent = File("${context.filesDir}/$type")
            if (!parent.exists() || !parent.isDirectory) {
                parent.mkdirs()
            }
            val file = File(parent, fileName)
            // 覆盖老文件
            if (file.exists() && file.isFile) {
                file.delete()
            }
            file.createNewFile()

            return file
        }

        /**
         * 将文件列表压缩为zip文件并写入到内部存储。
         * @param context Context
         * @param fileList List<File> 需要压缩的文件列表
         * @param type String 子目录
         * @param fileName String 文件名
         * @return Unit
         */
        fun zipFileToInternalFile(
            context: Context,
            fileList: List<File>,
            type: String,
            fileName: String
        ): File? {
            return zip(context, fileList, getInternalFilePath(context, type, fileName))
        }

        /**
         * 文件解压为zip文件并写入到内部存储。
         * @param context Context
         * @param type String 子目录
         * @param fileName String 文件名
         * @return Boolean
         */
        fun unzipFileToInternalFile(
            context: Context,
            type: String,
            fileName: String
        ): Boolean {
            val file = File(getInternalFilePath(context, type, fileName))
            if (!file.exists() || !file.isFile) {
                return false
            }
            return unzip(file)
        }

        /**
         * 删除内部文件。
         * @param context Context
         * @param type String 子目录
         * @return Unit
         */
        fun clearInternalFile(
            context: Context,
            type: String
        ) {
            val dir = File("${context.filesDir}/$type")
            deleteFileInterval(dir)
        }

        private fun deleteFileInterval(dir: File) {
            if (dir.exists() && dir.isDirectory) {
                dir.listFiles()?.forEach { file ->
                    if (file.isFile)
                        file.delete()
                    else if (file.isDirectory)
                        deleteFileInterval(file)
                }
                dir.delete();
            }
        }

        /**
         * 根据子目录和文件名获取内部存储上的全路径。
         * @param context Context
         * @param type String 子目录
         * @param fileName String 文件名
         * @return String
         */
        fun getInternalFilePath(
            context: Context,
            type: String,
            fileName: String
        ): String {
            return "${context.filesDir}/$type/$fileName"
        }

        private fun zip(context: Context, files: List<File>, zipFilePath: String): File? {
            if (files.isEmpty()) return null
            val zipFile = File(zipFilePath)
            if (zipFile.exists() && zipFile.isFile) {
                zipFile.delete()
            }
            zipFile.createNewFile()

            val buffer = ByteArray(1024)
            val uri = Uri.fromFile(zipFile)
            context.contentResolver.openFileDescriptor(uri, "w")?.use { fd ->
                ZipOutputStream(FileOutputStream(fd.fileDescriptor)).use { zos ->
                    for (file in files) {
                        if (!file.exists()) continue
                        zos.putNextEntry(ZipEntry(file.name))
                        FileInputStream(file).use { fis ->
                            var len: Int
                            while (fis.read(buffer).also { len = it } > 0) {
                                zos.write(buffer, 0, len)
                            }
                        }
                        zos.closeEntry()
                    }
                }
            }

            return zipFile
        }

        data class ZipIO(val entry: ZipEntry, val output: File)

        private fun unzip(file: File, unzipLocationRoot: File? = null): Boolean {

            val rootFolder = unzipLocationRoot
                ?: File(file.parentFile!!.absolutePath)
            if (!rootFolder.exists()) {
                rootFolder.mkdirs()
            }

            ZipFile(file).use { zip ->
                zip
                    .entries()
                    .asSequence()
                    .filter {
                        it.name.endsWith(".json")
                    }
                    .map {
                        val outputFile = File(rootFolder.absolutePath + File.separator + it.name)
                        ZipIO(it, outputFile)
                    }
                    .map {
                        it.output.parentFile?.run {
                            if (!exists()) mkdirs()
                        }
                        it
                    }
                    .filter { !it.entry.isDirectory }
                    .forEach { (entry, output) ->
                        zip.getInputStream(entry).use { input ->
                            output.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
            }

            return true
        }

        /**
         * 写入SD卡文件，如果存在同名的文件，将会被覆盖。
         * @param sourceFile File
         * @param fileName String
         * @param onCreateFile Function1<[@kotlin.ParameterName] String, Unit>
         * @return Unit
         */
        fun saveFileToSD(
            sourceFile: File,
            fileName: String,
            onCreateFile: (sourceFile: Uri, filename: String) -> Unit
        ) {
            cachedWriteRequest[sourceFile.toString()] = Uri.fromFile(sourceFile)
            onCreateFile(Uri.fromFile(sourceFile), fileName)
        }

        /**
         * 复制文件到指定Uri。
         * @param context Context
         * @param from Uri
         * @param to Uri
         * @return Unit
         */
        fun copyFile(
            context: Context,
            from: Uri,
            to: Uri
        ) {
            val contentResolver = context.contentResolver
            val buffer = ByteArray(1024)
            contentResolver.openFileDescriptor(to, "w")?.use { fd ->
                FileOutputStream(fd.fileDescriptor).use { fos ->
                    contentResolver.openInputStream(from)?.use { fis ->
                        var len: Int
                        while (fis.read(buffer).also { len = it } > 0) {
                            fos.write(buffer, 0, len)
                        }
                    }
                }
            }
        }

        /**
         * 用户选择文件
         * @param activity Activity
         * @param tag Int
         * @return Unit
         */
        fun selectOneFile(activity: Activity, tag: Int) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            activity.startActivityForResult(intent, tag)
        }

        fun updateFile(context: Context, uri: Uri, content: String) {
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

        /**
         * 读取文件内容
         * @param context Context
         * @param file File
         * @return String
         */
        fun readFile(context: Context, file: File): String {
            val stringBuilder = StringBuilder()
            val contentResolver = context.contentResolver
            val uri = Uri.fromFile(file)
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