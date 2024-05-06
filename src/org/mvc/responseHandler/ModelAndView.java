package org.mvc.responseHandler;

import java.util.HashMap;

public class ModelAndView {
    private String viewName ;
    private HashMap<String,Object> reqAttributes = new HashMap<>();
    public ModelAndView(){}
    public ModelAndView(String viewName){
        this.viewName=viewName;
    }
    public String getViewName() {
        return viewName;
    }
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    public HashMap<String, Object> getReqAttributes() {
        return reqAttributes;
    }
    public void setReqAttributes(HashMap<String, Object> reqAttributes) {
        this.reqAttributes = reqAttributes;
    }
}
