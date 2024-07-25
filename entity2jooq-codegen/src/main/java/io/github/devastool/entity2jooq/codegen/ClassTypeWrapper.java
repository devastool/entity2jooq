package io.github.devastool.entity2jooq.codegen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ClassTypeWrapper {
  private Class<?> type;
  private Annotation[] annotations;

  public ClassTypeWrapper() {
  }

  public ClassTypeWrapper(Class<?> type, Annotation[] annotations) {
    this.type = type;
    this.annotations = annotations;
  }

  public Field[] getDeclaredFields() {
    return type.getDeclaredFields();
  }

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public Annotation[] getAnnotations() {
    return annotations;
  }

  public void setAnnotations(Annotation[] annotations) {
    this.annotations = annotations;
  }
}
