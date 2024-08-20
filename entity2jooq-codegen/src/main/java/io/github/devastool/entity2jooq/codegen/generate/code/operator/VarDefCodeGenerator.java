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

/**
 * Implementation of {@link OperatorCodeGenerator} to generate variable definition.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class VarDefCodeGenerator implements OperatorCodeGenerator {
  private final String name;
  private final Class<?> type;

  private static final String NEW_KEYWORD = "new";
  private static final String ASSIGN_OPERATOR = "=";
  private static final String PARAMS_BEGIN = "(";
  private static final String PARAMS_END = ")";

  /**
   * Constructs new instance of {@link VarDefCodeGenerator}.
   *
   * @param name name of the variable
   * @param type type of the variable
   */
  public VarDefCodeGenerator(String name, Class<?> type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public void generate(CodeTarget target) {
    target
        .write(type.getCanonicalName())
        .space()
        .write(name)
        .space()
        .write(ASSIGN_OPERATOR)
        .space()
        .write(NEW_KEYWORD)
        .space()
        .write(type.getCanonicalName())
        .write(PARAMS_BEGIN)
        .write(PARAMS_END);
  }
}
