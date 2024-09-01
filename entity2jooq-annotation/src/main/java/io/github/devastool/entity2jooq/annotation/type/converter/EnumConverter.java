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

package io.github.devastool.entity2jooq.annotation.type.converter;

/**
 * Converter for converting strings to specific enums.
 *
 * @param <To> converter output type
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EnumConverter<To extends Enum<To>> extends ParameterizableConverter<String, To> {
  /**
   * Constructs new instance of {@link EnumConverter}.
   *
   * @param toType converter output type
   */
  public EnumConverter(Class<To> toType) {
    super(toType);
  }

  @Override
  public To from(String value) {
    return To.valueOf(toType, value);
  }

  @Override
  public String to(To value) {
    return value.name();
  }

  @Override
  public Class<String> fromType() {
    return String.class;
  }
}