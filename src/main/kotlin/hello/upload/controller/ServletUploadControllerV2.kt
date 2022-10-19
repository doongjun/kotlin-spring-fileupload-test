package hello.upload.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.util.StreamUtils
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.InputStream
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/servlet/v2")
class ServletUploadControllerV2 {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${file.dir}")
    private val fileDir: String? = null

    @GetMapping("/upload")
    fun newFile(): String {
        return "upload-form"
    }

    @PostMapping("/upload")
    fun saveFileV1(request: HttpServletRequest): String {
        log.info("request={}", request)

        val itemName = request.getParameter("itemName")
        log.info("itemName={}", itemName)

        val parts = request.getParts()
        log.info("parts={}", parts)

        for (part in parts) {
            log.info("==== PART ====")
            log.info("name = {}", part.name)
            val headerNames = part.headerNames
            for(headerName in headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName))
            }
            //편의 메서드
            //content-disposition; filename
            log.info("submittedFilename={}", part.submittedFileName)
            log.info("size={}", part.size)

            //데이터 읽기
            val inputStream: InputStream = part.inputStream
            val body: String = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
            log.info("body={}", body)

            if(StringUtils.hasText(part.submittedFileName)) {
                val fullPath: String = fileDir + part.submittedFileName
                log.info("파일 저장 fullPath={}", fullPath)
                part.write(fullPath)
            }
        }

        return "upload-form"
    }

}