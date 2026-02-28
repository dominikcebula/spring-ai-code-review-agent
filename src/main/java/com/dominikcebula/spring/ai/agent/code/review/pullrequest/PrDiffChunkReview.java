package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

import java.util.List;

public record PrDiffChunkReview(List<PrComment> comments) {
}
