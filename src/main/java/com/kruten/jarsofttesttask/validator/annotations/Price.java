package com.kruten.jarsofttesttask.validator.annotations;

import com.kruten.jarsofttesttask.validator.validation.PriceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
public @interface Price {
    String message() default "{Price.invalid}";

    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default{};

}
