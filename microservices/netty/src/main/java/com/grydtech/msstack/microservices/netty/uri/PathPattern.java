package com.grydtech.msstack.microservices.netty.uri;

import javax.ws.rs.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class parses the {@link Path} annotations, extract their path values, and generates regular expressions
 * that matches all URIs targeting this {@link Path}. Using this class, a URI can be determined be either matching
 * or not matching the given path.
 */
public final class PathPattern {

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{(\\w[\\-.\\w]*\\s*)(:(.+?))?}");
    private static final String F_SLASH_STR = "/";
    private static final String PATH_PART_STR = "[^\\/]+";
    private static final String UNSAFE_REGEX = "[^\\w]";

    private List<String> paramNames = new ArrayList<>();
    private Pattern pattern;

    private PathPattern() {

    }

    private PathPattern(Pattern pattern, List<String> paramNames) {
        this.pattern = pattern;
        this.paramNames = paramNames;
    }

    public static PathPattern fromAnnotatedPath(String annotatedPath) {
        return new PathPattern().setAnnotatedPath(annotatedPath);
    }

    private PathPattern setAnnotatedPath(String annotatedPath) {
        // Canonicalize Path Value. Resulting path will look like "/path/to/foo"
        String canonicalPath = String.format("/%s/", annotatedPath).replaceAll("/+", F_SLASH_STR);
        canonicalPath = canonicalPath.substring(1, canonicalPath.length() - 1);

        // Build a Regex from path
        StringBuilder pathRegexBuilder = new StringBuilder();
        Arrays.stream(canonicalPath.split(F_SLASH_STR)).forEach(part -> {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathPattern that = (PathPattern) o;

        if (!paramNames.equals(that.paramNames)) return false;
        return pattern.equals(that.pattern);
    }

    @Override
    public int hashCode() {
        int result = paramNames.hashCode();
        result = 31 * result + pattern.hashCode();
        return result;
    }

    /**
     * Checks whether a given URI matches this {@link PathPattern}.
     * If it matches, a {@link PathMatch} object is returned. Else null is returned.
     *
     * @param path The URI to match this {@link PathPattern} against
     * @return A {@link PathMatch} object or null, depending on whether the URI matched or not.
     */
    public PathMatch getPathMatch(String path) {
        final Matcher pathMatcher = pattern.matcher(path);
        if (pathMatcher.matches()) {
            final Map<String, List<String>> matchedParams = new HashMap<>();
            for (String paramName : paramNames) {
                final String regexSafeParamName = paramName.replaceAll(UNSAFE_REGEX, "");
                matchedParams.put(paramName, Collections.singletonList(pathMatcher.group(regexSafeParamName)));
            }
            return new PathMatch(matchedParams);
        }
        return null;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
