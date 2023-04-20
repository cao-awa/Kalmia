package com.github.cao.awa.trtr.framework.accessor.method;

import com.github.cao.awa.trtr.framework.exception.NotStaticFieldException;
import com.github.cao.awa.trtr.util.string.StringConcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Access the methods in class.
 *
 * @author 草二号机
 * @since 1.0.0
 */
public class MethodAccess {
    private static final Logger LOGGER = LogManager.getLogger("MethodAccessor");

    public static Method ensureAccessible(Method method) throws NotStaticFieldException {
        // Modifier maybe private or without declarations.
        // Need to make it be accessible.
        // If unable to access, then throw an exception for notice this error.
        if (! method.canAccess(null)) {
            if (method.trySetAccessible()) {
                return method;
            }
            throw new IllegalStateException(StringConcat.concat("The method '",
                                                                method.getName(),
                                                                "' with @Auto automatic IoC is not accessible"
            ));
        }
        return method;
    }
}
