package com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff;

import com.dominikcebula.spring.ai.agent.code.review.pullrequest.review.PrComment;

import java.util.List;

public record PrDiffChunkReview(List<PrComment> comments) {
}
