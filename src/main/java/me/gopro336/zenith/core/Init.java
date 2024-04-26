package me.gopro336.zenith.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with this Annotation
 * will be called during the initialization process
 * with regard to their given stage
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Init {
    InitStage stage() default InitStage.Pre;
    Class<?>[] dependencies() default { };
}
