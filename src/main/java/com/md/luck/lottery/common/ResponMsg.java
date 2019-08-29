package com.md.luck.lottery.common;

/**
 * @author madong
 * @version V2.1 * Update Logs: * Name: * Date: * Description: 初始化
 * @ClassName: ResponMsg
 * @Description: (这里用一句话描述这个类的作用)
 * @date 2019/8/29 10:32
 */
public class ResponMsg<T> {
    public static int SUCCESS_CODE = 0;
    public static int FAIL_CODE = -1;
    private int code;
    private T data;
    private String msg;

    public ResponMsg(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 创建成功对象，返回值
     * @param data 数据对象
     * @return ResponMsg
     */
    public static ResponMsg newSuccess(Object data) {
        ResponMsg responMsg = new ResponMsg(ResponMsg.SUCCESS_CODE, data);
        return responMsg;
    }

    /**
     * 创建失败对象，返回值
     * @param data 数据对象
     * @return ResponMsg
     */
    public static ResponMsg newFail(Object data) {
        ResponMsg responMsg = new ResponMsg(ResponMsg.FAIL_CODE, data);
        return responMsg;
    }

    /**
     * 设置返回信息
     * @param msg
     */
    public ResponMsg setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
