package hello.upload.controller

import hello.upload.domain.Item
import hello.upload.domain.ItemRepository
import hello.upload.domain.UploadFile
import hello.upload.file.FileStore
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

@Controller
class ItemController (
    private val itemRepository: ItemRepository,
    private val fileStore: FileStore
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/items/new")
    fun newItem(@ModelAttribute form: ItemForm): String {
        return "item-form"
    }

    @PostMapping("/items/new")
    fun saveItem(@ModelAttribute form: ItemForm, redirectAttributes: RedirectAttributes): String {
        val attachFile: UploadFile? = form.attachFile?.let { fileStore.storeFile(it) }
        val storeImageFile: List<UploadFile>? = form.imageFiles?.let { fileStore.storeFiles(it) }

        val item = Item(
            itemName = form.itemName,
            attachFile = attachFile,
            imageFiles = storeImageFile
        )
        itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", item.id)
        return "redirect:/items/{itemId}"
    }

    @GetMapping("/items/{id}")
    fun items(@PathVariable id: Long, model: Model): String {
        val item: Item? = itemRepository.findById(id)
        model.addAttribute("item", item)
        return "item-view"
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    fun downloadImage(@PathVariable filename: String): Resource {
        return UrlResource("file:" + fileStore.getFullPath(filename))
    }

    @GetMapping("/attach/{itemId}")
    fun downloadAttach(@PathVariable itemId: Long): ResponseEntity<Resource> {
        val item: Item? = itemRepository.findById(itemId)
        val storeFileName: String? = item?.attachFile?.storeFileName
        val uploadFileName: String? = item?.attachFile?.uploadFileName

        val resource = UrlResource("file:" + fileStore.getFullPath(storeFileName))

        log.info("uploadFileName={}", uploadFileName)

        val encodedUploadFileName: String? = uploadFileName?.let { UriUtils.encode(it, StandardCharsets.UTF_8) }
        var contentDisposition: String = "attachment; filename=\"$encodedUploadFileName\""

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource)
    }

}