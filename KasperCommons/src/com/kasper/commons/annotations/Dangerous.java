package com.kasper.commons.annotations;

import java.lang.annotation.*;

public @interface Dangerous {
    /**
     * @Warning: This method is unsafe for use. Please do not use unless you know what you are doing.<br>Unexpected behavior may occur.
     *
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface UnsafeMethod {
        String value() default "Warning: This method is unsafe for use. Please do not use unless you know what you are doing.";
    }
}
