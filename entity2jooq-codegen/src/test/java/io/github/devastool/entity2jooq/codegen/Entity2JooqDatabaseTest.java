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

import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.CLASSES;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.CLASSPATH;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.DIALECT;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.TEST_CLASSES;

import java.util.Properties;
import org.jooq.SQLDialect;
import org.jooq.meta.DefaultRelations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link Entity2JooqDatabase}.
 *
 * @author Evgeniy_Gerasimov
 */
class Entity2JooqDatabaseTest {
  private final Entity2JooqDatabase db = new Entity2JooqDatabase();

  @Test
  void create0Test() {
    Assertions.assertDoesNotThrow(() -> db.create0());
  }

  @Test
  void loadMethodsTest() {
    var defaultRelations = new DefaultRelations();
    Assertions.assertDoesNotThrow(() -> db.loadForeignKeys(defaultRelations));
    Assertions.assertDoesNotThrow(() -> db.loadPrimaryKeys(defaultRelations));
    Assertions.assertDoesNotThrow(() -> db.loadUniqueKeys(defaultRelations));
    Assertions.assertDoesNotThrow(() -> db.loadCheckConstraints(defaultRelations));
  }

  @Test
  void getCatalogs0Test() {
    Assertions.assertDoesNotThrow(() -> db.getCatalogs0());
  }

  @Test
  void getSequences0Test() {
    Assertions.assertDoesNotThrow(() -> db.getSequences0());
  }

  @Test
  void getRoutines0Test() {
    Assertions.assertDoesNotThrow(() -> db.getRoutines0());
  }

  @Test
  void getPackages0Test() {
    Assertions.assertDoesNotThrow(() -> db.getPackages0());
  }

  @Test
  void getEnums0Test() {
    Assertions.assertDoesNotThrow(() -> db.getEnums0());
  }

  @Test
  void getDomains0Test() {
    Assertions.assertDoesNotThrow(() -> db.getDomains0());
  }

  @Test
  void getUDTs0Test() {
    Assertions.assertDoesNotThrow(() -> db.getUDTs0());
  }

  @Test
  void getArrays0() {
    Assertions.assertDoesNotThrow(() -> db.getArrays0());
  }

  @Test
  void initTest() {
    var properties = new Properties();
    properties.put(CLASSPATH.getName(), "testClassPath");
    properties.put(CLASSES.getName(), "classes");
    properties.put(TEST_CLASSES.getName(), "testClasses");
    properties.put(DIALECT.getName(), SQLDialect.POSTGRES.getName());
    db.setProperties(properties);

    Assertions.assertDoesNotThrow(() -> db.init());
  }

  @Test
  void getTables0Test() {
    var properties = new Properties();
    properties.put(CLASSPATH.getName(), "testClassPath");
    properties.put(CLASSES.getName(), "classes");
    properties.put(TEST_CLASSES.getName(), "testClasses");
    properties.put(DIALECT.getName(), SQLDialect.POSTGRES.getName());
    db.setProperties(properties);

    Assertions.assertDoesNotThrow(() -> db.getTables0());
    Assertions.assertDoesNotThrow(() -> db.getSchemata0());
  }
}