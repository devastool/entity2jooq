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

package io.github.devastool.entity2jooq.annotation.type;

/**
 * Mapping between SQL and Java types.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public interface TypeMapper {
  /**
   * Returns Java type by dialect and SQL type.
   *
   * @param dialect SQL dialect
   * @param sqlType SQL type
   * @return Java type
   * @throws NoSuchTypeException when th type is not found
   */
  Class<?> getType(String dialect, String sqlType) throws NoSuchTypeException;

  /**
   * Returns SQL type by dialect and Java type.
   *
   * @param dialect SQL dialect
   * @param type    Java type
   * @return SQL type
   * @throws NoSuchTypeException when the type is not found
   */
  String getSqlType(String dialect, Class<?> type) throws NoSuchTypeException;
}