package me.gopro336.zenith.feature;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Gopro336
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationHelper {
    String name() default "No name provided!";

    String description() default "No description provided!";

    Category category() default Category.CLIENT;

    int key() default Keyboard.KEY_NONE;
}