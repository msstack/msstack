package com.grydtech.msstack.microservices.netty.uri;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PathPattern {

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{(\\w[\\-.\\w]*\\s*)(:(.+?))?}");
    private static final String F_SLASH_STR = "/";
    private static final String PATH_PART_STR = "[^\\/]+";
    private static final String UNSAFE_REGEX = "[^\\w]";

    private List<String> paramNames = new ArrayList<>();
    private Pattern pattern;

    private PathPattern() {

    }

    public static PathPattern fromAnnotatedPath(String annotatedPath) {
        return new PathPattern().setAnnotatedPath(annotatedPath);
    }

    private PathPattern setAnnotatedPath(String path) {
        // Canonicalize the Path Value.
        // Resulting path will look like "/path/to/foo"
        path = String.format("/%s/", path);
        path = path.replaceAll("/+", F_SLASH_STR);
        path = path.substring(1, path.length() - 1);

        // Build a Regex from path
        StringBuilder pathRegexBuilder = new StringBuilder();
        Arrays.stream(path.split(F_SLASH_STR)).forEach(part -> {
            pathRegexBuilder.append(F_SLASH_STR);
            final Matcher paramMatcher = PARAM_PATTERN.matcher(part);
            if (paramMatcher.matches()) {
                // Param name
                final String paramName = paramMatcher.group(1);
                final String regexSafeParamName = paramName.replaceAll(UNSAFE_REGEX, "");
                // Pattern either matches given regex, or PATH_PART_STR (not type-restricted)
                final String paramPattern = String.format(
                        "(?<%s>%s)",
                        regexSafeParamName,
                        paramMatcher.group(3) != null ? paramMatcher.group(3) : PATH_PART_STR
                );
                // Add Parameter Name to List
                paramNames.add(paramName);
                pathRegexBuilder.append(paramPattern);
            } else {
                pathRegexBuilder.append(part);
            }
        });

        // Update the Pattern
        this.pattern = Pattern.compile(pathRegexBuilder.toString());
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PathPattern) {
            return ((PathPattern) obj).pattern.equals(this.pattern);
        }
        return false;
    }

    public PathMatch getPathMatch(String path) {
        final Matcher pathMatcher = pattern.matcher(path);
        if (pathMatcher.matches()) {
            final Map<String, String> paramMatches = new HashMap<>();
            for (String paramName : paramNames) {
                final String regexSafeParamName = paramName.replaceAll(UNSAFE_REGEX, "");
                paramMatches.put(paramName, pathMatcher.group(regexSafeParamName));
            }
            return new PathMatch().setPath(path).setParamMatches(paramMatches);
        }
        return null;
    }
}
