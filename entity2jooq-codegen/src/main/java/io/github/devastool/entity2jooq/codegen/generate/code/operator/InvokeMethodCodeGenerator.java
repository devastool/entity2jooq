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
 * Implementation of {@link OperatorCodeGenerator} to generate method invocation.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class InvokeMethodCodeGenerator implements OperatorCodeGenerator {
  private final String methodName;
  private final Collection<OperatorCodeGenerator> args;

  private static final String PARAMS_BEGIN = "(";
  private static final String PARAMS_END = ")";
  private static final String PARAMS_SEPARATOR = ", ";

  /**
   * Constructs new instance of {@link InvokeMethodCodeGenerator}.
   *
   * @param methodName name of the method
   * @param args       method invocation arguments
   */
  public InvokeMethodCodeGenerator(String methodName, OperatorCodeGenerator... args) {
    this.methodName = methodName;

    if (args.length > 0) {
      this.args = new ArrayList<>(Arrays.asList(args));
    } else {
      this.args = null;
    }
  }

  @Override
  public void generate(CodeTarget target) {
    target
        .write(methodName)
        .write(PARAMS_BEGIN)
        .writeAll(args, codeTarget -> codeTarget.write(PARAMS_SEPARATOR))
        .write(PARAMS_END);
  }
}
