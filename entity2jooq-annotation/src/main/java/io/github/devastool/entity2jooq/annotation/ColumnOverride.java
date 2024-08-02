package io.github.devastool.entity2jooq.annotation;

import io.github.devastool.entity2jooq.annotation.ColumnOverride.ColumnOverrides;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Repeatable(ColumnOverrides.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnOverride {
  String name();
  Column column();

  @Documented
  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface ColumnOverrides {
    ColumnOverride[] value();
  }
}
