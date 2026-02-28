package com.dominikcebula.spring.ai.agent.code.review.pullrequest.diff;

import com.dominikcebula.spring.ai.agent.code.review.utils.StringSplitter;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class PrDiffService {
    @Value("${spring.ai.bedrock.converse.chat.options.max-tokens}")
    private int maxPatchSize;

    public List<PrDiffChunk> getPrDiffChunks(GHPullRequest pullRequest) {
        List<PrDiffChunk> prDiffChunks = new LinkedList<>();
        for (GHPullRequestFileDetail prFileDetail : pullRequest.listFiles()) {
            String patch = prFileDetail.getPatch();

            if (patch.getBytes(UTF_8).length > maxPatchSize)
                for (String chunk : StringSplitter.splitByBytes(patch, maxPatchSize, UTF_8)) {
                    prDiffChunks.add(new PrDiffChunk(prFileDetail.getFilename(), chunk));
                }
            else
                prDiffChunks.add(new PrDiffChunk(prFileDetail.getFilename(), patch));
        }
        return prDiffChunks;
    }
}
