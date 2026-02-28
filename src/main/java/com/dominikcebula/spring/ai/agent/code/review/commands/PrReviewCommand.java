package com.dominikcebula.spring.ai.agent.code.review.commands;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "pr-review", mixinStandardHelpOptions = true)
@Slf4j
public class PrReviewCommand implements Runnable {

    @Option(names = "-pr-link", description = "PR Link", required = false)
    private String prLink;

    @SneakyThrows
    @Override
    public void run() {
        log.info("Executing pr review command");

        GitHub github = GitHubBuilder.fromEnvironment().build();


        System.out.println(github.getMyself().getPublicRepoCount());
    }
}
