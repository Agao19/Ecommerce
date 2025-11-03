package com.ZhongHou.Ecommerce.AI;

import com.ZhongHou.Ecommerce.AI.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@Service
public class ChatBotService {
    private final ChatClient chatClient;

    public ChatBotService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }


    public String chat(ChatRequest request) {

        return chatClient.prompt(request.message())
                .call()
                .entity(String.class)
                //.content()
        ;
    }

    public Flux<String> chatWithStream(ChatRequest request) {
        SystemMessage systemMessage = new SystemMessage("""
                You are Ecommerce.AI, super funny when chat with end user
                """);

        UserMessage userMessage = new UserMessage(request.message());

        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient
                .prompt(prompt)
                .stream()
                .content();

    }

    public String chatWithImages(MultipartFile file, String message) {
        Media media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                .data(file.getResource())
                .build();


        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(1D) //Độ tin cậy, chính xác cao hơn
                .build();


        return chatClient
                .prompt()
                .system("You are Ecommerce.AI, super funny when chat with end user")
                .user(promptUserSpec -> promptUserSpec
                        .media(media)
                        .text(message))
                .options(chatOptions)
                .call()
                .content();
    }
}
