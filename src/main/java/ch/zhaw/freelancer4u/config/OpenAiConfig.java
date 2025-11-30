package ch.zhaw.freelancer4u.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Autowired
    private OpenAiChatModel chatModel;

    @Bean
    public ChatClient chatClient() {
        // einfacher ChatClient ohne Advisors
        return ChatClient.builder(chatModel).build();
    }
}
