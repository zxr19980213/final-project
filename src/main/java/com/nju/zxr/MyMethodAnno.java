package com.nju.zxr;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface MyMethodAnno {
    String Author() default "zxr";
    String MethodF() default "Creature";
    String WTime() default "181215";
}
