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

package io.github.devastool.entity2jooq.codegen.definition.factory;

import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntitySchemaDefinitionFactory.DefaultSchemaResolver;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntityEmptySchema;
import io.github.devastool.entity2jooq.codegen.model.TestEntitySchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntitySchemaDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntitySchemaDefinitionFactoryTest extends CommonFactoryTest {
  @Test
  void buildSuccessTest() {
    EntitySchemaDefinitionFactory factory = getSchemaFactory();
    EntitySchemaDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntitySchema.class, getProperties())
    );
    Assertions.assertNotNull(built);

    Schema annotation = TestEntitySchema.class.getAnnotation(Schema.class);
    Assertions.assertEquals(annotation.value(), built.getName());
  }

  @Test
  void buildEmptySchemaAnnotationTest() {
    EntitySchemaDefinitionFactory factory = getSchemaFactory();
    EntitySchemaDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityEmptySchema.class, getProperties())
    );
    Assertions.assertNotNull(built);

    String packageName = TestEntityEmptySchema.class.getPackageName();
    Assertions.assertTrue(packageName.endsWith(built.getName()));
  }

  @Test
  void buildWithoutSchemaAnnotationTest() {
    EntitySchemaDefinitionFactory factory = getSchemaFactory();
    EntitySchemaDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntity.class, getProperties())
    );
    Assertions.assertNotNull(built);

    String packageName = TestEntity.class.getPackageName();
    Assertions.assertTrue(packageName.endsWith(built.getName()));
  }

  @Test
  void resolveDefaultSchemaTest() {
    String packageName = TestEntity.class.getPackage().getName();
    String lastName = packageName.substring(packageName.lastIndexOf('.') + 1);

    DefaultSchemaResolver resolver = new DefaultSchemaResolver();
    Assertions.assertEquals(lastName, resolver.apply(TestEntity.class, packageName));
    Assertions.assertEquals(lastName, resolver.apply(TestEntity.class, lastName));
  }
}