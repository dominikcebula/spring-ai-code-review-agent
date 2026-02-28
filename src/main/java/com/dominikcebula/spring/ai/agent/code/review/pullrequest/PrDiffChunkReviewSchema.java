package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

import org.springframework.ai.converter.BeanOutputConverter;

public interface PrDiffChunkReviewSchema {
    BeanOutputConverter<PrDiffChunkReview> EXTRACTION_CONVERTER = new BeanOutputConverter<>(PrDiffChunkReview.class);
}
