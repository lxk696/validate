package lxk.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 统一异常处理器
 * 
 * @author zhumaer
 * @since 8/31/2017 3:00 PM
 */
@RestController
// @ControllerAdvice
public class GlobalExceptionHandler  {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
    //     LOGGER.info("handleMethodArgumentNotValidException start, uri:{}, caused by: ", request.getRequestURI(), e);
    //     List<ParameterInvalidItem> parameterInvalidItemList = Lists.newArrayList();
    //
    //     List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
    //     for (FieldError fieldError : fieldErrorList) {
    //         ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
    //         parameterInvalidItem.setFieldName(fieldError.getField());
    //         parameterInvalidItem.setMessage(fieldError.getDefaultMessage());
    //         parameterInvalidItemList.add(parameterInvalidItem);
    //     }
    //
    //     return Result.failure(ResultCode.PARAM_IS_INVALID, parameterInvalidItemList);
    // }
}
