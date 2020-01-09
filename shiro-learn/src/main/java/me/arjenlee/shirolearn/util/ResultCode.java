package me.arjenlee.shirolearn.util;

public enum ResultCode {
    /**
     * 成功
     */
    OK("200", "成功"),


    /**
     * 用户新建或修改数据成功
     */
    CREATED("201", "用户新建或修改数据成功"),

    /**
     * 删除数据成功
     */
    NO_CONTENT("204", "用户删除数据成功"),

    /**
     * 请求有错误
     */
    BAD_REQUEST("400", "请求有错误"),

    /**
     * 表示用户没有权限
     */
    UNAUTHORIZED("401", "没有权限"),

    /**
     * 表示用户得到授权（与401错误相对），但是访问是被禁止的
     */
    FORBIDDEN("403", "禁止访问"),

    /**
     * 用户发出的请求针对的是不存在的记录
     */
    NOT_FOUND("404", "不存在"),

    /**
     * 用户请求的资源被永久删除
     */
    GONE("410", "请求的资源被永久删除"),

    /**
     * 用户请求范围未满足
     */
    Requested_Range_Not_Satisfiable("416", "请求范围未满足"),

    /**
     * 当创建一个对象时，发生一个验证错误
     */
    UNPROCESSABLE_ENTITY("422", "参数验证错误"),

    /**
     * 价格变动
     */
    TICKETPRICE_CHANGED("423", "价格发生变动"),

    /**
     * 重复未支付订单
     */
    REPEAT_ORDER("424", "重复未支付订单"),

    /**
     * 不支持的操作
     */
    INVALID_OPERATION("430", "系统不支持此操作"),

    /**
     * AuthCode错误
     */
    INVALID_AUTHCODE("444", "无效的AuthCode"),

    /**
     * 未知的错误
     */
    UNKNOWN_ERROR("499", "未知错误"),

    /**
     * 服务器发生错误
     */
    INTERNAL_SERVER_ERROR("500", "服务器发生错误"),

    ;

    ResultCode(String value, String msg) {
        this.val = value;
        this.msg = msg;
    }

    public String val() {
        return val;
    }

    public String msg() {
        return msg;
    }

    private String val;
    private String msg;

}
