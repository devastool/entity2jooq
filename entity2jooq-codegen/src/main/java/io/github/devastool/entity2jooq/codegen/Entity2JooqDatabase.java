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

import static io.github.devastool.entity2jooq.codegen.filesystem.ExtFileVisitor.CLASS_FILE_EXT;

import io.github.devastool.entity2jooq.codegen.definition.factory.EntityColumnDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityDataTypeDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntitySchemaDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityTableDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.FactoryContext;
import io.github.devastool.entity2jooq.codegen.filesystem.ExtFileVisitor;
import io.github.devastool.entity2jooq.codegen.filesystem.PathClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
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
  private final List<Class<?>> entities = new ArrayList<>();

  private final FactoryContext context = new FactoryContext();
  private final EntityDataTypeDefinitionFactory typeFactory = new EntityDataTypeDefinitionFactory();
  private final EntityColumnDefinitionFactory columnFactory =
      new EntityColumnDefinitionFactory(typeFactory, context);
  private final EntitySchemaDefinitionFactory schemaFactory =
      new EntitySchemaDefinitionFactory(context);
  private final EntityTableDefinitionFactory tableFactory =
      new EntityTableDefinitionFactory(schemaFactory, columnFactory, context);

  private static final String CLASSES_PROPERTY_KEY = "classes";
  private static final String TEST_CLASSES_PROPERTY_KEY = "testClasses";

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

      List<Path> paths = new ArrayList<>();
      String classes = properties.getProperty(CLASSES_PROPERTY_KEY);
      if (classes != null && !classes.isEmpty()) {
        paths.add(Paths.get(classes));
      }

      String testClasses = properties.getProperty(TEST_CLASSES_PROPERTY_KEY);
      if (testClasses != null && !testClasses.isEmpty()) {
        paths.add(Paths.get(testClasses));
      }

      try {
        PathClassLoader loader = new PathClassLoader();

        for (Path root : paths) {
          ExtFileVisitor visitor = new ExtFileVisitor(CLASS_FILE_EXT);
          Files.walkFileTree(root, visitor);

          for (Path classPath : visitor.getFiltered()) {
            Class<?> type = loader.loadClass(root, classPath);
            if (type != null) {
              entities.add(type);
            }
          }
        }
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      }
    }
  }
}
