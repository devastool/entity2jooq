package io.github.devastool.entity2jooq.codegen.definition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldPair {
  private final Class<?> type;
  private final Annotation[] annotations;

  public FieldPair(Class<?> type, Annotation[] annotations) {
    this.type = type;
    this.annotations = annotations;
  }

  public Field[] getDeclaredFields() {
    return type.getDeclaredFields();
  }

  public Class<?> getType() {
    return type;
  }

  public Annotation[] getAnnotations() {
    return annotations;
  }

}
