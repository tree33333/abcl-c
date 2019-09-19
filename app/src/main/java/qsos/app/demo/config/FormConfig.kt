package qsos.app.demo.config

import kotlinx.coroutines.*
import qsos.core.form.config.IFormConfig
import qsos.core.form.db.entity.FormValueOfFile
import qsos.core.form.db.entity.FormValueOfLocation
import timber.log.Timber

/**
 * @author : 华清松
 * 表单文件操作代理
 */
class FormConfig : IFormConfig {

    override fun takeCamera(onSuccess: (FormValueOfFile) -> Any) {
        Timber.tag("表单文件代理").i("拍照")
        CoroutineScope(Job()).launch(Dispatchers.Main) {
            val takeFile = async(Dispatchers.IO) {
                val file = FormValueOfFile(fileId = "0001", fileName = "拍照", filePath = "/0/data/vip.qsos.demo/temp/logo.png", fileType = ".png", fileUrl = "http://www.qsos.vip/resource/logo.png")
                file
            }
            val file = takeFile.await()
            onSuccess.invoke(file)
        }
    }

    override fun takeGallery(onSuccess: (FormValueOfFile) -> Any) {
        Timber.tag("表单文件代理").i("图库")
        CoroutineScope(Job()).launch(Dispatchers.Main) {
            val takeFile = async(Dispatchers.IO) {
                val file = FormValueOfFile(fileId = "0002", fileName = "图库", filePath = "/0/data/vip.qsos.demo/temp/logo.jpg", fileType = ".jpg", fileUrl = "http://www.qsos.vip/resource/logo.jpg")
                file
            }
            val file = takeFile.await()
            onSuccess.invoke(file)
        }
    }

    override fun takeVideo(onSuccess: (FormValueOfFile) -> Any) {
        Timber.tag("表单文件代理").i("视频")
        CoroutineScope(Job()).launch(Dispatchers.Main) {
            val takeFile = async(Dispatchers.IO) {
                val file = FormValueOfFile(fileId = "0003", fileName = "视频", filePath = "/0/data/vip.qsos.demo/temp/logo.mp4", fileType = ".mp4", fileUrl = "http://www.qsos.vip/resource/logo.mp4")
                file
            }
            val file = takeFile.await()
            onSuccess.invoke(file)
        }
    }

    override fun takeAudio(onSuccess: (FormValueOfFile) -> Any) {
        Timber.tag("表单文件代理").i("音频")
        CoroutineScope(Job()).launch(Dispatchers.Main) {
            val takeFile = async(Dispatchers.IO) {
                val file = FormValueOfFile(fileId = "0004", fileName = "音频", filePath = "/0/data/vip.qsos.demo/temp/logo.aar", fileType = ".aar", fileUrl = "http://www.qsos.vip/resource/logo.aar")
                file
            }
            val file = takeFile.await()
            onSuccess.invoke(file)
        }
    }

    override fun takeFile(mimeTypes: ArrayList<String>, onSuccess: (FormValueOfFile) -> Any) {
        Timber.tag("表单文件代理").i("文件")
        CoroutineScope(Job()).launch(Dispatchers.Main) {
            val takeFile = async(Dispatchers.IO) {
                val file = FormValueOfFile(fileId = "0004", fileName = "文件", filePath = "/0/data/vip.qsos.demo/temp/logo.pdf", fileType = ".pdf", fileUrl = "http://www.qsos.vip/resource/logo.pdf")
                file
            }
            val file = takeFile.await()
            onSuccess.invoke(file)
        }
    }

    override fun takeLocation(onSuccess: (FormValueOfLocation) -> Any) {
        Timber.tag("表单位置代理").i("位置")
        CoroutineScope(Job()).launch(Dispatchers.Main) {
            val takeLocation = async(Dispatchers.IO) {
                val location = FormValueOfLocation(locName = "位置", locX = 30.0000, locY = 104.000000)
                location
            }
            val location = takeLocation.await()
            onSuccess.invoke(location)
        }
    }

    override fun previewFile(formValueOfFile: FormValueOfFile) {
        Timber.tag("表单文件预览代理").i("文件")
    }

    override fun previewLocation(formValueOfLocation: FormValueOfLocation) {
        Timber.tag("表单位置预览代理").i("位置")
    }
}