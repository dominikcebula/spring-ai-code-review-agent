package com.dominikcebula.spring.ai.agent.code.review.agent;

import com.dominikcebula.spring.ai.agent.code.review.utils.ResourceReader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class AgentService {
    private final ChatClient chatClient;

    public AgentService(ChatClient.Builder chatClientBuilder, ResourceLoader resourceLoader) {
        this.chatClient = chatClientBuilder
                .defaultSystem(ResourceReader.asString(
                        resourceLoader.getResource("classpath:agent.md")
                ))
                .build();
    }

    public String reviewPr(String prDiff) {
        return chatClient.prompt()
                .user("Please review the following PR diff and provide feedback:\n\n\n" + prDiff)
                .call()
                .content();
    }
}
