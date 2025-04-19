package com.example.ai;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }

    @Bean
    McpSyncClient mcpSyncClient() {
        var sseClientTransport = HttpClientSseClientTransport
                .builder("http://localhost:8081")
                .build();
        var mcp = McpClient
                .sync(sseClientTransport)
                .build();
        mcp.initialize();
        return mcp;
    }
}

@Controller
@ResponseBody
class AssistantController {

    private final Map<String, PromptChatMemoryAdvisor> memory = new ConcurrentHashMap<>();

    private final ChatClient ai;


    AssistantController(
            JdbcClient db,
            ChatClient.Builder ai,
            DogRepository repository,
            VectorStore vectorStore,
            McpSyncClient mcpSyncClient) {

        if (db.sql("select count(id) from vector_store").query(Integer.class)
                .single().equals(0)) {
            repository.findAll().forEach(d -> {
                var dogument = new Document("id: %s, name: %s, description: %s"
                        .formatted(d.id(), d.name(), d.description()));
                vectorStore.accept(List.of(dogument));
            });
        }
        var system = """
                You are an AI powered assistant to help people adopt a dog from the adoption\s
                agency named Pooch Palace with locations in Antwerp, Seoul, Tokyo, Singapore, Paris,\s
                Mumbai, New Delhi, Barcelona, San Francisco, and London. Information about the dogs available\s
                will be presented below. If there is no information, then return a polite response suggesting we\s
                don't have any dogs available.
                """;

//        this.dogAdoptionScheduler = dogAdoptionScheduler;
        this.ai = ai
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .defaultSystem(system)
                .defaultTools(new SyncMcpToolCallbackProvider(mcpSyncClient))
                .build();
    }

    @PostMapping("/{user}/inquire")
    String inquire(@PathVariable String user, @RequestParam String question) {

        var advisor = this.memory.computeIfAbsent(user, _ -> PromptChatMemoryAdvisor
                .builder(new InMemoryChatMemory())
                .build());

        return this.ai
                .prompt()
                .user(question)
                .advisors(advisor)
                .call()
                .content();
    }
}

record Dog(@Id int id, String name, String owner, String description) {
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {
}

