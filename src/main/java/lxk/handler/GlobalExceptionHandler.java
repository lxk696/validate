package lxk.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import javax.validation.Payload;
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
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Set<MediaType> mediaTypeSet = new HashSet<>();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("utf-8"));
        mediaTypeSet.add(mediaType);
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypeSet);

        // LOGGER.info(
        //         "handleMethodArgumentNotValidException start, uri:{}, caused by: ",
        //         request.getRequestURI(),
        //         e);

        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        //String defaultMessage = e.getBindingResult().getGlobalErrors().iterator().next().getDefaultMessage();
        List<ErrorFild> errorFilds = new ArrayList<>();
        for (FieldError fieldError : fieldErrorList) {
            LOGGER.info("Error field:{}, Error message:{}, Error value:{}", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
            errorFilds.add(ErrorFild.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).value(fieldError.getRejectedValue()).build());
        }
        return errorFilds;
    }


    private static void processError(ConstraintViolation<?> violation) {
        Set<Class<? extends Payload>> payload = violation.getConstraintDescriptor().getPayload();

        payload.forEach(p -> {
            if (AppErrorHandler.class.isAssignableFrom(p)) {
                try {
                    AppErrorHandler errorHandler = (AppErrorHandler) p.newInstance();
                    errorHandler.onError(violation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public Object handleMethodArgumentNotValidException2(ConstraintViolationException e, HttpServletRequest request) {

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
        //Set<ConstraintViolation<TestBean>> constraintViolations =
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();


        boolean severeError = false;

        if (constraintViolations.size() > 0) {
            constraintViolations.stream().forEach(GlobalExceptionHandler::processError);

            for (ConstraintViolation<?> violation : constraintViolations) {
                Set<Class<? extends Payload>> payloads = violation.getConstraintDescriptor().getPayload();
                for (Class<? extends Payload> payload : payloads) {
                    if (payload == Severity.Error.class) {
                        severeError = true;
                        System.out.println("Payload Error: " + violation.getPropertyPath() + " " + violation.getMessage());
                    } else if (payload == Severity.Info.class) {
                        System.out.println("Payload Info: " + violation.getPropertyPath() + " " + violation.getMessage());
                    }
                }
            }
        }



        List<ErrorFild> errorFilds = new ArrayList<>();
        Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<?> next = iterator.next();
            Iterator<Path.Node> nodeIterator = next.getPropertyPath().iterator();
            Integer paramIndex = 0;
            String methodName = "";
            String kindName = "";
            List<Class<?>> parameterTypes = null;
            int i = 0;
            while (nodeIterator.hasNext()) {
                Path.Node node = nodeIterator.next();
                if (i == 0) {
                    methodName = node.getName();
                    parameterTypes = ((NodeImpl) node).getParameterTypes();
                } else if (i == 1) {
                    kindName = node.getKind().name();
                    if ("PARAMETER".equals(kindName)) {
                        paramIndex = ((NodeImpl) node).getParameterIndex();
                    }
                }
                i++;
            }

            Object invalidValue = next.getInvalidValue();
            String message = next.getMessage();
            if ("PARAMETER".equals(kindName)) {
                String[] paramNames = getParamNames(next.getLeafBean().getClass(), methodName, parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
                String theParameName = paramNames[paramIndex];
                LOGGER.info("Error field:{}, Error message:{}, Error value:{}", theParameName, message, invalidValue);
                errorFilds.add(ErrorFild.builder().field(theParameName).message(message).value(invalidValue).build());
            } else {
                LOGGER.info("Error method:{}, Error message:{}, Error value:{}", methodName, message, invalidValue);
                errorFilds.add(ErrorFild.builder().message(methodName + " 方法返回值异常，" + message).value(invalidValue).build());
            }
        }

        return errorFilds;
    }

    public String[] getParamNames(Class<?> clazz, String methodName, Class<?>[] parameTypes) {
        try {
            Method method = clazz.getMethod(methodName, parameTypes);
            ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = pnd.getParameterNames(method);
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
