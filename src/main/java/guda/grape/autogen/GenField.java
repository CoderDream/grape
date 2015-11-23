package guda.grape.autogen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by foodoon on 2014/6/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenField {

    boolean ignore() default false;

    String cn();

    int order() default 0;

    boolean inSearchForm() default false;

    boolean canNull() default false;
}
