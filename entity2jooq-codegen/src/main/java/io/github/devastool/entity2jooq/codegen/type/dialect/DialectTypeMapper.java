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

package io.github.devastool.entity2jooq.codegen.type.dialect;

import io.github.devastool.entity2jooq.annotation.type.NoSuchTypeException;
import io.github.devastool.entity2jooq.annotation.type.TypeMapper;
import io.github.devastool.entity2jooq.codegen.type.TypePair;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link TypeMapper} for mapping type of concrete SQL dialect.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public abstract class DialectTypeMapper implements TypeMapper {
  private final String dialect;
  private final Set<TypePair> types;

  /**
   * Constructs new instance of {@link DialectTypeMapper}.
   *
   * @param dialect SQL dialect
   * @param types   types collection
   */
  public DialectTypeMapper(String dialect, Set<TypePair> types) {
    this.dialect = dialect;
    this.types = types;
  }

  @Override
  public String getSqlType(String dialect, Class<?> type) throws NoSuchTypeException {
    Class<?> targetType;
    if (Enum.class.isAssignableFrom(type)) {
      targetType = Enum.class;
    } else {
      targetType = type;
    }

    return types
        .stream()
        .filter(data -> Objects.equals(data.getType(), targetType))
        .findFirst()
        .orElseThrow(() -> new NoSuchTypeException(dialect, targetType))
        .getSqlType();
  }

  /**
   * Return SQL dialect.
   *
   * @return SQL dialect
   */
  public String getDialect() {
    return dialect;
  }
}