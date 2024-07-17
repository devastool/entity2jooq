/*
 *    Copyright 2024 All entity2jooq contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.devastool.entity2jooq.annotation;

import io.github.devastool.entity2jooq.annotation.Column.Columns;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The column of marked field.
 *
 * @since 0.0.1
 * @author Andrey_Yurzanov
 */
@Documented
@Repeatable(Columns.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
  /**
   * Name of the column.
   */
  String value() default "";

  /**
   * SQL type.
   */
  String type() default "";

  /**
   * Naming strategy for column name resolving. By default {@link SnakeCaseStrategy}.
   */
  Class<? extends NamingStrategy> naming() default SnakeCaseStrategy.class;

  /**
   * Container of {@link Column} annotations.
   *
   * @author Andrey_Yurzanov
   * @since 0.0.1
   */
  @Documented
  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Columns {
    /**
     * Set of {@link Column} annotations.
     */
    Column[] value();
  }
}
