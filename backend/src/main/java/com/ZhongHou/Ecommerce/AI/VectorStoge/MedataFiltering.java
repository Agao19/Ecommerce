package com.ZhongHou.Ecommerce.AI.VectorStoge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedataFiltering {

    private final VectorStore vectorStore;
    private final ChatClient.Builder chatClientBuilder;

    private final String ragPromptTemplate = """
            Bạn là một trợ lý bán hàng chuyên nghiệp của web thương mại điện tử .
            Nhiệm vụ của bạn là trả lời câu hỏi của người dùng DỰA VÀO 
            THÔNG TIN SẢN PHẨM được cung cấp dưới đây.
            
            Nếu thông tin không có trong bối cảnh, hãy nói "Xin lỗi, tôi không 
            tìm thấy thông tin cho sản phẩm đó."
            
            KHÔNG ĐƯỢC bịa đặt thông tin sản phẩm.
            Trả lời bằng tiếng Việt.
            
            --- BỐI CẢNH SẢN PHẨM ---
            {context}
            --- KẾT THÚC BỐI CẢNH ---
            """;

    public String ragQuery(String query, String categoryId, String priceRange) {
        // 1. Build filter expression từ params

        String filterExpression = buildFilter(categoryId, priceRange);
        log.info("filter expression:{}", filterExpression);

        // 2. Embed query và search trong VectorStore
        List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)  // ← User query được embed tự động
                        .topK(100)
                        .similarityThreshold(0.2)
                        .filterExpression(filterExpression)  // Metadata filter
                        .build()
        );

        // 3. RAG với context từ documents
        String context = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        log.info("Found {} documents", similarDocs.size());

        if (context.isEmpty()) {
            return "Xin lỗi, tôi không tìm thấy sản phẩm nào phù hợp với yêu cầu của bạn (category: "
                    + categoryId + ", priceRange: " + priceRange + ").";
        }

        ChatClient chatClient = chatClientBuilder.build();

        String fullPrompt = ragPromptTemplate.replace("{context}", context);

        return chatClient.prompt()
                .system(fullPrompt) // Gửi "mệnh lệnh" và "bối cảnh"
                .user(query)
                .call()
                .content();

    }


    private String buildFilter(String categoryId, String priceRange) {
        List<String> clauses = new ArrayList<>();

        if (categoryId != null && !categoryId.isBlank()) {
            clauses.add("categoryId == '" + categoryId.trim() + "'");
        }
        if (priceRange != null && !priceRange.isBlank()) {
            clauses.add("priceRange == '" + priceRange.trim() + "'");
        }
        if (clauses.isEmpty()) return null;
        return String.join(" && ", clauses);
    }
}
