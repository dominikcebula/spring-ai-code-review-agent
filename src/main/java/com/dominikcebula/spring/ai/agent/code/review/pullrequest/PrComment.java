package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

public record PrComment(String priority, String category, String path, int startLine, int endLine, String body) {
}
