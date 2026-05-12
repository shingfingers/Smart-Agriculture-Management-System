package com.itbaizhan.farm_common.result;


import lombok.AllArgsConstructor;
import lombok.Data;

/*
*
* 统一结果集*/
@Data
@AllArgsConstructor
public class BaseResult<T> {
    //状态码
    private Integer code;
    //提示信息
    private  String message;
    //返回数据
    private T data;

    //构建成功结果
    public static <T> BaseResult<T> ok(){
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMessage(),null);
    }

    //带数据的成功结果
    public static <T> BaseResult ok(T data){
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMessage(),data);
    }

    //失败
    public static <T> BaseResult error(CodeEnum codeEnum){
        return new BaseResult<>(codeEnum.getCode(),codeEnum.getMessage(),null);
    }
    public static <T> BaseResult error(CodeEnum codeEnum,T data){
        return new BaseResult<>(codeEnum.getCode(),codeEnum.getMessage(),data);
    }
}
