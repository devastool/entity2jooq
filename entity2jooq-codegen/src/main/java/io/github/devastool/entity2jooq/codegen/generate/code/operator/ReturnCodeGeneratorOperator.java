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
 * Implementation of {@link CodeGeneratorOperator} to generate the 'return' operator.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class ReturnCodeGeneratorOperator implements CodeGeneratorOperator {
  private final String returnValue;

  /**
   * Constructs new instance of {@link ReturnCodeGeneratorOperator}.
   *
   * @param returnValue value to return
   */
  public ReturnCodeGeneratorOperator(String returnValue) {
    this.returnValue = returnValue;
  }

  private static final String RETURN_KEYWORD = "return";
  private static final String END_OPERATOR = ";";

  @Override
  public void generate(CodeTarget target) {
    target
        .write(RETURN_KEYWORD)
        .space()
        .write(returnValue)
        .write(END_OPERATOR);
  }
}
