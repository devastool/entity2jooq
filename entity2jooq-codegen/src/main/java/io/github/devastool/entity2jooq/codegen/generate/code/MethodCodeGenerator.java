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

package io.github.devastool.entity2jooq.codegen.generate.code;

import io.github.devastool.entity2jooq.codegen.generate.code.operator.OperatorCodeGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator of method source code.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class MethodCodeGenerator implements CodeGenerator {
  private String name;
  private Class<?> returnType;
  private List<CodeGenerator> params;
  private List<OperatorCodeGenerator> operators;

  private static final String ACCESS_KEYWORD = "public";
  private static final String PARAMS_BEGIN = "(";
  private static final String PARAMS_END = ")";
  private static final String PARAMS_SEPARATOR = ",";
  private static final String BODY_BEGIN = "{";
  private static final String BODY_END = "}";

  /**
   * Sets name of the method.
   *
   * @param name name of the method
   * @return current instance
   */
  public MethodCodeGenerator setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets type of the method return.
   *
   * @param returnType type of the method return
   * @return current instance
   */
  public MethodCodeGenerator setReturnType(Class<?> returnType) {
    this.returnType = returnType;
    return this;
  }

  /**
   * Append parameter of the method.
   *
   * @param name name of the parameter
   * @param type type of the parameter
   * @return current instance
   */
  public MethodCodeGenerator setParam(String name, Class<?> type) {
    if (this.params == null) {
      this.params = new ArrayList<>();
    }

    this.params.add(target ->
        target
            .write(type)
            .space()
            .write(name)
    );
    return this;
  }

  /**
   * Append operator of method's body.
   *
   * @param operator operator of method's body
   * @return current instance
   */
  public MethodCodeGenerator setOperator(OperatorCodeGenerator operator) {
    if (this.operators == null) {
      this.operators = new ArrayList<>();
    }
    this.operators.add(operator);

    return this;
  }

  @Override
  public void generate(CodeTarget target) {
    target
        .write(ACCESS_KEYWORD)
        .space();

    if (isVoid(returnType)) {
      target.write(void.class.getSimpleName());
    } else {
      target.write(returnType);
    }

    target
        .space()
        .write(name)
        .write(PARAMS_BEGIN)
        .writeAll(params, codeTarget -> codeTarget.write(PARAMS_SEPARATOR))
        .write(PARAMS_END)
        .space()
        .writeln(BODY_BEGIN)
        .writeAll(operators, null)
        .writeln(BODY_END);
  }

  // Checks type, it returns true if type is void, else false
  private boolean isVoid(Class<?> type) {
    return type == null || type == void.class || type == Void.class;
  }
}
