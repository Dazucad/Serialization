package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    int FILE = 1;
    int IN_MEMORY = 2;

    int cacheType() default FILE;

    String fileNamePrefix() default "data";

    boolean zip() default false;

    Class[] identityBy() default {};

    int listList() default Integer.MAX_VALUE;
}
