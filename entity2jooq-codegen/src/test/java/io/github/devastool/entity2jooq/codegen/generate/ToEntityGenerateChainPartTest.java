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

import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityColumnDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityDataTypeDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntitySchemaDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityTableDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.FactoryContext;
import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.IndentCodeTarget;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link ToEntityGenerateChainPart}.
 *
 * @author Andrey_Yurzanov
 */
class ToEntityGenerateChainPartTest {
  private final FactoryContext context = new FactoryContext();
  private final EntityTableDefinitionFactory factory = new EntityTableDefinitionFactory(
      new EntitySchemaDefinitionFactory(context),
      new EntityColumnDefinitionFactory(new EntityDataTypeDefinitionFactory(context), context),
      context
  );
  private static final CodegenProperties PROPERTIES = new CodegenProperties(
      Map.of(
          CodegenProperty.DIALECT, "",
          CodegenProperty.DATABASE, new Entity2JooqDatabase()
      )
  );
  private static final String EXPECTED = String.join(
      "",
      "    public ",
      "io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntity ",
      "toEntity(org.jooq.Record record) {",
      System.lineSeparator(),
      "        ",
      "io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntity ",
      "entity = new io.github.devastool.entity2jooq.codegen.generate.",
      "ToEntityGenerateChainPartTest.TestEntity();",
      System.lineSeparator(),
      "        entity.setId(record.get(TEST_ENTITY.ID));",
      System.lineSeparator(),
      "        entity.setCount(record.get(TEST_ENTITY.COUNT));",
      System.lineSeparator(),
      "        return entity;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  @Test
  void generateTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    ToEntityGenerateChainPart part = new ToEntityGenerateChainPart();
    part.generate(factory.build(TestEntity.class, PROPERTIES), new IndentCodeTarget(target));

    Assertions.assertEquals(EXPECTED, target.getBuffer());
  }

  @Table
  static class TestEntity {
    private String id;
    private Integer count;
  }
}