package com.dominikcebula.spring.ai.agent.code.review.pullrequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrLinkParser {
    private static final Pattern PR_PATTERN = Pattern.compile(
            "^https://.*/(?<org>[^/]+)/(?<repo>[^/]+)/pull/(?<number>\\d+)/?$"
    );

    public static PrData parse(String url) {
        Matcher matcher = PR_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid GitHub pull request URL: " + url);
        }

        return new PrData(
                matcher.group("org"),
                matcher.group("repo"),
                Integer.parseInt(matcher.group("number"))
        );
    }
}
