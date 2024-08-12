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

import io.github.devastool.entity2jooq.codegen.generate.code.operator.CodeGeneratorOperator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Generator of method source code.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class MethodCodeGenerator implements CodeGenerator {
  private String name;
  private Class<?> returnType;
  private Map<String, Class<?>> params;
  private List<CodeGeneratorOperator> operators;

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
      this.params = new HashMap<>();
    }
    this.params.put(name, type);

    return this;
  }

  /**
   * Append operator of method's body.
   *
   * @param operator operator of method's body
   * @return current instance
   */
  public MethodCodeGenerator setOperator(CodeGeneratorOperator operator) {
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
      target.write(returnType.getCanonicalName());
    }
    target
        .space()
        .write(name);

    generateParams(target);
    target.space();
    generateBody(target);
  }

  private boolean isVoid(Class<?> type) {
    return type == null || type == void.class || type == Void.class;
  }

  private void generateParams(CodeTarget target) {
    target.write(PARAMS_BEGIN);
    if (params != null && !params.isEmpty()) {
      Iterator<Entry<String, Class<?>>> iterator = params.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<String, Class<?>> param = iterator.next();
        String paramName = param.getKey();
        Class<?> paramType = param.getValue();

        target
            .write(paramType.getCanonicalName())
            .space()
            .write(paramName);

        if (iterator.hasNext()) {
          target
              .write(PARAMS_SEPARATOR)
              .space();
        }
      }
    }
    target.write(PARAMS_END);
  }

  private void generateBody(CodeTarget target) {
    target.writeln(BODY_BEGIN);
    if (operators != null && !operators.isEmpty()) {
      for (CodeGenerator operator : operators) {
        operator.generate(target);
        target.writeln();
      }
    }
    target.writeln(BODY_END);
  }
}
