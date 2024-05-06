package org.mvc.tag;

/**
 * 存储paramType标签的信息
 */
public class ParamTypeTag {
    //该参数的key
    private String name;
    //该参数的源头（从哪里用key获得该参数 例如：cookie或header）
    private String type ;
    //该参数的类型
    private String text ;

    public ParamTypeTag(){}
    public ParamTypeTag(String name, String type, String text) {
        this.name = name;
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
