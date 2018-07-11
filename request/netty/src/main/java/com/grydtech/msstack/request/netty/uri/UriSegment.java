package com.grydtech.msstack.request.netty.uri;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * One to Many Linked List for Route Mapping
 */
@SuppressWarnings("unused")
public class UriSegment {

    private final Pattern pattern;
    private final String paramId;
    private final Class paramClass;
    private final Field paramField;
    private final Method paramMethod;
    private final HashSet<UriSegment> segments;

    /**
     * Constructor for URI segment
     *
     * @param uriSegment URI segment as String
     */
    public UriSegment(String uriSegment) {
        this.pattern = extractPattern(uriSegment);
        this.paramId = extractParamId();
        this.paramClass = extractParamClass();
        this.paramField = extractParamField();
        this.paramMethod = extractParamMethod();
        this.segments = new HashSet<>();
    }

    private Pattern extractPattern(String uriSegment) {
        return Pattern.compile(uriSegment);
    }

    private String extractParamId() {
        return null;
    }

    private Class extractParamClass() {
        return null;
    }

    private Field extractParamField() {
        return null;
    }

    private Method extractParamMethod() {
        return null;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getParamId() {
        return paramId;
    }

    public Class getParamClass() {
        return paramClass;
    }

    public Field getParamField() {
        return paramField;
    }

    public Method getParamMethod() {
        return paramMethod;
    }

    public HashSet<UriSegment> getSegments() {
        return segments;
    }

    public boolean isTerminalNode() {
        return segments.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UriSegment that = (UriSegment) o;

        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;
        if (paramId != null ? !paramId.equals(that.paramId) : that.paramId != null) return false;
        if (paramClass != null ? !paramClass.equals(that.paramClass) : that.paramClass != null) return false;
        if (paramField != null ? !paramField.equals(that.paramField) : that.paramField != null) return false;
        if (paramMethod != null ? !paramMethod.equals(that.paramMethod) : that.paramMethod != null) return false;
        return segments != null ? segments.equals(that.segments) : that.segments == null;
    }

    @Override
    public int hashCode() {
        int result = pattern != null ? pattern.hashCode() : 0;
        result = 31 * result + (paramId != null ? paramId.hashCode() : 0);
        result = 31 * result + (paramClass != null ? paramClass.hashCode() : 0);
        result = 31 * result + (paramField != null ? paramField.hashCode() : 0);
        result = 31 * result + (paramMethod != null ? paramMethod.hashCode() : 0);
        result = 31 * result + (segments != null ? segments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UriSegment{" +
                "pattern=" + pattern +
                ", paramField=" + paramField +
                ", paramId='" + paramId + '\'' +
                ", segments=" + segments +
                '}';
    }
}
