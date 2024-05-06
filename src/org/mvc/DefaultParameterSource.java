package org.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

public class DefaultParameterSource implements ParameterSource {
    private Map<String, String[]> paramValues;
    private Map<String, MvcFile[]> fileParamValues;
    private HttpServletResponse response;
    private HttpServletRequest request;

    public Map<String, String[]> getParamValues() {
        return paramValues;
    }

    public void setParamValues(Map<String, String[]> paramValues) {
        this.paramValues = paramValues;
    }

    public Map<String, MvcFile[]> getFileParamValues() {
        return fileParamValues;
    }

    public void setFileParamValues(Map<String, MvcFile[]> fileParamValues) {
        this.fileParamValues = fileParamValues;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public MvcFile[] getFileParam(String key) {
        return fileParamValues.get(key);
    }

    @Override
    public String[] getStringParam(String key) {
        return paramValues.get(key);
    }

    @Override
    public HttpServletResponse getHttpServletResponse() {
        return this.getResponse();
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {
        return this.getRequest();
    }

    @Override
    public HttpSession getHttpSession() {
        return this.request.getSession();
    }

    @Override
    public Set<String> getStringParamNames() {
        return paramValues.keySet();
    }

    @Override
    public Set<String> getFileParamNames() {
        return fileParamValues.keySet();
    }


}
