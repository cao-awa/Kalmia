package com.github.cao.awa.kalmia.exception.auto.config;

import java.lang.reflect.Field;

public class WrongConfigTemplateException extends RuntimeException {
    private final Class<?> processingTemplate;
    private final Field field;

    public WrongConfigTemplateException(Class<?> templateClass, Field field) {
        super(formatErrorMessage(templateClass,
                                 field
        ));
        this.processingTemplate = templateClass;
        this.field = field;
    }

    public WrongConfigTemplateException(Class<?> templateClass, Field field, Throwable cause) {
        super(formatErrorMessage(templateClass,
                                 field
              ),
              cause
        );
        this.processingTemplate = templateClass;
        this.field = field;
    }

    public Class<?> getProcessingTemplate() {
        return this.processingTemplate;
    }

    public Field getField() {
        return this.field;
    }

    private static String formatErrorMessage(Class<?> templateClass, Field field) {
        StringBuilder builder = new StringBuilder();
        builder.append("Processing the field '")
               .append(field.getName())
               .append("' in config template '")
               .append(templateClass.getName())
               .append("'");
        return builder.toString();
    }
}
