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

package io.github.devastool.entity2jooq.codegen.generate;

import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link GenerateContext}.
 *
 * @author Andrey_Yurzanov
 */
class GenerateContextTest {
  private static final BufferedCodeTarget TARGET = new BufferedCodeTarget();
  private static final EntityTableDefinition TABLE_DEFINITION = new EntityTableDefinition(
      new EntitySchemaDefinition(new Entity2JooqDatabase(), "test_schema"),
      "test_table"
  );
  private static final String VARIABLE_KEY = "key";
  private static final String VARIABLE_NOT_FOUND_KEY = "key";
  private static final String VARIABLE_VALUE = "value";
  private final GenerateContext context = new GenerateContext(TABLE_DEFINITION, TARGET);

  @Test
  void getTableTest() {
    Assertions.assertEquals(TABLE_DEFINITION, context.getTable());
  }

  @Test
  void getTargetTest() {
    Assertions.assertEquals(TARGET, context.getTarget());
  }

  @Test
  void getVariableTest() {
    context.setVariable(VARIABLE_KEY, VARIABLE_VALUE);
    Assertions.assertEquals(VARIABLE_VALUE, context.getVariable(VARIABLE_KEY, String.class));
  }

  @Test
  void getVariableNotFoundTest() {
    Assertions.assertNull(context.getVariable(VARIABLE_NOT_FOUND_KEY, String.class));
  }

  @Test
  void setVariableTest() {
    Assertions.assertDoesNotThrow(() -> context.setVariable(VARIABLE_KEY, VARIABLE_VALUE));
  }
}