package KasperCommons.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface RawKasperReferenceUsage {
    String value() default "Warning: This field/method uses raw references and may be unsafe when accessing keys with '$' and '.' characters.";
}
