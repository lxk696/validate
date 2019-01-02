package lxk.handler;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @desc 统一异常处理器
 * @author zhumaer
 * @since 8/31/2017 3:00 PM
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(Exception.class)ConstraintViolationException
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Object handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        Set<MediaType> mediaTypeSet = new HashSet<>();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("utf-8"));
        mediaTypeSet.add(mediaType);
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypeSet);

        // LOGGER.info(
        //         "handleMethodArgumentNotValidException start, uri:{}, caused by: ",
        //         request.getRequestURI(),
        //         e);

        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        List<ErrorFild> errorFilds = new ArrayList<>();
        for (FieldError fieldError : fieldErrorList) {
            LOGGER.info(
                    "Error field:{}, Error message:{}, Error value:{}",
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue());
            errorFilds.add(
                    ErrorFild.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                            .value(fieldError.getRejectedValue())
                            .build());
        }
        return errorFilds;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public Object handleMethodArgumentNotValidException2(
            ConstraintViolationException e, HttpServletRequest request) {

        Set<MediaType> mediaTypeSet = new HashSet<>();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("utf-8"));
        mediaTypeSet.add(mediaType);
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypeSet);

        // LOGGER.info(
        //         "handleMethodArgumentNotValidException start, uri:{}, caused by: ",
        //         request.getRequestURI(),
        //         e);
        // e.getConstraintViolations().iterator().next().getPropertyPath().iterator()
        // e.getConstraintViolations()
        List<ErrorFild> errorFilds = new ArrayList<>();
        Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        for (ConstraintViolation<?> next : e.getConstraintViolations()) {
            // 获得验证失败的类 next.getLeafBean()
            // 获得验证失败的值 next.getInvalidValue()
            // 获取参数值 next.getExecutableParameters()
            // 获得返回值 next.getExecutableReturnValue()

            LOGGER.info(
                    "Error class:{},Error field:{}, Error message:{}, Error value:{}",
                    next.getLeafBean().getClass().getName(),
                    next.getExecutableParameters(),
                    next.getMessage(),
                    next.getInvalidValue());
        }

        while (iterator.hasNext()) {
            ConstraintViolation<?> next = iterator.next();
            Iterator<Path.Node> nodeIterator = next.getPropertyPath().iterator();
            Integer paramIndex = 0;
            String methodName = "";
            List<Class<?>> parameterTypes = null;
            int i = 0;
            while (nodeIterator.hasNext()) {
                Path.Node node = nodeIterator.next();
                if (i == 0) {
                    methodName = node.getName();
                    parameterTypes = ((NodeImpl) node).getParameterTypes();
                } else if (i == 1) {
                    paramIndex = ((NodeImpl) node).getParameterIndex();
                }
                i++;
            }

            Object invalidValue = next.getInvalidValue();
            String message = next.getMessage();
            String[] paramNames = getParamNames(next.getLeafBean().getClass(), methodName, parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
            String theParameName = paramNames[paramIndex];
            LOGGER.info(
                    "Error field:{}, Error message:{}, Error value:{}", theParameName, message, invalidValue);
            errorFilds.add(
                    ErrorFild.builder().field(theParameName).message(message).value(invalidValue).build());
        }

        return errorFilds;
    }

    public String[] getParamNames(Class<?> clazz, String methodName, Class<?>[] parameTypes) {
        try {
            Method method = clazz.getMethod(methodName, parameTypes);
            ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = pnd.getParameterNames(method);
            System.out.println(Arrays.toString(paramNames));
            return paramNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<?>[] getParamNamesType(Object[] parames) {
        try {
            Class<?>[] paramsTypes = new Class<?>[parames.length];
            for (int i = 0; i < parames.length; i++) {
                paramsTypes[i] = parames.getClass();
            }
            return paramsTypes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ErrorFild {
        String field;
        String message;
        Object value;
    }
}
