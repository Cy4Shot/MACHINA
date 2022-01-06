package com.machina.api.api_extension;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface ApiExtension {

	Class<? extends IApiExtendable> value();

}
