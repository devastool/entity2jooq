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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link InvokeMethodCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class InvokeMethodCodeGeneratorTest {
  private static final String METHOD_NAME = "call";
  private static final String EXPECTED = "call(\"Test\")";
  private static final String WITHOUT_PARAMS_EXPECTED = "call()";

  @Test
  void generateTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    InvokeMethodCodeGenerator generator = new InvokeMethodCodeGenerator(
        METHOD_NAME,
        target -> target.write("\"Test\"")
    );
    generator.generate(buffer);

    Assertions.assertEquals(EXPECTED, buffer.getBuffer());
  }

  @Test
  void generateWithoutParamsTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    InvokeMethodCodeGenerator generator = new InvokeMethodCodeGenerator(METHOD_NAME);
    generator.generate(buffer);

    Assertions.assertEquals(WITHOUT_PARAMS_EXPECTED, buffer.getBuffer());
  }
}