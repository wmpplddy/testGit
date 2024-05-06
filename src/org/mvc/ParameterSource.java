package org.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

public interface ParameterSource {
    MvcFile[] getFileParam(String key);
    String[] getStringParam(String key);
    HttpServletResponse getHttpServletResponse();
    HttpServletRequest getHttpServletRequest();
    HttpSession getHttpSession();

    Set<String> getStringParamNames();
    Set<String> getFileParamNames();
}
