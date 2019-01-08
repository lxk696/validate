//package lxk.test;
//
//import org.hibernate.validator.constraints.CompositionType;
//import org.hibernate.validator.constraints.ConstraintComposition;
//import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
//import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
//import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
//import org.hibernate.validator.internal.util.CollectionHelper;
//import org.hibernate.validator.internal.util.TypeHelper;
//import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
//import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
//import org.hibernate.validator.internal.util.logging.Log;
//import org.hibernate.validator.internal.util.logging.LoggerFactory;
//import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
//import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
//import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
//
//import javax.validation.*;
//import javax.validation.constraintvalidation.SupportedValidationTarget;
//import javax.validation.constraintvalidation.ValidationTarget;
//import javax.validation.groups.Default;
//import javax.validation.metadata.ConstraintDescriptor;
//import java.lang.annotation.*;
//import java.lang.reflect.*;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.util.*;
//
///**
// * @author 刘雄康
// * @version v1.0
// * @description 为提示消息获取校验值, 重新不可变map
// * @date 2019年01月05日
// */
//public class ToGetValidateValue extends ConstraintDescriptorImpl  {
//    private static final long serialVersionUID = -2563102960314069246L;
//    private static final Log log = LoggerFactory.make();
//    private static final int OVERRIDES_PARAMETER_DEFAULT_INDEX = -1;
//    private static final List<String> NON_COMPOSING_CONSTRAINT_ANNOTATIONS = Arrays.asList(Documented.class.getName(), Retention.class.getName(), Target.class.getName(), Constraint.class.getName(), ReportAsSingleViolation.class.getName());
//    private final T annotation;
//    private final Class<T> annotationType;
//    private final List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses;
//    private final List<Class<? extends ConstraintValidator<T, ?>>> matchingConstraintValidatorClasses;
//    private final Set<Class<?>> groups;
//    private final Map<String, Object> attributes;
//    private final Set<Class<? extends Payload>> payloads;
//    private final Set<ConstraintDescriptorImpl<?>> composingConstraints;
//    private final boolean isReportAsSingleInvalidConstraint;
//    private final ElementType elementType;
//    private final ConstraintOrigin definedOn;
//    private final ConstraintDescriptorImpl.ConstraintType constraintType;
//    private final CompositionType compositionType;
//    private final int hashCode;
//
//    public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type, Class<?> implicitGroup, ConstraintOrigin definedOn, ConstraintDescriptorImpl.ConstraintType externalConstraintType) {
//        this.annotation = annotation;
//        this.annotationType = this.annotation.annotationType();
//        this.elementType = type;
//        this.definedOn = definedOn;
//        this.isReportAsSingleInvalidConstraint = this.annotationType.isAnnotationPresent(ReportAsSingleViolation.class);
//        this.attributes = this.buildAnnotationParameterMap(annotation);
//        this.groups = this.buildGroupSet(implicitGroup);
//        this.payloads = this.buildPayloadSet(annotation);
//        this.constraintValidatorClasses = constraintHelper.getAllValidatorClasses(this.annotationType);
//        List<Class<? extends ConstraintValidator<T, ?>>> crossParameterValidatorClasses = constraintHelper.findValidatorClasses(this.annotationType, ValidationTarget.PARAMETERS);
//        List<Class<? extends ConstraintValidator<T, ?>>> genericValidatorClasses = constraintHelper.findValidatorClasses(this.annotationType, ValidationTarget.ANNOTATED_ELEMENT);
//        if (crossParameterValidatorClasses.size() > 1) {
//            throw log.getMultipleCrossParameterValidatorClassesException(this.annotationType.getName());
//        } else {
//            this.constraintType = this.determineConstraintType(annotation.annotationType(), member, type, !genericValidatorClasses.isEmpty(), !crossParameterValidatorClasses.isEmpty(), externalConstraintType);
//            this.composingConstraints = this.parseComposingConstraints(member, constraintHelper, this.constraintType);
//            this.compositionType = this.parseCompositionType(constraintHelper);
//            this.validateComposingConstraintTypes();
//            if (this.constraintType == ConstraintDescriptorImpl.ConstraintType.GENERIC) {
//                this.matchingConstraintValidatorClasses = Collections.unmodifiableList(genericValidatorClasses);
//            } else {
//                this.matchingConstraintValidatorClasses = Collections.unmodifiableList(crossParameterValidatorClasses);
//            }
//
//            this.hashCode = annotation.hashCode();
//        }
//    }
//
//    public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type) {
//        this(constraintHelper, member, annotation, type, (Class)null, ConstraintOrigin.DEFINED_LOCALLY, (ConstraintDescriptorImpl.ConstraintType)null);
//    }
//
//    public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type, ConstraintDescriptorImpl.ConstraintType constraintType) {
//        this(constraintHelper, member, annotation, type, (Class)null, ConstraintOrigin.DEFINED_LOCALLY, constraintType);
//    }
//
//    public T getAnnotation() {
//        return this.annotation;
//    }
//
//    public Class<T> getAnnotationType() {
//        return this.annotationType;
//    }
//
//    public String getMessageTemplate() {
//        return (String)this.getAttributes().get("message");
//    }
//
//    public Set<Class<?>> getGroups() {
//        return this.groups;
//    }
//
//    public Set<Class<? extends Payload>> getPayload() {
//        return this.payloads;
//    }
//
//    public ConstraintTarget getValidationAppliesTo() {
//        return (ConstraintTarget)this.attributes.get("validationAppliesTo");
//    }
//
//    public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses() {
//        return this.constraintValidatorClasses;
//    }
//
//    public List<Class<? extends ConstraintValidator<T, ?>>> getMatchingConstraintValidatorClasses() {
//        return this.matchingConstraintValidatorClasses;
//    }
//
//    public Map<Type, Class<? extends ConstraintValidator<T, ?>>> getAvailableValidatorTypes() {
//        Map<Type, Class<? extends ConstraintValidator<T, ?>>> availableValidatorTypes = TypeHelper.getValidatorsTypes(this.getAnnotationType(), this.getMatchingConstraintValidatorClasses());
//        return availableValidatorTypes;
//    }
//
//    public Map<String, Object> getAttributes() {
//        return this.attributes;
//    }
//
//    public Set<ConstraintDescriptor<?>> getComposingConstraints() {
//        return Collections.unmodifiableSet(this.composingConstraints);
//    }
//
//    public Set<ConstraintDescriptorImpl<?>> getComposingConstraintImpls() {
//        return this.composingConstraints;
//    }
//
//    public boolean isReportAsSingleViolation() {
//        return this.isReportAsSingleInvalidConstraint;
//    }
//
//    public ElementType getElementType() {
//        return this.elementType;
//    }
//
//    public ConstraintOrigin getDefinedOn() {
//        return this.definedOn;
//    }
//
//    public ConstraintDescriptorImpl.ConstraintType getConstraintType() {
//        return this.constraintType;
//    }
//
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        } else if (o != null && this.getClass() == o.getClass()) {
//            ConstraintDescriptorImpl<?> that = (ConstraintDescriptorImpl)o;
//            if (this.annotation != null) {
//                if (!this.annotation.equals(that.annotation)) {
//                    return false;
//                }
//            } else if (that.annotation != null) {
//                return false;
//            }
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public int hashCode() {
//        return this.hashCode;
//    }
//
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("ConstraintDescriptorImpl");
//        sb.append("{annotation=").append(this.annotationType.getName());
//        sb.append(", payloads=").append(this.payloads);
//        sb.append(", hasComposingConstraints=").append(this.composingConstraints.isEmpty());
//        sb.append(", isReportAsSingleInvalidConstraint=").append(this.isReportAsSingleInvalidConstraint);
//        sb.append(", elementType=").append(this.elementType);
//        sb.append(", definedOn=").append(this.definedOn);
//        sb.append(", groups=").append(this.groups);
//        sb.append(", attributes=").append(this.attributes);
//        sb.append(", constraintType=").append(this.constraintType);
//        sb.append('}');
//        return sb.toString();
//    }
//
//    private ConstraintDescriptorImpl.ConstraintType determineConstraintType(Class<? extends Annotation> constraintAnnotationType, Member member, ElementType elementType, boolean hasGenericValidators, boolean hasCrossParameterValidator, ConstraintDescriptorImpl.ConstraintType externalConstraintType) {
//        ConstraintTarget constraintTarget = (ConstraintTarget)this.attributes.get("validationAppliesTo");
//        ConstraintDescriptorImpl.ConstraintType constraintType = null;
//        boolean isExecutable = this.isExecutable(elementType);
//        if (constraintTarget == ConstraintTarget.RETURN_VALUE) {
//            if (!isExecutable) {
//                throw log.getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(this.annotationType.getName(), ConstraintTarget.RETURN_VALUE);
//            }
//
//            constraintType = ConstraintDescriptorImpl.ConstraintType.GENERIC;
//        } else if (constraintTarget == ConstraintTarget.PARAMETERS) {
//            if (!isExecutable) {
//                throw log.getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(this.annotationType.getName(), ConstraintTarget.PARAMETERS);
//            }
//
//            constraintType = ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER;
//        } else if (externalConstraintType != null) {
//            constraintType = externalConstraintType;
//        } else if (hasGenericValidators && !hasCrossParameterValidator) {
//            constraintType = ConstraintDescriptorImpl.ConstraintType.GENERIC;
//        } else if (!hasGenericValidators && hasCrossParameterValidator) {
//            constraintType = ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER;
//        } else if (!isExecutable) {
//            constraintType = ConstraintDescriptorImpl.ConstraintType.GENERIC;
//        } else if (constraintAnnotationType.isAnnotationPresent(SupportedValidationTarget.class)) {
//            SupportedValidationTarget supportedValidationTarget = (SupportedValidationTarget)constraintAnnotationType.getAnnotation(SupportedValidationTarget.class);
//            if (supportedValidationTarget.value().length == 1) {
//                constraintType = supportedValidationTarget.value()[0] == ValidationTarget.ANNOTATED_ELEMENT ? ConstraintDescriptorImpl.ConstraintType.GENERIC : ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER;
//            }
//        } else {
//            boolean hasParameters = this.hasParameters(member);
//            boolean hasReturnValue = this.hasReturnValue(member);
//            if (!hasParameters && hasReturnValue) {
//                constraintType = ConstraintDescriptorImpl.ConstraintType.GENERIC;
//            } else if (hasParameters && !hasReturnValue) {
//                constraintType = ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER;
//            }
//        }
//
//        if (constraintType == null) {
//            throw log.getImplicitConstraintTargetInAmbiguousConfigurationException(this.annotationType.getName());
//        } else {
//            if (constraintType == ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER) {
//                this.validateCrossParameterConstraintType(member, hasCrossParameterValidator);
//            }
//
//            return constraintType;
//        }
//    }
//
//    private void validateCrossParameterConstraintType(Member member, boolean hasCrossParameterValidator) {
//        if (!hasCrossParameterValidator) {
//            throw log.getCrossParameterConstraintHasNoValidatorException(this.annotationType.getName());
//        } else if (member == null) {
//            throw log.getCrossParameterConstraintOnClassException(this.annotationType.getName());
//        } else if (member instanceof Field) {
//            throw log.getCrossParameterConstraintOnFieldException(this.annotationType.getName(), member.toString());
//        } else if (!this.hasParameters(member)) {
//            throw log.getCrossParameterConstraintOnMethodWithoutParametersException(this.annotationType.getName(), member.toString());
//        }
//    }
//
//    private void validateComposingConstraintTypes() {
//        Iterator var1 = this.composingConstraints.iterator();
//
//        ConstraintDescriptorImpl composingConstraint;
//        do {
//            if (!var1.hasNext()) {
//                return;
//            }
//
//            composingConstraint = (ConstraintDescriptorImpl)var1.next();
//        } while(composingConstraint.constraintType == this.constraintType);
//
//        throw log.getComposedAndComposingConstraintsHaveDifferentTypesException(this.annotationType.getName(), composingConstraint.annotationType.getName(), this.constraintType, composingConstraint.constraintType);
//    }
//
//    private boolean hasParameters(Member member) {
//        boolean hasParameters = false;
//        if (member instanceof Constructor) {
//            Constructor<?> constructor = (Constructor)member;
//            hasParameters = constructor.getParameterTypes().length > 0;
//        } else if (member instanceof Method) {
//            Method method = (Method)member;
//            hasParameters = method.getParameterTypes().length > 0;
//        }
//
//        return hasParameters;
//    }
//
//    private boolean hasReturnValue(Member member) {
//        boolean hasReturnValue;
//        if (member instanceof Constructor) {
//            hasReturnValue = true;
//        } else if (member instanceof Method) {
//            Method method = (Method)member;
//            hasReturnValue = method.getGenericReturnType() != Void.TYPE;
//        } else {
//            hasReturnValue = false;
//        }
//
//        return hasReturnValue;
//    }
//
//    private boolean isExecutable(ElementType elementType) {
//        return elementType == ElementType.METHOD || elementType == ElementType.CONSTRUCTOR;
//    }
//
//    private Set<Class<? extends Payload>> buildPayloadSet(T annotation) {
//        HashSet payloadSet = CollectionHelper.newHashSet();
//
//        Class[] payloadFromAnnotation;
//        try {
//            payloadFromAnnotation = (Class[])this.run(GetAnnotationParameter.action(annotation, "payload", Class[].class));
//        } catch (ValidationException var5) {
//            payloadFromAnnotation = null;
//        }
//
//        if (payloadFromAnnotation != null) {
//            payloadSet.addAll(Arrays.asList(payloadFromAnnotation));
//        }
//
//        return Collections.unmodifiableSet(payloadSet);
//    }
//
//    private Set<Class<?>> buildGroupSet(Class<?> implicitGroup) {
//        Set<Class<?>> groupSet = CollectionHelper.newHashSet();
//        Class<?>[] groupsFromAnnotation = (Class[])this.run(GetAnnotationParameter.action(this.annotation, "groups", Class[].class));
//        if (groupsFromAnnotation.length == 0) {
//            groupSet.add(Default.class);
//        } else {
//            groupSet.addAll(Arrays.asList(groupsFromAnnotation));
//        }
//
//        if (implicitGroup != null && groupSet.contains(Default.class)) {
//            groupSet.add(implicitGroup);
//        }
//
//        return Collections.unmodifiableSet(groupSet);
//    }
//
//    private Map<String, Object> buildAnnotationParameterMap(Annotation annotation) {
//        Method[] declaredMethods = (Method[])this.run(GetDeclaredMethods.action(annotation.annotationType()));
//        Map<String, Object> parameters = CollectionHelper.newHashMap(declaredMethods.length);
//        Method[] var4 = declaredMethods;
//        int var5 = declaredMethods.length;
//
//        for(int var6 = 0; var6 < var5; ++var6) {
//            Method m = var4[var6];
//            Object value = this.run(GetAnnotationParameter.action(annotation, m.getName(), Object.class));
//            parameters.put(m.getName(), value);
//        }
//
//        return Collections.unmodifiableMap(parameters);
//    }
//
//    private Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> parseOverrideParameters() {
//        Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters = CollectionHelper.newHashMap();
//        Method[] methods = (Method[])this.run(GetDeclaredMethods.action(this.annotationType));
//        Method[] var3 = methods;
//        int var4 = methods.length;
//
//        for(int var5 = 0; var5 < var4; ++var5) {
//            Method m = var3[var5];
//            if (m.getAnnotation(OverridesAttribute.class) != null) {
//                this.addOverrideAttributes(overrideParameters, m, (OverridesAttribute)m.getAnnotation(OverridesAttribute.class));
//            } else if (m.getAnnotation(javax.validation.OverridesAttribute.List.class) != null) {
//                this.addOverrideAttributes(overrideParameters, m, ((javax.validation.OverridesAttribute.List)m.getAnnotation(javax.validation.OverridesAttribute.List.class)).value());
//            }
//        }
//
//        return overrideParameters;
//    }
//
//    private void addOverrideAttributes(Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters, Method m, OverridesAttribute... attributes) {
//        Object value = this.run(GetAnnotationParameter.action(this.annotation, m.getName(), Object.class));
//        OverridesAttribute[] var5 = attributes;
//        int var6 = attributes.length;
//
//        for(int var7 = 0; var7 < var6; ++var7) {
//            OverridesAttribute overridesAttribute = var5[var7];
//            this.ensureAttributeIsOverridable(m, overridesAttribute);
//            ConstraintDescriptorImpl<T>.ClassIndexWrapper wrapper = new ConstraintDescriptorImpl.ClassIndexWrapper(overridesAttribute.constraint(), overridesAttribute.constraintIndex());
//            Map<String, Object> map = (Map)overrideParameters.get(wrapper);
//            if (map == null) {
//                map = CollectionHelper.newHashMap();
//                overrideParameters.put(wrapper, map);
//            }
//
//            ((Map)map).put(overridesAttribute.name(), value);
//        }
//
//    }
//
//    private void ensureAttributeIsOverridable(Method m, OverridesAttribute overridesAttribute) {
//        Method method = (Method)this.run(GetMethod.action(overridesAttribute.constraint(), overridesAttribute.name()));
//        if (method == null) {
//            throw log.getOverriddenConstraintAttributeNotFoundException(overridesAttribute.name());
//        } else {
//            Class<?> returnTypeOfOverriddenConstraint = method.getReturnType();
//            if (!returnTypeOfOverriddenConstraint.equals(m.getReturnType())) {
//                throw log.getWrongAttributeTypeForOverriddenConstraintException(returnTypeOfOverriddenConstraint.getName(), m.getReturnType());
//            }
//        }
//    }
//
//    private Set<ConstraintDescriptorImpl<?>> parseComposingConstraints(Member member, ConstraintHelper constraintHelper, ConstraintDescriptorImpl.ConstraintType constraintType) {
//        Set<ConstraintDescriptorImpl<?>> composingConstraintsSet = CollectionHelper.newHashSet();
//        Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters = this.parseOverrideParameters();
//        Annotation[] var6 = this.annotationType.getDeclaredAnnotations();
//        int var7 = var6.length;
//
//        for(int var8 = 0; var8 < var7; ++var8) {
//            Annotation declaredAnnotation = var6[var8];
//            Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();
//            if (!NON_COMPOSING_CONSTRAINT_ANNOTATIONS.contains(declaredAnnotationType.getName())) {
//                if (constraintHelper.isConstraintAnnotation(declaredAnnotationType)) {
//                    ConstraintDescriptorImpl<?> descriptor = this.createComposingConstraintDescriptor(member, overrideParameters, -1, declaredAnnotation, constraintType, constraintHelper);
//                    composingConstraintsSet.add(descriptor);
//                    log.debugf("Adding composing constraint: %s.", descriptor);
//                } else if (constraintHelper.isMultiValueConstraint(declaredAnnotationType)) {
//                    List<Annotation> multiValueConstraints = constraintHelper.getConstraintsFromMultiValueConstraint(declaredAnnotation);
//                    int index = 0;
//
//                    for(Iterator var13 = multiValueConstraints.iterator(); var13.hasNext(); ++index) {
//                        Annotation constraintAnnotation = (Annotation)var13.next();
//                        ConstraintDescriptorImpl<?> descriptor = this.createComposingConstraintDescriptor(member, overrideParameters, index, constraintAnnotation, constraintType, constraintHelper);
//                        composingConstraintsSet.add(descriptor);
//                        log.debugf("Adding composing constraint: %s.", descriptor);
//                    }
//                }
//            }
//        }
//
//        return Collections.unmodifiableSet(composingConstraintsSet);
//    }
//
//    private CompositionType parseCompositionType(ConstraintHelper constraintHelper) {
//        Annotation[] var2 = this.annotationType.getDeclaredAnnotations();
//        int var3 = var2.length;
//
//        for(int var4 = 0; var4 < var3; ++var4) {
//            Annotation declaredAnnotation = var2[var4];
//            Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();
//            if (!NON_COMPOSING_CONSTRAINT_ANNOTATIONS.contains(declaredAnnotationType.getName()) && constraintHelper.isConstraintComposition(declaredAnnotationType)) {
//                if (log.isDebugEnabled()) {
//                    log.debugf("Adding Bool %s.", declaredAnnotationType.getName());
//                }
//
//                return ((ConstraintComposition)declaredAnnotation).value();
//            }
//        }
//
//        return CompositionType.AND;
//    }
//
//    private <U extends Annotation> ConstraintDescriptorImpl<U> createComposingConstraintDescriptor(Member member, Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters, int index, U constraintAnnotation, ConstraintDescriptorImpl.ConstraintType constraintType, ConstraintHelper constraintHelper) {
//        Class<U> annotationType = constraintAnnotation.annotationType();
//        AnnotationDescriptor<U> annotationDescriptor = new AnnotationDescriptor(annotationType, this.buildAnnotationParameterMap(constraintAnnotation));
//        Map<String, Object> overrides = (Map)overrideParameters.get(new ConstraintDescriptorImpl.ClassIndexWrapper(annotationType, index));
//        if (overrides != null) {
//            Iterator var10 = overrides.entrySet().iterator();
//
//            while(var10.hasNext()) {
//                Map.Entry<String, Object> entry = (Map.Entry)var10.next();
//                annotationDescriptor.setValue((String)entry.getKey(), entry.getValue());
//            }
//        }
//
//        annotationDescriptor.setValue("groups", this.groups.toArray(new Class[this.groups.size()]));
//        annotationDescriptor.setValue("payload", this.payloads.toArray(new Class[this.payloads.size()]));
//        if (annotationDescriptor.getElements().containsKey("validationAppliesTo")) {
//            ConstraintTarget validationAppliesTo = this.getValidationAppliesTo();
//            if (validationAppliesTo == null) {
//                if (constraintType == ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER) {
//                    validationAppliesTo = ConstraintTarget.PARAMETERS;
//                } else {
//                    validationAppliesTo = ConstraintTarget.IMPLICIT;
//                }
//            }
//
//            annotationDescriptor.setValue("validationAppliesTo", validationAppliesTo);
//        }
//
//        U annotationProxy = AnnotationFactory.create(annotationDescriptor);
//        return new ConstraintDescriptorImpl(constraintHelper, member, annotationProxy, this.elementType, (Class)null, this.definedOn, constraintType);
//    }
//
//    private <P> P run(PrivilegedAction<P> action) {
//        return System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run();
//    }
//
//    public CompositionType getCompositionType() {
//        return this.compositionType;
//    }
//
//    public static BtUser ConstraintType {
//        GENERIC,
//        CROSS_PARAMETER;
//
//        private ConstraintType() {
//        }
//    }
//
//    private class ClassIndexWrapper {
//        final Class<?> clazz;
//        final int index;
//
//        ClassIndexWrapper(Class<?> clazz, int index) {
//            this.clazz = clazz;
//            this.index = index;
//        }
//
//        public boolean equals(Object o) {
//            if (this == o) {
//                return true;
//            } else if (o != null && this.getClass() == o.getClass()) {
//                ConstraintDescriptorImpl<T>.ClassIndexWrapper that = (ConstraintDescriptorImpl.ClassIndexWrapper)o;
//                if (this.index != that.index) {
//                    return false;
//                } else if (this.clazz != null && !this.clazz.equals(that.clazz)) {
//                    return false;
//                } else {
//                    return this.clazz != null || that.clazz == null;
//                }
//            } else {
//                return false;
//            }
//        }
//
//        public int hashCode() {
//            int result = this.clazz != null ? this.clazz.hashCode() : 0;
//            result = 31 * result + this.index;
//            return result;
//        }
//    }
//}
