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

package io.github.devastool.entity2jooq.example;

import static org.jooq.generated.test_inherit_schema.Tables.TEST_INHERIT_ENTITY;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UpdateSetMoreStep;
import org.jooq.generated.DefaultCatalog;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests of {@link TestInheritEntity} DDL.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@TestMethodOrder(OrderAnnotation.class)
public class TestInheritEntityTest {
  private static Connection connection;
  private static final String DB_USER = "SA";
  private static final String DB_PASSWORD = "";
  private static final String DB_URL = String.join(
      "",
      "jdbc:hsqldb:mem:mydb;",
      "shutdown=true;"
  );
  private static final List<TestInheritEntity> DATA = Arrays.asList(
      new TestInheritEntity(),
      new TestInheritEntity(),
      new TestInheritEntity()
  );

  @BeforeAll
  static void init() throws SQLException {
    connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.TEST_INHERIT_SCHEMA)
        .execute();

    Table<Record> table = TEST_INHERIT_ENTITY.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_INHERIT_ENTITY.INHERIT_FIELD)
        .execute();
  }

  @AfterAll
  static void destroy() throws SQLException {
    connection.close();
  }

  @Test
  @Order(1)
  void insertTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    var insert = context.insertInto(TEST_INHERIT_ENTITY);
    var iterator = DATA.iterator();
    while (iterator.hasNext()) {
      Record record = TEST_INHERIT_ENTITY.toRecord(iterator.next());
      InsertValuesStepN<Record> insertStep = insert
          .columns(record.fields())
          .values(record.intoList());

      if (!iterator.hasNext()) {
        Assertions.assertDoesNotThrow(insertStep::execute);
      }
    }
  }

  @Test
  @Order(2)
  void selectTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    var select = context
        .select(
            TEST_INHERIT_ENTITY.INHERIT_FIELD
        )
        .from(TEST_INHERIT_ENTITY)
        .where(TEST_INHERIT_ENTITY.INHERIT_FIELD.isNotNull());

    List<TestInheritEntity> results = select.fetch(
        records -> new TestInheritEntity(
            records.get(TEST_INHERIT_ENTITY.INHERIT_FIELD)
        )
    );

    for (TestInheritEntity entity : DATA) {
      String inheritField = entity.getInheritField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getInheritField(), inheritField))
      );
    }
  }

  @Test
  @Order(3)
  void updateTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    ArrayList<UpdateSetMoreStep<?>> updates = new ArrayList<>();
    for (TestInheritEntity entity : DATA) {
      updates.add(
          context
              .update(TEST_INHERIT_ENTITY)
              .set(TEST_INHERIT_ENTITY.INHERIT_FIELD, entity.getInheritField())
      );
    }
    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());
  }

  @Test
  @Order(4)
  void deleteTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    DeleteConditionStep<Record> delete = context
        .delete(TEST_INHERIT_ENTITY)
        .where(
            TEST_INHERIT_ENTITY.INHERIT_FIELD.in(
                DATA
                    .stream()
                    .map(TestInheritEntity::getInheritField)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);
  }
}
