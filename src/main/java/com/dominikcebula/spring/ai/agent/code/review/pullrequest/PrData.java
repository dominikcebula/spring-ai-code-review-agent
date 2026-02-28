package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

public record PrData(String organization, String repository, int pullRequestNumber) {
}
