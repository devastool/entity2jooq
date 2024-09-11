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

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link BufferedCodeTarget}.
 *
 * @author Andrey_Yurzanov
 */
class BufferedCodeTargetTest {
  @Test
  void writeStringTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.write("string");

    Assertions.assertEquals("string", buffer.getBuffer());
  }

  @Test
  void writeTypeTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.write(ArrayList.class);

    Assertions.assertEquals(ArrayList.class.getCanonicalName(), buffer.getBuffer());
  }

  @Test
  void writeLangClassTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.write(String.class);

    Assertions.assertEquals(String.class.getSimpleName(), buffer.getBuffer());
  }

  @Test
  void writeCodeGeneratorTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.write(target -> target.write(String.class));

    Assertions.assertEquals(String.class.getSimpleName(), buffer.getBuffer());
  }

  @Test
  void writelnTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.writeln();

    Assertions.assertEquals(System.lineSeparator(), buffer.getBuffer());
  }

  @Test
  void writeAllTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.writeAll(
        List.of(
            target -> target.write("1"),
            target -> target.write("2")
        ),
        target -> target.write(",")
    );

    Assertions.assertEquals("1,2", buffer.getBuffer());
  }

  @Test
  void writeAllWithoutSeparatorTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.writeAll(
        List.of(
            target -> target.write("1"),
            target -> target.write("2")
        ),
        null
    );

    Assertions.assertEquals("12", buffer.getBuffer());
  }

  @Test
  void writeClassTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.writeClass(String.class);

    Assertions.assertEquals(String.class.getSimpleName() + ".class", buffer.getBuffer());
  }

  @Test
  void spaceTest() {
    BufferedCodeTarget buffer = new BufferedCodeTarget();
    buffer.space();

    Assertions.assertEquals(" ", buffer.getBuffer());
  }
}