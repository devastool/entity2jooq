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

package io.github.devastool.entity2jooq.example.enums;

import static org.jooq.generated.enums.tables.TestEntityEnum.TEST_ENTITY_ENUM;

import io.github.devastool.entity2jooq.annotation.type.converter.EnumConverter;
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
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UpdateConditionStep;
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
 * Tests of {@link TestEntityEnum} DDL.
 *
 * @author Sergey_Konovalov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEntityEnumTest {
  private static Connection connection;
  private static final String DB_USER = "SA";
  private static final String DB_PASSWORD = "";
  private static final String DB_URL = String.join(
      "",
      "jdbc:hsqldb:mem:mydb;",
      "shutdown=true;"
  );

  private static final List<TestEntityEnum> DATA = Arrays.asList(
      new TestEntityEnum(1, TestEnum.FIRST),
      new TestEntityEnum(2, TestEnum.SECOND)
  );

  @BeforeAll
  static void init() throws SQLException {
    connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    DSLContext context = DSL.using(connection);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.ENUMS)
        .execute();

    Table<Record> table = TEST_ENTITY_ENUM.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_ENTITY_ENUM.INTEGER_FIELD)
        .column(TEST_ENTITY_ENUM.ENUM_FIELD)
        .execute();
  }

  @AfterAll
  static void destroy() throws SQLException {
    connection.close();
  }

  @Test
  @Order(1)
  void insertTest() {
    DSLContext context = DSL.using(connection);

    var insert = context
        .insertInto(TEST_ENTITY_ENUM)
        .columns(
            TEST_ENTITY_ENUM.INTEGER_FIELD,
            TEST_ENTITY_ENUM.ENUM_FIELD
        );

    for (TestEntityEnum entity : DATA) {
      insert.values(
          entity.getIntegerField(),
          TEST_ENTITY_ENUM.TEST_ENUM_ENUM_CONVERTER.to(entity.getEnumField())
      );
    }
    Assertions.assertDoesNotThrow(insert::execute);
  }

  @Test
  @Order(2)
  void selectTest() {
    DSLContext context = DSL.using(connection);

    var select = context
        .select(
            TEST_ENTITY_ENUM.INTEGER_FIELD,
            TEST_ENTITY_ENUM.ENUM_FIELD
        )
        .from(TEST_ENTITY_ENUM)
        .where(TEST_ENTITY_ENUM.INTEGER_FIELD.isNotNull());

    List<TestEntityEnum> results = select.fetch(TEST_ENTITY_ENUM::toEntity);

    for (TestEntityEnum entity : DATA) {
      Integer integerField = entity.getIntegerField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getIntegerField(), integerField))
      );
    }
  }

  @Test
  @Order(3)
  void updateTest() {
    DSLContext context = DSL.using(connection);

    EnumConverter<TestEnum> converter = TEST_ENTITY_ENUM.TEST_ENUM_ENUM_CONVERTER;
    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEntityEnum entity : DATA) {
      updates.add(
          context
              .update(TEST_ENTITY_ENUM)
              .set(TEST_ENTITY_ENUM.INTEGER_FIELD, entity.getIntegerField())
              .set(TEST_ENTITY_ENUM.ENUM_FIELD, converter.to(entity.getEnumField()))
              .where(TEST_ENTITY_ENUM.INTEGER_FIELD.eq(entity.getIntegerField()))
      );
    }
    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());
  }

  @Test
  @Order(4)
  void deleteTest() {
    DSLContext context = DSL.using(connection);

    DeleteConditionStep<Record> delete = context
        .delete(TEST_ENTITY_ENUM)
        .where(
            TEST_ENTITY_ENUM.INTEGER_FIELD.in(
                DATA
                    .stream()
                    .map(TestEntityEnum::getIntegerField)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);
  }
}