package com.dominikcebula.spring.ai.agent.code.review.commands;

import com.dominikcebula.spring.ai.agent.code.review.agent.AgentService;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrData;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrLinkParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "pr-review", mixinStandardHelpOptions = true)
@Slf4j
public class PrReviewCommand implements Runnable {

    @Option(names = "-pr-link", description = "PR Link", required = true)
    private String prLink;

    @Autowired
    private AgentService agentService;

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
        String prDiff = getPrDiff(pullRequest);
        log.info("Pull request diff found");

        log.info("Reviewing pull request...");
        String feedback = agentService.reviewPr(prDiff);
        log.info("Pull request reviewed: {}", feedback);
    }

    private String getPrDiff(GHPullRequest pullRequest) {
        StringBuilder sb = new StringBuilder();
        for (GHPullRequestFileDetail prFileDetail : pullRequest.listFiles()) {
            sb.append("-----\n");
            sb.append(">>> filename: ");
            sb.append(prFileDetail.getFilename());
            sb.append("\n");
            sb.append(prFileDetail.getPatch());
            sb.append("\n");
        }
        return sb.toString();
    }
}
