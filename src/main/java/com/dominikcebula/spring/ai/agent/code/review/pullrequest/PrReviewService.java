package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

import com.dominikcebula.spring.ai.agent.code.review.agent.AgentService;
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

    public void review(GHPullRequest pullRequest) {
        log.info("Fetching pull request diff...");
        List<PrDiffChunk> prDiffChunks = prDiffService.getPrDiffChunks(pullRequest);
        log.info("Pull request diff found");

        log.info("Reviewing pull request chunks...");
        for (int i = 0; i < prDiffChunks.size(); i++) {
            PrDiffChunk prDiffChunk = prDiffChunks.get(i);
            log.info("Reviewing diff chunk [{}/{}]", i + 1, prDiffChunks.size());
            String feedback = agentService.reviewPr(prDiffChunk.patch());
            System.out.println(feedback);
        }
        log.info("Pull request chunks reviewed");
    }
}
