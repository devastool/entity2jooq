package io.github.devastool.entity2jooq.annotation.type;

import io.github.devastool.entity2jooq.annotation.type.Type.Types;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Type of the column.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
@Documented
@Repeatable(Types.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
  /**
   * Name of SQL type.
   */
  String value() default "";

  /**
   * SQL dialect.
   */
  String dialect() default "";

  /**
   * Mapping between SQL and Java types.
   */
  Class<? extends TypeMapper> mapper() default TypeMapper.class;

  /**
   * Container of {@link Type} annotations.
   *
   * @author Andrey_Yurzanov
   * @since 0.0.1
   */
  @Documented
  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Types {
    /**
     * Set of {@link Type} annotations.
     */
    Type[] value();
  }
}
