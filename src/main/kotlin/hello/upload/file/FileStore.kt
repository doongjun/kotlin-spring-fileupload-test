package hello.upload.file

import hello.upload.domain.UploadFile
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

@Component
class FileStore {

    @Value("\${file.dir}")
    private val fileDir: String = ""

    fun getFullPath(fileName: String?): String {
        return "$fileDir$fileName"
    }

    fun storeFiles(multipartFiles: List<MultipartFile>): List<UploadFile> {
        val storeFileResult: MutableList<UploadFile> = mutableListOf()
        for(multipartFile in multipartFiles) {
            if(!multipartFile.isEmpty) {
                val uploadFile: UploadFile? = multipartFile?.let { storeFile(it) }
                uploadFile?.let { storeFileResult.add(it) }
            }
        }
        return storeFileResult
    }

    fun storeFile(multipartFile: MultipartFile): UploadFile? {
        if(multipartFile.isEmpty) {
            return null
        }

        val originalFilename: String = multipartFile.originalFilename?: ""
        val storeFileName: String = createStoreFileName(originalFilename)
        multipartFile.transferTo(File(getFullPath(storeFileName)))

        return UploadFile(originalFilename, storeFileName)
    }

    private fun createStoreFileName(originalFilename: String): String {
        val uuid: String = UUID.randomUUID().toString()
        val ext = extracted(originalFilename)
        return "$uuid.$ext"
    }

    private fun extracted(originalFilename: String): String {
        val pos: Int = originalFilename.lastIndexOf(".")
        return originalFilename.substring(pos + 1)
    }
}