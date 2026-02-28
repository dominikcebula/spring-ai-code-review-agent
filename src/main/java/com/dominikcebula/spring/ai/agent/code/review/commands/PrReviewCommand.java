package com.dominikcebula.spring.ai.agent.code.review.commands;

import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrData;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrLinkParser;
import com.dominikcebula.spring.ai.agent.code.review.pullrequest.PrReviewService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "pr-review", mixinStandardHelpOptions = true)
@Slf4j
@RequiredArgsConstructor
public class PrReviewCommand implements Runnable {
    @Option(names = "-pr-link", description = "PR Link", required = true)
    private String prLink;

    private final PrReviewService prReviewService;

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

        log.info("Reviewing pull request...");
        prReviewService.review(pullRequest);
        log.info("Pull request reviewed");
    }
}
