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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Implementation of {@link OperatorCodeGenerator} to generate end operator.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EndLineCodeOperator implements OperatorCodeGenerator {
  private final Collection<OperatorCodeGenerator> operators;

  private static final String END_OPERATOR = ";";

  /**
   * Constructs new instance of {@link EndLineCodeOperator}.
   *
   * @param operators operators on the line
   */
  public EndLineCodeOperator(OperatorCodeGenerator... operators) {
    if (operators != null && operators.length > 0) {
      this.operators = new ArrayList<>(Arrays.asList(operators));
    } else {
      this.operators = null;
    }
  }

  @Override
  public void generate(CodeTarget target) {
    target
        .writeAll(operators, null)
        .writeln(END_OPERATOR);
  }
}
