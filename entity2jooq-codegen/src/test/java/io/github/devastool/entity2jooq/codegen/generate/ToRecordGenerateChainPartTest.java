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
import org.jooq.meta.ColumnDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link ToRecordGenerateChainPart}.
 *
 * @author Andrey_Yurzanov
 */
class ToRecordGenerateChainPartTest extends CommonFactoryTest {
  private static final String EXPECTED = String.join(
      "",
      "    public org.jooq.Record toRecord(io.github.devastool.entity2jooq.codegen.model.TestEntity entity) {",
      System.lineSeparator(),
      "        org.jooq.Record record = TEST_ENTITY.newRecord();",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.BIG_DECIMAL_FIELD, entity.getBigDecimalField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.BOOLEAN_FIELD, entity.getBooleanField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.DATE_FIELD, entity.getDateField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.DOUBLE_FIELD, entity.getDoubleField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.ENTITY_NAME, entity.getStringField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.FLOAT_FIELD, entity.getFloatField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.INT_FIELD, entity.getIntField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.LOCAL_DATE_FIELD, entity.getLocalDateField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.LOCAL_DATE_TIME_FIELD, entity.getLocalDateTimeField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.LOCAL_TIME_FIELD, entity.getLocalTimeField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.LONG_FIELD, entity.getLongField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.OFFSET_DATE_TIME_FIELD, entity.getOffsetDateTimeField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.OFFSET_TIME_FIELD, entity.getOffsetTimeField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.SHORT_FIELD, entity.getShortField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.SQL_DATE_FIELD, entity.getSqlDateField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.TIME_FIELD, entity.getTimeField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.TIMESTAMP_FIELD, entity.getTimestampField());",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY.UUID_FIELD, entity.getUuidField());",
      System.lineSeparator(),
      "        return record;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  private static final String EXPECTED_WITH_CONVERTERS = String.join(
      "",
      "    public org.jooq.Record toRecord(io.github.devastool.entity2jooq.codegen.model.TestEntityConverter entity) {",
      System.lineSeparator(),
      "        org.jooq.Record record = TEST_ENTITY_CONVERTER.newRecord();",
      System.lineSeparator(),
      "        record.setValue(TEST_ENTITY_CONVERTER.INT_FIELD, entity.getIntField(), STRING_TO_INTEGER_CONVERTER);",
      System.lineSeparator(),
      "        return record;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  @Test
  void generateTest() {
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ToRecordGenerateChainPart part = new ToRecordGenerateChainPart();
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
    ToRecordGenerateChainPart part = new ToRecordGenerateChainPart();
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

    new ToRecordGenerateChainPart().generate(context);
    Assertions.assertEquals(EXPECTED_WITH_CONVERTERS, target.getBuffer());
  }
}