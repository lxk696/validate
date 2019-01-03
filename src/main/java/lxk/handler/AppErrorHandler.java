package lxk.handler;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;

public interface AppErrorHandler<T> extends Payload {

    void onError(ConstraintViolation<T> violation);
}
