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
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link TypeCodeGenerator}.
 *
 * @author Andrey_Yurzanov
 */
class TypeCodeGeneratorTest {
  private static final String EXPECTED = "java.util.Map<String, java.util.List<Integer>>";

  @Test
  void generateTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();

    new TypeCodeGenerator(
        Map.class,
        new TypeCodeGenerator(String.class),
        new TypeCodeGenerator(List.class, new TypeCodeGenerator(Integer.class))
    ).generate(target);

    Assertions.assertEquals(EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithoutGenericsTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();

    TypeCodeGenerator generator = new TypeCodeGenerator(Integer.class);
    generator.generate(target);

    Assertions.assertEquals(Integer.class.getSimpleName(), target.getBuffer());
  }
}