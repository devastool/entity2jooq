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

import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link VarDefCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class VarDefCodeGeneratorTest {
  private static final String VARIABLE_NAME = "myVariable";
  private static final String WITHOUT_ASSIGNMENT_EXPECTED = "java.util.ArrayList myVariable";
  private static final String WITH_ASSIGNMENT_EXPECTED =
      "java.util.ArrayList myVariable = new java.util.ArrayList()";

  @Test
  void generateWithoutAssignmentTest() {
    VarDefCodeGenerator operator = new VarDefCodeGenerator(
        VARIABLE_NAME,
        new TypeCodeGenerator(ArrayList.class)
    );

    BufferedCodeTarget target = new BufferedCodeTarget();
    operator.generate(target);

    Assertions.assertEquals(WITHOUT_ASSIGNMENT_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithAssignmentTest() {
    VarDefCodeGenerator operator = new VarDefCodeGenerator(
        VARIABLE_NAME,
        new TypeCodeGenerator(ArrayList.class),
        new NewCodeGenerator(ArrayList.class)
    );

    BufferedCodeTarget target = new BufferedCodeTarget();
    operator.generate(target);

    Assertions.assertEquals(WITH_ASSIGNMENT_EXPECTED, target.getBuffer());
  }
}