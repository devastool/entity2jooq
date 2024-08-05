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

import io.github.devastool.entity2jooq.annotation.ColumnOverride.ColumnOverrides;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to override the mapping of a column in an entity.
 *
 * @since 1.0.0
 * @author Sergey_Konovalov
 */
@Documented
@Repeatable(ColumnOverrides.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnOverride {
  /**
   * Specifies the name of the column to be overridden.
   *
   * @return the column name.
   */
  String name();

  /**
   * Specifies the new column mapping to apply.
   *
   * @return a {@link Column} annotation with the new column configuration.
   */
  Column column();

  /**
   * Container of {@link ColumnOverride} annotations.
   *
   * @since 1.0.0
   * @author Sergey_Konovalov
   */
  @Documented
  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface ColumnOverrides {
    /**
     * An array of ColumnOverride annotations.
     *
     * @return an array of {@link ColumnOverride} annotations.
     */
    ColumnOverride[] value();
  }
}
