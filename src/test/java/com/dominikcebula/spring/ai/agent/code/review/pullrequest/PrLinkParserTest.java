package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrLinkParserTest {
    @Test
    void shouldParsePrLink() {
        PrData prData = PrLinkParser.parse("https://github.com/dominikcebula/spring-ai-code-review-agent/pull/1");

        assertThat(prData.organization()).isEqualTo("dominikcebula");
        assertThat(prData.repository()).isEqualTo("spring-ai-code-review-agent");
        assertThat(prData.pullRequestNumber()).isEqualTo(1);
    }
}
