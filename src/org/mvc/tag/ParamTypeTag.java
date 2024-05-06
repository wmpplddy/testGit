package org.mvc.tag;

/**
 * �洢paramType��ǩ����Ϣ
 */
public class ParamTypeTag {
    //�ò�����key
    private String name;
    //�ò�����Դͷ����������key��øò��� ���磺cookie��header��
    private String type ;
    //�ò���������
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
