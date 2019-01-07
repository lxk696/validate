package lxk.annotation;

import org.hibernate.validator.internal.constraintvalidators.hv.ScriptAssertContext;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {PropertyScriptAssert.PropertyScriptAssertValidator.class})
@Documented
public @interface PropertyScriptAssert {

    String message() default "{org.hibernate.validator.constraints.ScriptAssert.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String lang();

    String script();

    String alias() default "_this";

    String property();

    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        PropertyScriptAssert[] value();
    }

    class PropertyScriptAssertValidator implements ConstraintValidator<PropertyScriptAssert, Object> {

        private static final Log log = LoggerFactory.make();

        private String script;

        private String languageName;

        private String alias;

        private String property;

        private String message;

        private ScriptAssertContext scriptAssertContext;

        @Override
        public void initialize(PropertyScriptAssert constraintAnnotation) {
            validateParameters(constraintAnnotation);

            this.script = constraintAnnotation.script();
            this.languageName = constraintAnnotation.lang();
            this.alias = constraintAnnotation.alias();
            this.property = constraintAnnotation.property();
            this.message = constraintAnnotation.message();
            this.scriptAssertContext = new ScriptAssertContext( constraintAnnotation.lang(), constraintAnnotation.script() );
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

            Object evaluationResult;
            //ScriptEvaluator scriptEvaluator;
            //try {
            //    ScriptEvaluatorFactory evaluatorFactory = ScriptEvaluatorFactory.getInstance();
            //    scriptEvaluator = evaluatorFactory.getScriptEvaluatorByLanguageName(languageName);
            //} catch (ScriptException e) {
            //    throw new ConstraintDeclarationException(e);
            //}

            try {
                //evaluationResult = scriptEvaluator.evaluate(script, value, alias);
                evaluationResult = scriptAssertContext.evaluateScriptAssertExpression( value, alias );
            } catch (Exception e) {
                throw log.getErrorDuringScriptExecutionException(script, e);
            }

            if (evaluationResult == null) {
                throw log.getScriptMustReturnTrueOrFalseException(script);
            }
            if (!(evaluationResult instanceof Boolean)) {
                throw log.getScriptMustReturnTrueOrFalseException(script, evaluationResult, evaluationResult.getClass().getCanonicalName());
            }

            if (Boolean.FALSE.equals(evaluationResult)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message).addPropertyNode(property).addConstraintViolation();
            }

            return Boolean.TRUE.equals(evaluationResult);
        }

        private void validateParameters(PropertyScriptAssert constraintAnnotation) {
            Contracts.assertNotEmpty(constraintAnnotation.script(), MESSAGES.parameterMustNotBeEmpty("script"));
            Contracts.assertNotEmpty(constraintAnnotation.lang(), MESSAGES.parameterMustNotBeEmpty("lang"));
            Contracts.assertNotEmpty(constraintAnnotation.alias(), MESSAGES.parameterMustNotBeEmpty("alias"));
            Contracts.assertNotEmpty(constraintAnnotation.property(), MESSAGES.parameterMustNotBeEmpty("property"));
            Contracts.assertNotEmpty(constraintAnnotation.message(), MESSAGES.parameterMustNotBeEmpty("message"));
        }
    }
}
