package com.tomorrow.android.api;

import com.tomorrow.android.R;

/**
 * Created by jasontujun on 14-7-12.
 */
public class StatusCode {

    public static final int HTTP_SUCCESS = 200;

    // 获取分页数据成功，已经是最后一页
    public static final int HTTP_LAST_PAGE = 2001;


    // [本地]http连接错误
    public static final int HTTP_EXCEPTION = 4000;

    // [本地]非法参数
    public static final int HTTP_LOCAL_ERROR_ARGUMENT = 4001;

    // [本地]解析结果一长
    public static final int HTTP_LOCAL_PARSE_ERROR = 4002;

    // [远程]服务器错误
    public static final int HTTP_FAIL = 4100;

    // [远程]服务器查无此记录
    public static final int HTTP_NOT_FOUND_DATA = 4110;

    // [远程]非法参数
    public static final int HTTP_ILLEGAL_ARGUMENT = 4120;

    // [远程]服务器无数据
    public static final int HTTP_NO_DATA = 4130;

    // [远程]登陆错误
    public static final int HTTP_LOGIN_ERROR = 4210;

    // [远程]注册错误
    public static final int HTTP_REGISTER_EXIST = 4220;


    public static boolean success(int code) {
        return (200 <= code && code < 300) ||
                (2000 <= code && code < 3000);
    }

    public static int toErrorString(int code) {
        switch (code) {
            case HTTP_EXCEPTION:
            case HTTP_LOCAL_ERROR_ARGUMENT:
                return R.string.http_exception;
            case HTTP_ILLEGAL_ARGUMENT:
                return R.string.http_bad_request;
            case HTTP_NOT_FOUND_DATA:
            case HTTP_NO_DATA:
                return R.string.http_no_data;
            case HTTP_LOGIN_ERROR:
                return R.string.login_argument_error;
            case HTTP_REGISTER_EXIST:
                return R.string.register_exist;
            default:
                return R.string.http_fail;
        }
    }


}
