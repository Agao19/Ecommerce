package com.ZhongHou.Ecommerce.AI;

import com.ZhongHou.Ecommerce.AI.VectorStoge.MedataFiltering;
import com.ZhongHou.Ecommerce.AI.VectorStoge.ProductIngestionService;
import com.ZhongHou.Ecommerce.AI.dto.ChatRequest;
import com.ZhongHou.Ecommerce.AI.dto.RagRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;
    private final ProductIngestionService productIngestionService;
    private final MedataFiltering medataFiltering;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return chatBotService.chat(request);
    }

    //Streaming
    @GetMapping("/stream")
    public Flux<String> chatWithStream(@RequestBody ChatRequest request) {
        return chatBotService.chatWithStream(request);
    }


    @PostMapping("/chat-with-images")
    public String chatWithImages(@RequestParam("file") MultipartFile file,
                                 @RequestParam("message") String message) {
        return chatBotService.chatWithImages(file, message);
    }

    @PostMapping("/chat-memory")
    public String chatMemory(@RequestBody ChatRequest request) {
        return chatBotService.chatWithMemory(request);
    }

    // ========== RAG API  ==========

    @PostMapping("/rag/ingest")
    public ResponseEntity<String> ingestAllProducts() {
        try {
            productIngestionService.ingestAllProducts();
            return ResponseEntity.ok("Đã ingest tất cả products vào VectorStore thành công!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi ingest: " + e.getMessage());
        }
    }

    @PostMapping("/rag/reindex")
    public ResponseEntity<String> reIndexAllProducts() {
        try {
            productIngestionService.reIndexAllProducts();
            return ResponseEntity.ok("Đã re-index tất cả products thành công!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi re-index: " + e.getMessage());
        }
    }

    @PostMapping("/rag/query")
    public ResponseEntity<String> ragQuery(@RequestBody RagRequest request) {
        try {
            String answer = medataFiltering.ragQuery(
                    request.query(),
                    request.categoryId(),
                    request.priceRange()
            );
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi query RAG: " + e.getMessage());
        }
    }

    @PostMapping("/rag/query-simple")
    public ResponseEntity<String> ragQuerySimple(@RequestBody RagRequest request) {
        try {
            String answer = medataFiltering.ragQuery(
                    request.query(),
                    null,  // No category filter
                    null            // No price filter
            );
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Lỗi khi query RAG: " + e.getMessage());
        }
    }
}
