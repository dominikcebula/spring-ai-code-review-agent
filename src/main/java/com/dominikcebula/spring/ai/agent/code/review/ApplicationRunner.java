package com.dominikcebula.spring.ai.agent.code.review;

import com.dominikcebula.spring.ai.agent.code.review.commands.PrReviewCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component
public class ApplicationRunner implements CommandLineRunner, ExitCodeGenerator {

    private final PrReviewCommand prReviewCommand;

    private final IFactory factory;

    private int exitCode;

    public ApplicationRunner(PrReviewCommand prReviewCommand, IFactory factory) {
        this.prReviewCommand = prReviewCommand;
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(prReviewCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
