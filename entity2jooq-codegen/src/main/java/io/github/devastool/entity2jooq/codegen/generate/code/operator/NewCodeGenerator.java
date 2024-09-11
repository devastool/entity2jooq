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

import io.github.devastool.entity2jooq.codegen.generate.code.CodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.CodeTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link OperatorCodeGenerator} to generate code of instance creation.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class NewCodeGenerator implements OperatorCodeGenerator {
  private List<OperatorCodeGenerator> genericTypes;
  private final Class<?> type;
  private final Collection<OperatorCodeGenerator> args;

  private static final String NEW_KEYWORD = "new";
  private static final String PARAMS_BEGIN = "(";
  private static final String PARAMS_END = ")";
  private static final String GENERICS_BEGIN = "<";
  private static final String GENERICS_END = ">";
  private static final String SEPARATOR = ", ";

  /**
   * Constructs new instance of {@link NewCodeGenerator}.
   *
   * @param type type for new operator
   */
  public NewCodeGenerator(Class<?> type, OperatorCodeGenerator... args) {
    this.type = type;
    this.args = Arrays.asList(args);
  }

  /**
   * Sets types to replace expression of generics.
   *
   * @param genericTypes types to replace expression of generics
   */
  public NewCodeGenerator setGenericTypes(Class<?>... genericTypes) {
    if (this.genericTypes == null) {
      this.genericTypes = new ArrayList<>();
    }

    for (Class<?> genericType : genericTypes) {
      this.genericTypes.add(target -> target.write(genericType));
    }
    return this;
  }

  @Override
  public void generate(CodeTarget target) {
    target
        .write(NEW_KEYWORD)
        .space()
        .write(type);

    CodeGenerator separator = codeTarget -> codeTarget.write(SEPARATOR);
    if (genericTypes != null) {
      target
          .write(GENERICS_BEGIN)
          .writeAll(genericTypes, separator)
          .write(GENERICS_END);
    }
    target
        .write(PARAMS_BEGIN)
        .writeAll(args, separator)
        .write(PARAMS_END);
  }
}
