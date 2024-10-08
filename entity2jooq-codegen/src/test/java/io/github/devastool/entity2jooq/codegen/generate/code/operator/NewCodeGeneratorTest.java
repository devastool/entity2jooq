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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link NewCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class NewCodeGeneratorTest {
  private static final String EXPECTED = "new java.util.ArrayList()";
  private static final String WITH_GENERICS_EXPECTED =
      "new java.util.function.Function<java.math.BigDecimal, java.math.BigDecimal>()";

  @Test
  void generateTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    NewCodeGenerator operator = new NewCodeGenerator(ArrayList.class);
    operator.generate(target);

    Assertions.assertEquals(EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithGenericsTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    NewCodeGenerator operator = new NewCodeGenerator(Function.class);
    operator.setGenericTypes(BigDecimal.class);
    operator.setGenericTypes(BigDecimal.class);
    operator.generate(target);

    Assertions.assertEquals(WITH_GENERICS_EXPECTED, target.getBuffer());
  }
}