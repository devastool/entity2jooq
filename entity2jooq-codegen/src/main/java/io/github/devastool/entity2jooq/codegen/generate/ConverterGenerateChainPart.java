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

import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.ConverterDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.CodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.FieldCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.NewCodeGenerator;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import org.jooq.meta.ColumnDefinition;

/**
 * Implementation of {@link GenerateChainPart} for converter fields generating.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class ConverterGenerateChainPart implements GenerateChainPart {
  private final NamingStrategy naming = new SnakeCaseStrategy(true);

  @Override
  public void generate(GenerateContext context) {
    EntityTableDefinition table = context.getTable();
    if (table.isMapping()) {
      TreeSet<ConverterDefinition> converters = new TreeSet<>();
      for (ColumnDefinition column : table.getColumns()) {
        Optional<EntityDataTypeDefinition> extracted = getType(column);
        if (extracted.isPresent()) {
          EntityDataTypeDefinition type = extracted.get();

          ConverterDefinition converter = type.getConverterDefinition();
          if (converter != null) {
            converters.add(converter);
          }
        }
      }

      CodeTarget target = context.getTarget();
      for (ConverterDefinition converter : converters) {
        String name = getConverterName(converter);
        context.setVariable(converter, name);

        Class<?> converterType = converter.getConverterType();
        NewCodeGenerator assignment = new NewCodeGenerator(converterType);
        FieldCodeGenerator field = new FieldCodeGenerator(name, converterType, assignment);

        Optional<Class<?>> toType = converter.getGenericToType();
        if (toType.isPresent()) {
          Class<?> type = toType.get();

          field.setGenericTypes(type);
          assignment.setGenericTypes(type);
        }

        field.generate(target);
      }
    }
  }

  // Returns type of the column
  private Optional<EntityDataTypeDefinition> getType(ColumnDefinition column) {
    return Optional
        .of(column)
        .filter(value -> Objects.equals(EntityColumnDefinition.class, value.getClass()))
        .map(EntityColumnDefinition.class::cast)
        .map(EntityColumnDefinition::getType)
        .filter(value -> Objects.equals(EntityDataTypeDefinition.class, value.getClass()))
        .map(EntityDataTypeDefinition.class::cast);
  }

  // Returns name of the converter
  private String getConverterName(ConverterDefinition converter) {
    ArrayList<String> names = new ArrayList<>();
    Optional<Class<?>> toType = converter.getGenericToType();
    if (toType.isPresent()) {
      Class<?> type = toType.get();
      names.add(type.getSimpleName());
    }
    Class<?> type = converter.getConverterType();
    names.add(type.getSimpleName());

    return naming.resolve(names.toArray(new String[]{}));
  }
}
