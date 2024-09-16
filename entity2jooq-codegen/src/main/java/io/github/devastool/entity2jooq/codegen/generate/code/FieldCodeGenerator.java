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
 * Generator of source code of public, final field.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class FieldCodeGenerator implements CodeGenerator {
  private List<CodeGenerator> genericTypes;
  private final String name;
  private final Class<?> type;
  private final OperatorCodeGenerator assignment;

  private static final String ACCESS_MODIFIER = "public";
  private static final String FINAL_MODIFIER = "final";
  private static final String ASSIGN_OPERATOR = "=";
  private static final String GENERICS_BEGIN = "<";
  private static final String GENERICS_END = ">";
  private static final String GENERICS_SEPARATOR = ", ";
  private static final String END_OPERATOR = ";";

  /**
   * Constructs new instance of {@link FieldCodeGenerator}.
   *
   * @param name name of the field
   * @param type type of the field
   */
  public FieldCodeGenerator(String name, Class<?> type) {
    this(name, type, null);
  }

  /**
   * Constructs new instance of {@link FieldCodeGenerator}.
   *
   * @param name       name of the field
   * @param type       type of the field
   * @param assignment assignment value to the field
   */
  public FieldCodeGenerator(String name, Class<?> type, OperatorCodeGenerator assignment) {
    this.name = name;
    this.type = type;
    this.assignment = assignment;
  }

  /**
   * Sets types to replace expression of generics.
   *
   * @param genericTypes types to replace expression of generics
   */
  public FieldCodeGenerator setGenericTypes(Class<?>... genericTypes) {
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
        .write(ACCESS_MODIFIER)
        .space()
        .write(FINAL_MODIFIER)
        .space()
        .write(type);

    // Generics
    if (genericTypes != null) {
      target
          .write(GENERICS_BEGIN)
          .writeAll(genericTypes, codeTarget -> codeTarget.write(GENERICS_SEPARATOR))
          .write(GENERICS_END);
    }

    // Name of the field
    target
        .space()
        .write(name);

    // Assignment operator
    if (assignment != null) {
      target
          .space()
          .write(ASSIGN_OPERATOR)
          .space()
          .write(assignment);
    }
    target.writeln(END_OPERATOR);
  }
}
