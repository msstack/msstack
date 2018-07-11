package com.grydtech.msstack.request.netty.util;

public final class UriUtils {

    private static final String F_SLASHES = "/+";
    private static final String F_SLASH = "/";
    private static final String TRIMMED_SLASHES = "^/+|/+$";
    private static final String EMPTY = "";

    private UriUtils() {

    }

    /**
     * Canonicalize a URI.
     * Resulting path will look like "path/to/foo" (no leading/trailing slashes).
     *
     * @param path Path to canonicalize (String) (Only path should be sent, no query parameters)
     * @return Canonical Path (String)
     */
    public static String canonicalize(String path) {
        return String.format("/%s/", path).replaceAll(F_SLASHES, F_SLASH).replaceAll(TRIMMED_SLASHES, EMPTY);
    }
}
