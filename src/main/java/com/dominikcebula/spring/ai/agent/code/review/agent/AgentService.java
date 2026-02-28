package com.dominikcebula.spring.ai.agent.code.review.agent;

import com.dominikcebula.spring.ai.agent.code.review.utils.ResourceReader;
import lombok.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import static com.dominikcebula.spring.ai.agent.code.review.pullrequest.review.PrReviewSchema.EXTRACTION_CONVERTER;

@Service
public class AgentService {
    private final ChatClient chatClient;

    public AgentService(ChatClient.Builder chatClientBuilder, ResourceLoader resourceLoader) {
        this.chatClient = chatClientBuilder
                .defaultSystem(getSystemPrompt(resourceLoader))
                .build();
    }

    private static @NonNull String getSystemPrompt(ResourceLoader resourceLoader) {
        String agentDefinition = ResourceReader.asString(resourceLoader.getResource("classpath:agent.md"));
        agentDefinition = agentDefinition.replace("{{RESPONSE_SCHEMA}}", EXTRACTION_CONVERTER.getJsonSchema());
        return agentDefinition;
    }

    public String reviewPr(String prDiff) {
        return chatClient.prompt()
                .user("Please review the following PR diff and provide feedback:\n\n\n" + prDiff)
                .call()
                .content();
    }
}
