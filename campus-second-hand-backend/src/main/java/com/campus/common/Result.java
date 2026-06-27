package com.campus.common;

import lombok.Data;

/**
 * 统一返回结果类。
 *
 * @param <T> 返回数据类型
 */
@Data
public class Result<T> {

    /**
     * 状态码，200 表示成功，500 表示失败。
     */
    private Integer code;

    /**
     * 返回消息。
     */
    private String message;

    /**
     * 返回数据。
     */
    private T data;

    /**
     * 返回成功结果，不携带数据。
     *
     * @return 统一返回对象
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 返回成功结果，并携带数据。
     *
     * @param data 返回数据
     * @return 统一返回对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 返回失败结果。
     *
     * @param message 失败消息
     * @return 统一返回对象
     */
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 返回指定状态码的失败结果。
     *
     * @param code 状态码
     * @param message 失败消息
     * @return 统一返回对象
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
