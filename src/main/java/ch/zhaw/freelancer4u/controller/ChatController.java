package ch.zhaw.freelancer4u.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.zhaw.freelancer4u.tools.FreelancerTools;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;
    private final FreelancerTools freelancerTools;

    @Autowired
    public ChatController(ChatClient chatClient, FreelancerTools freelancerTools) {
        this.chatClient = chatClient;
        this.freelancerTools = freelancerTools;
    }

    public record ChatRequest(String message) {}
    public record ChatResponseDto(String reply) {}

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> chat(@RequestBody ChatRequest request) {

        if (request == null || request.message() == null || request.message().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String systemPrompt = """
                Du bist ein hilfreicher Chatbot für die Plattform Freelancer4u.
                Du kennst die Datenbank mit Jobs und Companies und kannst mit Tools
                (FreelancerTools) neue Companies und Jobs anlegen.
                Antworte immer auf Deutsch, kurz und verständlich.
                """;

        String answer = chatClient
                .prompt(systemPrompt)
                .tools(freelancerTools)
                .user(request.message())
                .call()
                .content();

        return ResponseEntity.ok(new ChatResponseDto(answer));
    }
}
