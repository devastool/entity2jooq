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
 * Tests of {@link VarMemberCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class VarMemberCodeGeneratorTest {
  private static final String VARIABLE_NAME = "testVar";
  private static final String MEMBER_NAME = "testField";
  private static final String EXPECTED = String.join(".", VARIABLE_NAME, MEMBER_NAME);

  @Test
  void generateTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();

    VarMemberCodeGenerator generator = new VarMemberCodeGenerator(
        VARIABLE_NAME,
        target -> target.write(MEMBER_NAME)
    );
    generator.generate(buffer);

    Assertions.assertEquals(EXPECTED, buffer.getBuffer());
  }
}