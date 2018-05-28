package com.grydtech.msstack.microservices.netty.uri;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class parses the {@link Path} annotations, extract their path values, and generates regular expressions
 * that matches all URIs targeting this {@link Path}. Using this class, a URI can be determined be either matching
 * or not matching the given path.
 * <p>
 * TODO This class should ideally support URIs such as http://dev.brandstore.com/inventory/grocery;type=fruits/price;range=300/?offset=5&limit=10
 */
public final class PathMatcher {

    private static final Pattern PATHPARAM_PATTERN = Pattern.compile("\\{(\\w[\\-.\\w]*\\s*)(:(.+?))?}");
    private static final String F_SLASH_STR = "/";
    private static final String PATH_PART_STR = "[^/]+";
    private static final String UNSAFE_REGEX = "[^\\w]";

    private List<String> pathParamNames = new ArrayList<>();
    private Pattern pathPattern;

    private PathMatcher() {

    }

    public static PathMatcher fromAnnotatedPath(String annotatedPath) {
        return new PathMatcher().setAnnotatedPath(annotatedPath);
    }

    private PathMatcher setAnnotatedPath(String annotatedPath) {
        // Canonicalize Path Value. Resulting path will look like "/path/to/foo"
        // No need to consider query parameters, since this method is used only for endpoints defined in code
        String canonicalPath = String.format("/%s/", annotatedPath).replaceAll("/+", F_SLASH_STR);
        canonicalPath = canonicalPath.substring(1, canonicalPath.length() - 1);

        // Build a Regex from path
        StringBuilder pathRegexBuilder = new StringBuilder();
        Arrays.stream(canonicalPath.split(F_SLASH_STR)).forEach(part -> {
            pathRegexBuilder.append(F_SLASH_STR);
            final Matcher paramMatcher = PATHPARAM_PATTERN.matcher(part);
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
                pathParamNames.add(paramName);
                pathRegexBuilder.append(paramPattern);
            } else {
                pathRegexBuilder.append(part);
            }
        });

        // Update the Pattern
        this.pathPattern = Pattern.compile(pathRegexBuilder.toString());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathMatcher that = (PathMatcher) o;

        if (!pathParamNames.equals(that.pathParamNames)) {
            return false;
        }
        return pathPattern.equals(that.pathPattern);
    }

    @Override
    public int hashCode() {
        int result = pathParamNames.hashCode();
        result = 31 * result + pathPattern.hashCode();
        return result;
    }

    /**
     * Checks whether a given URI matches this {@link PathMatcher}.
     * If it matches, a {@link PathParameterMatch} object is returned. Else null is returned.
     *
     * @param path The URI to match this {@link PathMatcher} against
     * @return A {@link PathParameterMatch} object or null, depending on whether the URI matched or not.
     */
    public PathParameterMatch getPathParameterMatch(String path) {
        final String[] sections = path.split("\\?", 2);
        // Process the Path section of URI
        final String pathSection = sections[0];
        final Matcher pathSectionMatcher = pathPattern.matcher(pathSection);
        if (pathSectionMatcher.matches()) {
            final Map<String, String> matchedParams = new HashMap<>();
            // For all Params defined in the PathMatcher, assign values from Path
            for (String paramName : pathParamNames) {
                final String regexSafeParamName = paramName.replaceAll(UNSAFE_REGEX, "");
                // TODO Check if the section contains matrix parameters (later)
                matchedParams.put(paramName, pathSectionMatcher.group(regexSafeParamName));
            }
            return new PathParameterMatch(matchedParams);
        }
        return null;
    }

    public List<String> getPathParamNames() {
        return pathParamNames;
    }

    public Pattern getPathPattern() {
        return pathPattern;
    }
}
