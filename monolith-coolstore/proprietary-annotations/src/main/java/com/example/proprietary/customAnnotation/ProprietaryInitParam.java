package com.example.proprietary.customAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ProprietaryInitParam {
	String name() default "name";
	String value() default "value";
}