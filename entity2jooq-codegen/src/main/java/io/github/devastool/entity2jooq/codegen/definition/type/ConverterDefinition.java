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

package io.github.devastool.entity2jooq.codegen.definition.type;

import java.lang.reflect.TypeVariable;
import java.util.Optional;
import org.jooq.Converter;

/**
 * Meta-information about converter of entity field to Jooq built-in type.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class ConverterDefinition implements Comparable<ConverterDefinition> {
  private final Converter converter;

  private static final String TO_TYPE = "To";

  /**
   * Constructs new instance of {@link ConverterDefinition}.
   *
   * @param converter Jooq converter
   */
  public ConverterDefinition(Converter converter) {
    this.converter = converter;
  }

  /**
   * Returns instance of Jooq converter.
   *
   * @return instance of Jooq converter
   */
  public Converter getConverter() {
    return converter;
  }

  /**
   * Return type of the Jooq converter.
   *
   * @return type of the Jooq converter
   */
  public Class<?> getConverterType() {
    return converter.getClass();
  }

  /**
   * Returns ToType from converter definition.
   *
   * @return ToType from converter definition, or empty container
   */
  public Optional<Class<?>> getGenericToType() {
    Class<? extends Converter> type = converter.getClass();
    TypeVariable<? extends Class<? extends Converter>>[] parameters = type.getTypeParameters();
    for (TypeVariable<? extends Class<? extends Converter>> parameter : parameters) {
      if (TO_TYPE.equals(parameter.getName())) {
        return Optional.ofNullable(converter.toType());
      }
    }
    return Optional.empty();
  }

  @Override
  public int compareTo(ConverterDefinition other) {
    return getName(this).compareTo(getName(other));
  }

  // Returns name for comparing converters
  private String getName(ConverterDefinition definition) {
    StringBuilder nameBuilder = new StringBuilder(getConverterType().getCanonicalName());

    Optional<Class<?>> toType = definition.getGenericToType();
    if (toType.isPresent()) {
      Class<?> type = toType.get();
      nameBuilder.append(type.getCanonicalName());
    }
    return nameBuilder.toString();
  }
}
