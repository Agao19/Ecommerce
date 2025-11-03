package com.ZhongHou.Ecommerce.AI;

import com.ZhongHou.Ecommerce.AI.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;


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


}
