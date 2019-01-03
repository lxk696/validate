package lxk.handler;

import javax.validation.ConstraintViolation;

public class ErrorEmailSender<T> implements AppErrorHandler<T> {

    @Override
    public void onError(ConstraintViolation<T> violation) {
        System.out.println("Sending email to support team: " + violation.getPropertyPath() + " " + violation.getMessage());
    }
}
