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

import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.definition.factory.CommonFactoryTest;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityTableDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.type.ConverterDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.IndentCodeTarget;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntityConverter;
import io.github.devastool.entity2jooq.codegen.model.TestEntityDisabledMapping;
import io.github.devastool.entity2jooq.codegen.model.TestEntityEmbedded;
import org.jooq.meta.ColumnDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link ToEntityGenerateChainPart}.
 *
 * @author Andrey_Yurzanov
 */
class ToEntityGenerateChainPartTest extends CommonFactoryTest {
  private static final String EXPECTED = String.join(
      "",
      "    public io.github.devastool.entity2jooq.codegen.model.TestEntity toEntity(org.jooq.Record record) {",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.model.TestEntity entity = new io.github.devastool.entity2jooq.codegen.model.TestEntity();",
      System.lineSeparator(),
      "        entity.setBigDecimalField(record.get(TEST_ENTITY.BIG_DECIMAL_FIELD));",
      System.lineSeparator(),
      "        entity.setBooleanField(record.get(TEST_ENTITY.BOOLEAN_FIELD));",
      System.lineSeparator(),
      "        entity.setDateField(record.get(TEST_ENTITY.DATE_FIELD));",
      System.lineSeparator(),
      "        entity.setDoubleField(record.get(TEST_ENTITY.DOUBLE_FIELD));",
      System.lineSeparator(),
      "        entity.setStringField(record.get(TEST_ENTITY.ENTITY_NAME));",
      System.lineSeparator(),
      "        entity.setFloatField(record.get(TEST_ENTITY.FLOAT_FIELD));",
      System.lineSeparator(),
      "        entity.setIntField(record.get(TEST_ENTITY.INT_FIELD));",
      System.lineSeparator(),
      "        entity.setLocalDateField(record.get(TEST_ENTITY.LOCAL_DATE_FIELD));",
      System.lineSeparator(),
      "        entity.setLocalDateTimeField(record.get(TEST_ENTITY.LOCAL_DATE_TIME_FIELD));",
      System.lineSeparator(),
      "        entity.setLocalTimeField(record.get(TEST_ENTITY.LOCAL_TIME_FIELD));",
      System.lineSeparator(),
      "        entity.setLongField(record.get(TEST_ENTITY.LONG_FIELD));",
      System.lineSeparator(),
      "        entity.setOffsetDateTimeField(record.get(TEST_ENTITY.OFFSET_DATE_TIME_FIELD));",
      System.lineSeparator(),
      "        entity.setOffsetTimeField(record.get(TEST_ENTITY.OFFSET_TIME_FIELD));",
      System.lineSeparator(),
      "        entity.setShortField(record.get(TEST_ENTITY.SHORT_FIELD));",
      System.lineSeparator(),
      "        entity.setSqlDateField(record.get(TEST_ENTITY.SQL_DATE_FIELD));",
      System.lineSeparator(),
      "        entity.setTimeField(record.get(TEST_ENTITY.TIME_FIELD));",
      System.lineSeparator(),
      "        entity.setTimestampField(record.get(TEST_ENTITY.TIMESTAMP_FIELD));",
      System.lineSeparator(),
      "        entity.setUuidField(record.get(TEST_ENTITY.UUID_FIELD));",
      System.lineSeparator(),
      "        return entity;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  private static final String WITH_CONVERTERS_EXPECTED = String.join(
      "",
      "    public io.github.devastool.entity2jooq.codegen.model.TestEntityConverter toEntity(org.jooq.Record record) {",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.model.TestEntityConverter entity = new io.github.devastool.entity2jooq.codegen.model.TestEntityConverter();",
      System.lineSeparator(),
      "        entity.setIntField(record.get(TEST_ENTITY_CONVERTER.INT_FIELD, STRING_TO_INTEGER_CONVERTER));",
      System.lineSeparator(),
      "        return entity;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  private static final String WITH_EMBEDDED_EXPECTED = String.join(
      "",
      "    public io.github.devastool.entity2jooq.codegen.model.TestEntityEmbedded toEntity(org.jooq.Record record) {",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.model.TestSecondEmbeddable secondEmbeddable_0 = new io.github.devastool.entity2jooq.codegen.model.TestSecondEmbeddable();",
      System.lineSeparator(),
      "        secondEmbeddable_0.setSecondIntField(record.get(TEST_ENTITY_EMBEDDED.EMBEDDABLE_SECOND_EMBEDDABLE_SECOND_INT_FIELD));",
      System.lineSeparator(),
      "        secondEmbeddable_0.setSecondStringField(record.get(TEST_ENTITY_EMBEDDED.EMBEDDABLE_SECOND_EMBEDDABLE_SECOND_STRING_FIELD));",
      System.lineSeparator(),
      "        secondEmbeddable_0.setSecondStringField(record.get(TEST_ENTITY_EMBEDDED.EMBEDDABLE_SECOND_EMBEDDABLE_SECOND_STRING_FIELD));",
      System.lineSeparator(),
      "        ",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.model.TestEmbeddable embeddable_0 = new io.github.devastool.entity2jooq.codegen.model.TestEmbeddable();",
      System.lineSeparator(),
      "        embeddable_0.setIntField(record.get(TEST_ENTITY_EMBEDDED.EMBEDDABLE_INT_FIELD));",
      System.lineSeparator(),
      "        embeddable_0.setSecondEmbeddable(secondEmbeddable_0);",
      System.lineSeparator(),
      "        embeddable_0.setStringField(record.get(TEST_ENTITY_EMBEDDED.EMBEDDABLE_STRING_FIELD));",
      System.lineSeparator(),
      "        ",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.model.TestEntityEmbedded entity = new io.github.devastool.entity2jooq.codegen.model.TestEntityEmbedded();",
      System.lineSeparator(),
      "        entity.setEmbeddable(embeddable_0);",
      System.lineSeparator(),
      "        return entity;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  @Test
  void generateTest() {
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ToEntityGenerateChainPart part = new ToEntityGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntity.class, getProperties()),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals(EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithDisabledMappingTest() {
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ToEntityGenerateChainPart part = new ToEntityGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntityDisabledMapping.class, getProperties()),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals("", target.getBuffer());
  }

  @Test
  void generateWithConvertersTest() {
    EntityTableDefinitionFactory factory = getTableFactory();
    SnakeCaseStrategy naming = new SnakeCaseStrategy(true);

    BufferedCodeTarget target = new BufferedCodeTarget();
    EntityTableDefinition definition = factory.build(TestEntityConverter.class, getProperties());

    GenerateContext context = new GenerateContext(definition, new IndentCodeTarget(target));
    for (ColumnDefinition column : definition.getColumns()) {
      EntityDataTypeDefinition type = (EntityDataTypeDefinition) column.getType();
      ConverterDefinition converterDefinition = type.getConverterDefinition();
      Class<?> converterType = converterDefinition.getConverterType();
      context.setVariable(converterDefinition, naming.resolve(converterType.getSimpleName()));
    }

    new ToEntityGenerateChainPart().generate(context);
    Assertions.assertEquals(WITH_CONVERTERS_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithEmbeddedTest() {
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ToEntityGenerateChainPart part = new ToEntityGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntityEmbedded.class, getProperties()),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals(WITH_EMBEDDED_EXPECTED, target.getBuffer());
  }
}