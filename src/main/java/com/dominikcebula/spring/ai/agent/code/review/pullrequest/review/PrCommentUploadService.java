package com.dominikcebula.spring.ai.agent.code.review.pullrequest.review;

import com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff.PrDiffChunk;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff.PrDiffChunkReview;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class PrCommentUploadService {
    public void upload(GHPullRequest pullRequest, PrDiffChunk prDiffChunk, PrDiffChunkReview prDiffReview) {
        for (int i = 0; i < prDiffReview.comments().size(); i++) {
            log.info("Uploading comment [{}/{}]...", i + 1, prDiffReview.comments().size());
            PrComment prComment = prDiffReview.comments().get(i);
            uploadComment(pullRequest, prDiffChunk, prComment);

            log.info("Comment uploaded.");
        }
    }

    private void uploadComment(GHPullRequest pullRequest, PrDiffChunk prDiffChunk, PrComment prComment) {
        try {
            pullRequest.createReviewComment()
                    .commitId(pullRequest.getHead().getSha())
                    .path(prDiffChunk.filename())
                    .lines(prComment.startLine(), prComment.endLine())
                    .body(
                            """
                                    Priority: %s
                                    Category: %s
                                    
                                    %s
                                    """.formatted(prComment.priority(), prComment.category(), prComment.body())
                    ).create();
        } catch (IOException e) {
            log.error("Failed to upload comment.", e);
        }
    }
}
