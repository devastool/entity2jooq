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

package io.github.devastool.entity2jooq.codegen.generate.code.operator;

import io.github.devastool.entity2jooq.codegen.generate.code.CodeTarget;
import java.util.Arrays;
import java.util.Collection;

/**
 * Implementation of {@link OperatorCodeGenerator} to generate Java type, in the format:
 * Map&lt;String, String&gt;.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class TypeCodeGenerator implements OperatorCodeGenerator {
  private Collection<TypeCodeGenerator> genericTypes;
  private final Class<?> type;

  private static final String GENERICS_BEGIN = "<";
  private static final String GENERICS_END = ">";
  private static final OperatorCodeGenerator GENERICS_SEPARATOR = target -> target.write(", ");

  /**
   * Constructs new instance of {@link TypeCodeGenerator}.
   *
   * @param type         type of class
   * @param genericTypes generics types of class
   */
  public TypeCodeGenerator(Class<?> type, TypeCodeGenerator... genericTypes) {
    this.type = type;

    if (genericTypes.length > 0) {
      this.genericTypes = Arrays.asList(genericTypes);
    }
  }

  @Override
  public void generate(CodeTarget target) {
    target.write(type);

    if (genericTypes != null) {
      target
          .write(GENERICS_BEGIN)
          .writeAll(genericTypes, GENERICS_SEPARATOR)
          .write(GENERICS_END);
    }
  }
}
