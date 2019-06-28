package nationalcipher.util;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Register {
    
}
