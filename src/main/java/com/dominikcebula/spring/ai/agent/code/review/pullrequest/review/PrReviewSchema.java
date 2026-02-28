package com.dominikcebula.spring.ai.agent.code.review.pullrequest.review;

import com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff.PrDiffChunkReview;
import org.springframework.ai.converter.BeanOutputConverter;

public interface PrReviewSchema {
    BeanOutputConverter<PrDiffChunkReview> EXTRACTION_CONVERTER = new BeanOutputConverter<>(PrDiffChunkReview.class);
}
