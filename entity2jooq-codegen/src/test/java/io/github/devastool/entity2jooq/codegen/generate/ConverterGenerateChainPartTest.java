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

import io.github.devastool.entity2jooq.codegen.definition.factory.CommonFactoryTest;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityTableDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.IndentCodeTarget;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntityConverter;
import io.github.devastool.entity2jooq.codegen.model.TestEntityDefaultConverter;
import io.github.devastool.entity2jooq.codegen.model.TestEntityDisabledMapping;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link ConverterGenerateChainPart}.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
class ConverterGenerateChainPartTest extends CommonFactoryTest {
  private static final String EXPECTED = String.join(
      "",
      "    public final io.github.devastool.entity2jooq.codegen.model.converter.StringToIntegerConverter STRING_TO_INTEGER_CONVERTER = new io.github.devastool.entity2jooq.codegen.model.converter.StringToIntegerConverter();",
      System.lineSeparator()

  );
  private static final String GENERICS_EXPECTED = String.join(
      "",
      "    public final io.github.devastool.entity2jooq.annotation.type.converter.EnumConverter<io.github.devastool.entity2jooq.codegen.model.TestEnum> TEST_ENUM_ENUM_CONVERTER = new io.github.devastool.entity2jooq.annotation.type.converter.EnumConverter<io.github.devastool.entity2jooq.codegen.model.TestEnum>(io.github.devastool.entity2jooq.codegen.model.TestEnum.class);",
      System.lineSeparator()
  );

  @Test
  void generateTest() {
    CodegenProperties properties = getProperties();
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ConverterGenerateChainPart part = new ConverterGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntityConverter.class, properties),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals(EXPECTED, target.getBuffer());
  }

  @Test
  void generateWithDisabledMappingTest() {
    CodegenProperties properties = getProperties();
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ConverterGenerateChainPart part = new ConverterGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntityDisabledMapping.class, properties),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals("", target.getBuffer());
  }

  @Test
  void generateWithoutConverterTest() {
    CodegenProperties properties = getProperties();
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ConverterGenerateChainPart part = new ConverterGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntity.class, properties),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals("", target.getBuffer());
  }

  @Test
  void generateToTypeGenericTest() {
    CodegenProperties properties = getProperties();
    EntityTableDefinitionFactory factory = getTableFactory();

    BufferedCodeTarget target = new BufferedCodeTarget();
    ConverterGenerateChainPart part = new ConverterGenerateChainPart();
    part.generate(
        new GenerateContext(
            factory.build(TestEntityDefaultConverter.class, properties),
            new IndentCodeTarget(target)
        )
    );

    Assertions.assertEquals(GENERICS_EXPECTED, target.getBuffer());
  }
}