package hello.upload.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/servlet/v1")
class ServletUploadControllerV1 {
    private val log = LoggerFactory.getLogger(javaClass)

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

        return "upload-form"
    }

}