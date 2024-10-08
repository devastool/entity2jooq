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

import io.github.devastool.entity2jooq.codegen.generate.code.operator.EndLineCodeOperator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.ReturnCodeGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link MethodCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class MethodCodeGeneratorTest {
  private static final String METHOD_NAME = "testMethod";
  private static final String FIRST_PARAM_NAME = "firstTestParam";
  private static final String SECOND_PARAM_NAME = "secondTestParam";
  private static final String WITH_RETURN_TYPE_EXPECTED = String.join(
      "",
      "public String testMethod(String firstTestParam, Integer secondTestParam) {",
      System.lineSeparator(),
      "return \"Test\";",
      System.lineSeparator(),
      "}",
      System.lineSeparator()
  );
  private static final String WITHOUT_RETURN_TYPE_EXPECTED = String.join(
      "",
      "public void testMethod() {",
      System.lineSeparator(),
      "}",
      System.lineSeparator()
  );

  @Test
  void generateWithReturnTypeTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    new MethodCodeGenerator(METHOD_NAME)
        .setReturnType(String.class)
        .setParam(FIRST_PARAM_NAME, String.class)
        .setParam(SECOND_PARAM_NAME, Integer.class)
        .setOperator(new EndLineCodeOperator(new ReturnCodeGenerator("\"Test\"")))
        .generate(target);

    Assertions.assertEquals(WITH_RETURN_TYPE_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithoutReturnTypeTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    new MethodCodeGenerator(METHOD_NAME)
        .generate(target);

    Assertions.assertEquals(WITHOUT_RETURN_TYPE_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithVoidReturnTypeTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    MethodCodeGenerator generator = new MethodCodeGenerator(METHOD_NAME)
        .setReturnType(Void.class);

    generator.generate(target);
    Assertions.assertEquals(WITHOUT_RETURN_TYPE_EXPECTED, target.getBuffer());

    target = new BufferedCodeTarget();
    generator = generator.setReturnType(void.class);

    generator.generate(target);
    Assertions.assertEquals(WITHOUT_RETURN_TYPE_EXPECTED, target.getBuffer());
  }
}