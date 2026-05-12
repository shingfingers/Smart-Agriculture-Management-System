package com.itwork.farm_common.exception;


import com.itwork.farm_common.result.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/*自定义业务异常*/
@Data
@AllArgsConstructor
public class BusException extends RuntimeException{
    //状态码+异常信息
   private CodeEnum codeEnum;

}
