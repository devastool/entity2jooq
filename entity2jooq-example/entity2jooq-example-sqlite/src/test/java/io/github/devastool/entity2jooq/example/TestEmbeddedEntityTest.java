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

import io.github.devastool.entity2jooq.example.embedded.TestEmbeddedEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sqlite.SQLiteDataSource;

import static org.jooq.generated.test_schema.tables.TestEmbeddedEntity.TEST_EMBEDDED_ENTITY;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Tests of {@link TestEmbeddedEntity} DDL.
 *
 * @author Sergey_Konovalov, Artem_Filkov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEmbeddedEntityTest {
  private static final String DB_URL = "jdbc:sqlite::memory:";
  private static Connection connection;

  private static final List<TestEmbeddedEntity> DATA = Arrays.asList(
      new TestEmbeddedEntity("Pablo", "Pablo work street", "Pablo home street"),
      new TestEmbeddedEntity("Barry", "Barry work street", "Barry home street")
  );

  @BeforeAll
  static void init() throws SQLException {
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl(DB_URL);
    connection = dataSource.getConnection();

    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);
    context
        .createTableIfNotExists(TEST_EMBEDDED_ENTITY.getName())
        .column(TEST_EMBEDDED_ENTITY.NAME.getName(), TEST_EMBEDDED_ENTITY.NAME.getDataType())
        .column(TEST_EMBEDDED_ENTITY.WORK.getName(), TEST_EMBEDDED_ENTITY.WORK.getDataType())
        .column(TEST_EMBEDDED_ENTITY.HOME.getName(), TEST_EMBEDDED_ENTITY.getDataType())
        .execute();
  }

  @AfterAll
  static void destroy() throws SQLException {
    connection.close();
  }

  @Test
  @Order(1)
  void insertTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    var insert = context
        .insertInto(table(TEST_EMBEDDED_ENTITY.getName()))
        .columns(
            field(TEST_EMBEDDED_ENTITY.NAME.getName()),
            field(TEST_EMBEDDED_ENTITY.WORK.getName()),
            field(TEST_EMBEDDED_ENTITY.HOME.getName())
        );

    for (TestEmbeddedEntity entity : DATA) {
      insert.values(
          entity.getName(),
          entity.getWorkCity(),
          entity.getHomeCity()
      );
    }

    Assertions.assertDoesNotThrow(insert::execute);
  }

  @Test
  @Order(2)
  void selectTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    var select = context
        .select(
            field(TEST_EMBEDDED_ENTITY.NAME.getName()),
            field(TEST_EMBEDDED_ENTITY.WORK.getName()),
            field(TEST_EMBEDDED_ENTITY.HOME.getName())
        )
        .from(table(TEST_EMBEDDED_ENTITY.getName()))
        .where(field(TEST_EMBEDDED_ENTITY.NAME.getName()).isNotNull());

    List<TestEmbeddedEntity> results = select.fetch(
        records -> new TestEmbeddedEntity(
            records.get(TEST_EMBEDDED_ENTITY.NAME),
            records.get(TEST_EMBEDDED_ENTITY.WORK),
            records.get(TEST_EMBEDDED_ENTITY.HOME)
        )
    );

    for (TestEmbeddedEntity entity : DATA) {
      String name = entity.getName();
      String workCity = entity.getWorkCity();
      String homeCity = entity.getHomeCity();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(
                  result -> Objects.equals(result.getName(), name)
                      && Objects.equals(result.getWorkCity(), workCity)
                      && Objects.equals(result.getHomeCity(), homeCity)
              )
      );
    }
  }

  @Test
  @Order(3)
  void updateTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEmbeddedEntity entity : DATA) {
      updates.add(
          context
              .update(table(TEST_EMBEDDED_ENTITY.getName()))
              .set(field(TEST_EMBEDDED_ENTITY.NAME.getName()), entity.getName())
              .set(field(TEST_EMBEDDED_ENTITY.WORK.getName()), entity.getWorkCity())
              .where(field(TEST_EMBEDDED_ENTITY.HOME.getName()).eq(entity.getHomeCity()))
      );
    }

    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());
  }

  @Test
  @Order(4)
  void deleteTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    DeleteConditionStep<Record> delete = context
        .delete(table(TEST_EMBEDDED_ENTITY.getName()))
        .where(
            field(TEST_EMBEDDED_ENTITY.NAME.getName()).in(
                DATA
                    .stream()
                    .map(TestEmbeddedEntity::getName)
                    .collect(Collectors.toList())
            )
        );

    Assertions.assertDoesNotThrow(delete::execute);
  }
}