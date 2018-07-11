package com.grydtech.msstack.request.netty.uri;

import com.grydtech.msstack.request.netty.routing.MethodWrapper;
import com.grydtech.msstack.request.netty.util.UriUtils;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class parses a parametrized URI, and generates a regex that matches all URIs targeting this {@link Path}.
 * An instance of this class can be evaluated with a URI to check if it matches or not.
 * <p>
 * TODO This class should ideally support URIs such as http://dev.brandstore.com/inventory/grocery;type=fruits/price;range=300/?offset=5&limit=10
 */
public final class Endpoint {

    private static final Pattern PATHPARAM_PATTERN = Pattern.compile("\\{(\\w[\\-.\\w]*\\s*)(:(.+?))?}");
    private static final String PATH_PART = "[^/]+";
    private static final String UNSAFE_CHARS = "[^\\w]";

    private List<String> paramIds = new ArrayList<>();

    private Pattern pattern;
    private MethodWrapper method;

    public Endpoint(String path, MethodWrapper method) {
        this.compilePath(path);
        this.method = method;
    }

    private void compilePath(String path) {
        String canonicalPath = UriUtils.canonicalize(path);

        // Build a Regex from pattern
        StringBuilder pathRegexBuilder = new StringBuilder();
        Arrays.stream(canonicalPath.split("/")).forEach(part -> {
            pathRegexBuilder.append("/");
            final Matcher paramMatcher = PATHPARAM_PATTERN.matcher(part);
            if (paramMatcher.matches()) {
                // Param name
                final String paramId = paramMatcher.group(1);
                final String safeParamId = paramId.replaceAll(UNSAFE_CHARS, "");
                // Pattern either matches given regex, or PATH_PART (not type-restricted)
                final String paramPattern = String.format(
                        "(?<%s>%s)",
                        safeParamId,
                        paramMatcher.group(3) != null ? paramMatcher.group(3) : PATH_PART
                );
                // Add Parameter Name to List
                paramIds.add(paramId);
                pathRegexBuilder.append(paramPattern);
            } else {
                pathRegexBuilder.append(part);
            }
        });

        // Update the Pattern
        this.pattern = Pattern.compile(pathRegexBuilder.toString());
    }

    /**
     * Checks whether a given URI matches this {@link Endpoint}.
     * If it matches, a {@link EndpointMatch} object is returned. Else null is returned.
     *
     * @param path The URI to match this {@link Endpoint} against
     * @return A {@link EndpointMatch} object or null, depending on whether the URI matched or not.
     */
    public Optional<EndpointMatch> match(String path) {
        final String[] sections = path.split("\\?", 2);
        // Process the Path section of URI
        final String pathSection = sections[0];
        final Matcher pathSectionMatcher = this.pattern.matcher(pathSection);
        if (pathSectionMatcher.matches()) {
            final Map<String, String> matchedParams = new HashMap<>();
            // For all Params defined in the Endpoint, assign values from Path
            for (String paramName : paramIds) {
                final String regexSafeParamName = paramName.replaceAll(UNSAFE_CHARS, "");
                // TODO Check if the section contains matrix parameters (later)
                matchedParams.put(paramName, pathSectionMatcher.group(regexSafeParamName));
            }
            return Optional.of(new EndpointMatch(matchedParams));
        }
        return Optional.empty();
    }

    public List<String> getParamIds() {
        return paramIds;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public MethodWrapper getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint = (Endpoint) o;

        if (paramIds != null ? !paramIds.equals(endpoint.paramIds) : endpoint.paramIds != null)
            return false;
        return pattern != null ? pattern.equals(endpoint.pattern) : endpoint.pattern == null;
    }

    @Override
    public int hashCode() {
        int result = paramIds != null ? paramIds.hashCode() : 0;
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "paramIds=" + paramIds +
                ", pattern=" + pattern +
                '}';
    }
}
