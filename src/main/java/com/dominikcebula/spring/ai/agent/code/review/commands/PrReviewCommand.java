package com.dominikcebula.spring.ai.agent.code.review.commands;

import com.dominikcebula.spring.ai.agent.code.review.agent.AgentService;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrData;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrDiffChunk;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrLinkParser;
import com.dominikcebula.spring.ai.agent.code.review.utils.StringSplitter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.LinkedList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Command(name = "pr-review", mixinStandardHelpOptions = true)
@Slf4j
@RequiredArgsConstructor
public class PrReviewCommand implements Runnable {

    @Value("${spring.ai.bedrock.converse.chat.options.max-tokens}")
    private int maxPatchSize;

    @Option(names = "-pr-link", description = "PR Link", required = true)
    private String prLink;

    private final AgentService agentService;

    @SneakyThrows
    @Override
    public void run() {
        log.info("Executing pr review for pull request: {}", prLink);

        GitHub github = GitHubBuilder.fromEnvironment().build();

        PrData prData = PrLinkParser.parse(prLink);
        log.info("Parsed PR Data from pull request link: {}", prData);

        log.info("Fetching pull request...");
        GHPullRequest pullRequest = github.getUser(prData.organization())
                .getRepository(prData.repository()).getPullRequest(prData.pullRequestNumber());
        log.info("Pull request found: {}", pullRequest);

        log.info("Fetching pull request diff...");
        List<PrDiffChunk> prDiffChunks = getPrDiffChunks(pullRequest);
        log.info("Pull request diff found");

        log.info("Reviewing pull request...");
        for (int i = 0; i < prDiffChunks.size(); i++) {
            PrDiffChunk prDiffChunk = prDiffChunks.get(i);
            log.info("Reviewing diff chunk [{}/{}]", i + 1, prDiffChunks.size());
            String feedback = agentService.reviewPr(prDiffChunk.patch());
            System.out.println(feedback);
        }
    }

    private List<PrDiffChunk> getPrDiffChunks(GHPullRequest pullRequest) {
        List<PrDiffChunk> prDiffChunks = new LinkedList<>();
        for (GHPullRequestFileDetail prFileDetail : pullRequest.listFiles()) {
            String patch = prFileDetail.getPatch();

            if (patch.getBytes(UTF_8).length > maxPatchSize)
                StringSplitter.splitByBytes(patch, maxPatchSize, UTF_8);
            else
                prDiffChunks.add(new PrDiffChunk(prFileDetail.getFilename(), patch));
        }
        return prDiffChunks;
    }
}
