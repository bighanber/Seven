package com.luuu.seven.http.download

import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * 大文件开多线程进行下载
 */
class FileDownload(
    // 定义下载资源的路径
    private var path: String,
    // 指定所下载的文件的保存位置
    private var targetFile: String,
    // 定义需要使用多少线程下载资源
    private var threadNum: Int
) {
    // 定义下载的线程对象
    private var threads: Array<DownThread?> = arrayOfNulls(threadNum)
    // 定义下载的文件的总大小
    private var fileSize: Int = 0

    fun download() {
        val url = URL(path)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5 * 1000
        conn.requestMethod = "GET"
        conn.setRequestProperty(
            "Accept",
            "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                    + "application/x-shockwave-flash, application/xaml+xml, "
                    + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                    + "application/x-ms-application, application/vnd.ms-excel, "
                    + "application/vnd.ms-powerpoint, application/msword, */*"
        )
        conn.setRequestProperty("Accept-Language", "zh-CN")
        conn.setRequestProperty("Charset", "UTF-8")
        conn.setRequestProperty("Connection", "Keep-Alive")
        // 得到文件大小
        fileSize = conn.contentLength
        conn.disconnect()
        val currentPartSize = fileSize / threadNum + 1
        val file = RandomAccessFile(targetFile, "rw")
        // 设置本地文件的大小
        file.setLength(fileSize.toLong())
        file.close()
        for (i in 0 until threadNum) {
            // 计算每条线程的下载的开始位置
            val startPos = i * currentPartSize
            // 每个线程使用一个RandomAccessFile进行下载
            val currentPart = RandomAccessFile(
                targetFile,
                "rw"
            )
            // 定位该线程的下载位置
            currentPart.seek(startPos.toLong())
            // 创建下载线程
            threads[i] = DownThread(
                startPos, currentPartSize,
                currentPart, path
            )
            // 启动下载线程
            threads[i]!!.start()
        }
    }

    // 获取下载的完成百分比
    fun getCompleteRate(): Double {
        // 统计多条线程已经下载的总大小
        var sumSize = 0
        for (i in 0 until threadNum) {
            sumSize += threads[i]!!.length
        }
        // 返回已经完成的百分比
        return sumSize * 1.0 / fileSize
    }

    class DownThread(
        // 当前线程的下载位置
        private var startPos: Int,
        // 定义当前线程负责下载的文件大小
        private var currentPartSize: Int,
        // 当前线程需要下载的文件块
        private var currentPart: RandomAccessFile?,
        private var path: String
    ) : Thread() {

        // 定义已经该线程已下载的字节数
        var length = 0

        override fun run() {
            try {
                val url = URL(path)
                val conn: HttpURLConnection = url
                    .openConnection() as HttpURLConnection
                conn.connectTimeout = 5 * 1000
                conn.requestMethod = "GET"
                conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                            + "application/x-shockwave-flash, application/xaml+xml, "
                            + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                            + "application/x-ms-application, application/vnd.ms-excel, "
                            + "application/vnd.ms-powerpoint, application/msword, */*"
                )
                conn.setRequestProperty("Accept-Language", "zh-CN")
                conn.setRequestProperty("Charset", "UTF-8")
                val inStream = conn.inputStream
                // 跳过startPos个字节，表明该线程只下载自己负责哪部分文件。
                inStream.skip(startPos.toLong())
                val buffer = ByteArray(1024)
                var hasRead = 0
                // 读取网络数据，并写入本地文件
                while (length < currentPartSize
                    && inStream.read(buffer).also { hasRead = it } > 0
                ) {
                    currentPart!!.write(buffer, 0, hasRead)
                    // 累计该线程下载的总大小
                    length += hasRead
                }
                currentPart!!.close()
                inStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}