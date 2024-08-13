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

package io.github.devastool.entity2jooq.codegen;

import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import java.io.File;
import java.util.ArrayList;
import org.jooq.SQLDialect;
import org.jooq.codegen.GeneratorStrategy.Mode;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests of {@link Entity2JooqJavaGenerator}.
 *
 * @author Evgeniy_Gerasimov
 */
class Entity2JooqJavaGeneratorTest {
  private final Entity2JooqJavaGenerator generator = new Entity2JooqJavaGenerator();

  private static final String FILE_NAME = "Test.java";

  @Test
  void generateRecordsTest() {
    Assertions.assertFalse(generator.generateRecords());
  }

  @Test
  void getJavaTypeTest() {
    var schema = new EntitySchemaDefinition(new Entity2JooqDatabase(), "testName");
    var javaType = generator.getJavaType(
        new EntityDataTypeDefinition(schema, String.class, null, null)
    );

    Assertions.assertEquals(String.class.getName(), javaType);
  }

  @Test
  void getJavaTypeTwoParamTest() {
    var schema = new EntitySchemaDefinition(new Entity2JooqDatabase(), "testName");
    var javaType = generator.getJavaType(
        new EntityDataTypeDefinition(schema, String.class, null, null),
        Mode.DEFAULT
    );

    Assertions.assertEquals(String.class.getName(), javaType);
  }

  @Test
  void getJavaTypeReferenceTest() {
    var schema = new EntitySchemaDefinition(new Entity2JooqDatabase(), "testName");
    var javaTypeReference = generator.getJavaTypeReference(
        new Entity2JooqDatabase(),
        new EntityDataTypeDefinition(schema, String.class, SQLDialect.POSTGRES.getName(), null)
    );

    Assertions.assertEquals(
        "new org.jooq.impl.DefaultDataType(org.jooq.SQLDialect.POSTGRES, java.lang.String.class, \"OTHER\")",
        javaTypeReference
    );
  }

  @Test
  void getJavaTypeReferenceDefaultTypeTest() {
    Entity2JooqDatabase database = new Entity2JooqDatabase();
    var schema = new EntitySchemaDefinition(database, "testName");
    Assertions.assertThrows(IllegalArgumentException.class, () -> generator.getJavaTypeReference(
            database,
            new DefaultDataTypeDefinition(database, schema, "integer")
        )
    );
  }

  @Test
  void generateTableClassFooterTest(@TempDir File root) {
    BufferedCodeTarget target = new BufferedCodeTarget();
    generator.generateTableClassFooter(
        new EntityTableDefinition(
            new EntitySchemaDefinition(new Entity2JooqDatabase(), "test_schema"),
            "test_table",
            Object.class,
            new ArrayList<>()
        ),
        new JavaWriter(new File(root, FILE_NAME), null) {
          @Override
          public JavaWriter print(String value) {
            target.write(value);
            return this;
          }
        }
    );

    Assertions.assertFalse(target.getBuffer().isEmpty());
  }
}