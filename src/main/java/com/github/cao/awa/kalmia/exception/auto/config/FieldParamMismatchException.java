package com.github.cao.awa.kalmia.exception.auto.config;

import java.lang.reflect.Field;

public class FieldParamMismatchException extends RuntimeException {
    private final Field field;
    private final Class<?> specifiedType;
    private final Class<?> currentClass;

    public FieldParamMismatchException(Field field, Class<?> specifiedType, Class<?> currentClass) {
        super(formatErrorMessage(field,
                                 specifiedType,
                                 currentClass
        ));
        this.field = field;
        this.specifiedType = specifiedType;
        this.currentClass = currentClass;
    }

    public FieldParamMismatchException(Field field, Class<?> specifiedType, Class<?> currentClass, Throwable cause) {
        super(formatErrorMessage(field,
                                 specifiedType,
                                 currentClass
              ),
              cause
        );
        this.field = field;
        this.specifiedType = specifiedType;
        this.currentClass = currentClass;
    }

    public Field getField() {
        return this.field;
    }

    public Class<?> getCurrentClass() {
        return this.currentClass;
    }

    public Class<?> getSpecifiedType() {
        return this.specifiedType;
    }

    private static String formatErrorMessage(Field field, Class<?> specifiedType, Class<?> currentClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("The field '")
               .append(field.getName())
               .append("' specified type '")
               .append(specifiedType.getName())
               .append("', given type '")
               .append(currentClass.getName())
               .append("' arent matched");
        return builder.toString();
    }
}
