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

import io.github.devastool.entity2jooq.codegen.generate.code.operator.NewCodeGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link FieldCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class FieldCodeGeneratorTest {
  private static final String FIELD_NAME = "TEST_FIELD";
  private static final String WITH_ASSIGNMENT_EXPECTED = String.join(
      "",
      "public final java.util.List TEST_FIELD = new java.util.ArrayList();",
      System.lineSeparator()
  );
  private static final String WITHOUT_ASSIGNMENT_EXPECTED = String.join(
      "",
      "public final java.util.List TEST_FIELD;",
      System.lineSeparator()
  );
  private static final String WITH_GENERICS_EXPECTED = String.join(
      "",
      "public final java.util.function.Function<java.math.BigDecimal, java.math.BigDecimal> TEST_FIELD;",
      System.lineSeparator()
  );

  @Test
  void generateWithAssignmentTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    FieldCodeGenerator generator = new FieldCodeGenerator(
        FIELD_NAME,
        List.class,
        new NewCodeGenerator(ArrayList.class)
    );

    generator.generate(target);
    Assertions.assertEquals(WITH_ASSIGNMENT_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithoutAssignmentTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    FieldCodeGenerator generator = new FieldCodeGenerator(FIELD_NAME, List.class);

    generator.generate(target);
    Assertions.assertEquals(WITHOUT_ASSIGNMENT_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithGenericsTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    FieldCodeGenerator generator = new FieldCodeGenerator(FIELD_NAME, Function.class);
    generator.setGenericTypes(BigDecimal.class);
    generator.setGenericTypes(BigDecimal.class);

    generator.generate(target);
    Assertions.assertEquals(WITH_GENERICS_EXPECTED, target.getBuffer());
  }
}