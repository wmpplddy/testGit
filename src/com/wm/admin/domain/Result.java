package com.wm.admin.domain;

public class Result<T> {
    private Integer code;
    private String msg;
    private T data;


    public static <E> Result<E> success(E data) {
        return new Result(200, "操作成功", data);
    }

    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    public static Result error(String msg) {
        return new Result(400, msg, null);
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Result() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
