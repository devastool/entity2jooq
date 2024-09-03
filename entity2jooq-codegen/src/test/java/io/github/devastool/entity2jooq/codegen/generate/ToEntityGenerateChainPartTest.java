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

import io.github.devastool.entity2jooq.annotation.Embedded;
import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.type.Type;
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
import org.jooq.Converter;
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
  private static final String WITHOUT_CONVERTERS_EXPECTED = String.join(
      "",
      "    public io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntityWithoutConverters toEntity(org.jooq.Record record) {",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntityWithoutConverters entity = new io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntityWithoutConverters();",
      System.lineSeparator(),
      "        entity.setCount(record.get(TEST_ENTITY_WITHOUT_CONVERTERS.COUNT));",
      System.lineSeparator(),
      "        entity.setId(record.get(TEST_ENTITY_WITHOUT_CONVERTERS.ID));",
      System.lineSeparator(),
      "        return entity;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()
  );

  private static final String WITH_CONVERTERS_EXPECTED = String.join(
      "",
      "    public io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntity toEntity(org.jooq.Record record) {",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntity entity = new io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEntity();",
      System.lineSeparator(),
      "        io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEmbedded embedded_0 = new io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPartTest.TestEmbedded();",
      System.lineSeparator(),
      "        entity.setEmbedded(embedded_0);",
      System.lineSeparator(),
      "        entity.setCount(record.get(TEST_ENTITY.COUNT, null));",
      System.lineSeparator(),
      "        embedded_0.setName(record.get(TEST_ENTITY.EMBEDDED_NAME));",
      System.lineSeparator(),
      "        entity.setId(record.get(TEST_ENTITY.ID, null));",
      System.lineSeparator(),
      "        entity.setSecondId(record.get(TEST_ENTITY.SECOND_ID, null));",
      System.lineSeparator(),
      "        return entity;",
      System.lineSeparator(),
      "    }",
      System.lineSeparator()

  );

  @Test
  void generateWithoutConvertersTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    ToEntityGenerateChainPart part = new ToEntityGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntityWithoutConverters.class, PROPERTIES),
            new IndentCodeTarget(target)
        )
    );

    //Assertions.assertEquals(WITHOUT_CONVERTERS_EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithConvertersTest() {
    BufferedCodeTarget target = new BufferedCodeTarget();
    ToEntityGenerateChainPart part = new ToEntityGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntity.class, PROPERTIES),
            new IndentCodeTarget(target)
        )
    );

    //Assertions.assertEquals(WITH_CONVERTERS_EXPECTED, target.getBuffer());
  }

  @Table
  static class TestEntity {
    @Type(converter = StringToInteger.class)
    private String id;
    @Type(converter = StringToInteger.class)
    private String secondId;
    @Type(converter = IntegerToString.class)
    private Integer count;
    private TestEmbedded embedded;
  }

  @Table
  static class TestEntityWithoutConverters {
    private String id;
    private Integer count;
  }

  public static class StringToInteger implements Converter<String, Integer> {
    @Override
    public Integer from(String value) {
      return Integer.parseInt(value);
    }

    @Override
    public String to(Integer value) {
      return value.toString();
    }

    @Override
    public Class<String> fromType() {
      return String.class;
    }

    @Override
    public Class<Integer> toType() {
      return Integer.class;
    }
  }

  public static class IntegerToString implements Converter<Integer, String> {
    @Override
    public String from(Integer value) {
      return value.toString();
    }

    @Override
    public Integer to(String value) {
      return Integer.parseInt(value);
    }

    @Override
    public Class<Integer> fromType() {
      return Integer.class;
    }

    @Override
    public Class<String> toType() {
      return String.class;
    }
  }

  @Embedded
  static class TestEmbedded{
    private String name;

  }
}