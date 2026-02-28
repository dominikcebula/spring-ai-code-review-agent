package com.dominikcebula.spring.ai.agent.code.review.pullrequest.review;

import com.dominikcebula.spring.ai.agent.code.review.agent.AgentService;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff.PrDiffChunk;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff.PrDiffChunkReview;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff.PrDiffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrReviewService {
    private final AgentService agentService;
    private final PrDiffService prDiffService;
    private final PrCommentUploadService prCommentUploadService;

    public void review(GHPullRequest pullRequest) {
        log.info("Fetching pull request diff...");
        List<PrDiffChunk> prDiffChunks = prDiffService.getPrDiffChunks(pullRequest);
        log.info("Pull request diff found");

        log.info("Reviewing pull request chunks...");
        for (int i = 0; i < prDiffChunks.size(); i++) {
            PrDiffChunk prDiffChunk = prDiffChunks.get(i);
            log.info("Reviewing diff chunk [{}/{}]...", i + 1, prDiffChunks.size());
            String feedback = agentService.reviewPr(prDiffChunk.patch());
            log.info("Feedback for diff chunk provided.");

            log.info("Parsing feedback for diff chunk...");
            PrDiffChunkReview prDiffReview = PrReviewSchema.EXTRACTION_CONVERTER.convert(feedback);
            log.info("Feedback for diff chunk parsed.");

            log.info("Uploading comments for diff chunk...");
            prCommentUploadService.upload(pullRequest, prDiffChunk, prDiffReview);
            log.info("Comments uploaded.");
        }
        log.info("Pull request chunks reviewed");
    }
}
