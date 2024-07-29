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

import io.github.devastool.entity2jooq.codegen.definition.factory.EntityColumnDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityDataTypeDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntitySchemaDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityTableDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.filesystem.classload.PathClassLoader;
import io.github.devastool.entity2jooq.codegen.filesystem.classload.ClassLoaderContext;
import io.github.devastool.entity2jooq.codegen.filesystem.classload.ClassFile;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;

/**
 * Realization of {@link Database}, provides meta-information about database structure by
 * annotations.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class Entity2JooqDatabase extends AbstractDatabase {
  private final List<Class<?>> entities;
  private final EntitySchemaDefinitionFactory schemaFactory;
  private final EntityTableDefinitionFactory tableFactory;

  private static final String CLASSPATH_PROPERTY_KEY = "classpath";
  private static final String CLASSES_PROPERTY_KEY = "classes";
  private static final String TEST_CLASSES_PROPERTY_KEY = "testClasses";

  /**
   * Constructs new instance of {@link Entity2JooqDatabase}.
   */
  public Entity2JooqDatabase() {
    super();

    entities = new ArrayList<>();
    schemaFactory = new EntitySchemaDefinitionFactory();
    tableFactory = new EntityTableDefinitionFactory(
        schemaFactory,
        new EntityColumnDefinitionFactory(new EntityDataTypeDefinitionFactory())
    );
  }

  @Override
  protected DSLContext create0() {
    return DSL.using(SQLDialect.DEFAULT.family());
  }

  @Override
  protected void loadPrimaryKeys(DefaultRelations defaultRelations) throws SQLException {
  }

  @Override
  protected void loadUniqueKeys(DefaultRelations defaultRelations) throws SQLException {
  }

  @Override
  protected void loadForeignKeys(DefaultRelations defaultRelations) throws SQLException {
  }

  @Override
  protected void loadCheckConstraints(DefaultRelations defaultRelations) throws SQLException {
  }

  @Override
  protected List<CatalogDefinition> getCatalogs0() throws SQLException {
    List<CatalogDefinition> catalogs = new ArrayList<>();
    catalogs.add(new CatalogDefinition(this, "", ""));
    return catalogs;
  }

  @Override
  protected List<SchemaDefinition> getSchemata0() throws SQLException {
    init();

    // Build schemas by entities
    return entities
        .stream()
        .map(type -> schemaFactory.build(type, this))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  protected List<SequenceDefinition> getSequences0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<TableDefinition> getTables0() throws SQLException {
    init();

    return entities
        .stream()
        .map(type -> tableFactory.build(type, this))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  @Override
  protected List<RoutineDefinition> getRoutines0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<PackageDefinition> getPackages0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<EnumDefinition> getEnums0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<DomainDefinition> getDomains0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<UDTDefinition> getUDTs0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<ArrayDefinition> getArrays0() throws SQLException {
    return new ArrayList<>();
  }

  protected void init() {
    if (entities.isEmpty()) {
      Properties properties = getProperties();

      ClassLoaderContext context = new ClassLoaderContext();
      String classpath = properties.getProperty(CLASSPATH_PROPERTY_KEY);
      if (classpath != null && !classpath.isEmpty()) {
        context.addClasspath(classpath);
      }

      String classes = properties.getProperty(CLASSES_PROPERTY_KEY);
      if (classes != null && !classes.isEmpty()) {
        context.addRoot(Paths.get(classes));
      }

      String testClasses = properties.getProperty(TEST_CLASSES_PROPERTY_KEY);
      if (testClasses != null && !testClasses.isEmpty()) {
        context.addRoot(Paths.get(testClasses));
      }

      try (PathClassLoader loader = new PathClassLoader(context)) {
        for (ClassFile element : context) {
          entities.add(loader.loadClass(element));
        }
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      }
    }
  }
}
