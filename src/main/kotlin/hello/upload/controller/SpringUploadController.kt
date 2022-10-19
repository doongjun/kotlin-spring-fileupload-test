package hello.upload.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/spring")
class SpringUploadController {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${file.dir}")
    private val fileDir: String? = null

    @GetMapping("/upload")
    fun newFile(): String {
        return "upload-form"
    }

    @PostMapping("/upload")
    fun saveFile(@RequestParam itemName: String,
                 @RequestParam file: MultipartFile, request: HttpServletRequest): String {
        log.info("request={}", request)
        log.info("itemName={}", itemName)
        log.info("multipartFile={}", file)

        if(!file.isEmpty) {
            var fullPath: String = fileDir + file.originalFilename
            log.info("파일 저장 fullPath={}", fullPath)
            file.transferTo(File(fullPath))
        }
        return "upload-form"
    }
}