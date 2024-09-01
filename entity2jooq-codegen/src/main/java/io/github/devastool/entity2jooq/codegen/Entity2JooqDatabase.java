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
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.DATABASE;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.DIALECT;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.TEST_CLASSES;

import io.github.devastool.entity2jooq.codegen.definition.factory.EntityColumnDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityDataTypeDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntitySchemaDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityTableDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.FactoryContext;
import io.github.devastool.entity2jooq.codegen.filesystem.classload.ClassFile;
import io.github.devastool.entity2jooq.codegen.filesystem.classload.ClassLoaderContext;
import io.github.devastool.entity2jooq.codegen.filesystem.classload.PathClassLoader;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.EnumConverter;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.Definition;
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
  private final EntityTableDefinitionFactory tableFactory;

  /**
   * Constructs new instance of {@link Entity2JooqDatabase}.
   */
  public Entity2JooqDatabase() {
    super();

    // TODO. Load from pom.xml?
    Map<Class<?>, Class<? extends Converter>> converters = Map.of(
        Enum.class,
        EnumConverter.class
    );

    FactoryContext context = new FactoryContext();
    EntityDataTypeDefinitionFactory type = new EntityDataTypeDefinitionFactory(context, converters);
    EntitySchemaDefinitionFactory schemas = new EntitySchemaDefinitionFactory(context);
    EntityColumnDefinitionFactory columns = new EntityColumnDefinitionFactory(type, context);

    entities = new ArrayList<>();
    tableFactory = new EntityTableDefinitionFactory(schemas, columns, context);
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
    // Build schemas by entities
    return getTables0()
        .stream()
        .map(Definition::getSchema)
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  protected List<SequenceDefinition> getSequences0() throws SQLException {
    return new ArrayList<>();
  }

  @Override
  protected List<TableDefinition> getTables0() throws SQLException {
    CodegenProperties properties = init();
    return entities
        .stream()
        .map(type -> tableFactory.build(type, properties))
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

  protected CodegenProperties init() {
    Properties pluginProperties = getProperties();

    ClassLoaderContext context = new ClassLoaderContext();
    String classpath = pluginProperties.getProperty(CLASSPATH.getName());
    context.addClasspath(classpath);

    String classes = pluginProperties.getProperty(CLASSES.getName());
    context.addRoot(Paths.get(classes));

    String testClasses = pluginProperties.getProperty(TEST_CLASSES.getName());
    context.addRoot(Paths.get(testClasses));

    if (entities.isEmpty()) {
      try (PathClassLoader loader = new PathClassLoader(context)) {
        for (ClassFile element : context) {
          Class<?> loaded = loader.loadClass(element);
          if (tableFactory.canBuild(loaded)) {
            entities.add(loaded);
          }
        }
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      }
    }

    String dialect = pluginProperties.getProperty(DIALECT.getName());
    return new CodegenProperties(Map.of(
        CLASSPATH, classpath,
        CLASSES, classes,
        TEST_CLASSES, testClasses,
        DATABASE, this,
        DIALECT, dialect
    ));
  }
}
