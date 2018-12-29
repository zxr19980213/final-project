package com.nju.zxr;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface MyClassAnno {
    String Author() default "zxr";
    String ClassM() default "Creature";
    String WTime() default "181210";
}
