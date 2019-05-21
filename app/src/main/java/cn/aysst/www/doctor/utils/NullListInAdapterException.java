package cn.aysst.www.doctor.utils;

/**
 * Created by 蒲公英之流 on 2019-02-27.
 */

public class NullListInAdapterException extends Exception {
    String msg;

    /**
     * adapter中空列表异常无参构造函数
     *
     * @param
     */
    public NullListInAdapterException() {
        super();
    }

    /**
     * adapter中空列表异常有参构造函数
     *
     * @param msg
     */
    public NullListInAdapterException(String msg) {
        super(msg);
        this.msg = msg;
    }
}