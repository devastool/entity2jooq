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

import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.definition.factory.column.FieldDetails;
import io.github.devastool.entity2jooq.codegen.definition.type.ConverterDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.CodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.MethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.EndLineCodeOperator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.InvokeMethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.NewCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.OperatorCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.ReturnCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarDefCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarMemberCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.params.CodeGeneratorAccumulator;
import io.github.devastool.entity2jooq.codegen.generate.params.EntityGenerationParams;
import io.github.devastool.entity2jooq.codegen.generate.params.LinkPair;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.jooq.Record;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;

/**
 * Implementation of {@link GenerateChainPart} for 'toEntity' method generating. Method 'toEntity'
 * maps Jooq record to entity instance.
 *
 * @author Andrey_Yurzanov, Sergey_Konovalov
 * @since 1.0.0
 */
public class ToEntityGenerateChainPart implements GenerateChainPart {
  private static final String METHOD_NAME = "toEntity";
  private static final String VARIABLE_NAME = "entity";
  private static final String PARAM_NAME = "record";
  private static final String PARAM_METHOD_NAME = "get";
  private static final String SETTER_PREFIX = "set";
  private static final int FIRST_INDEX = 0;
  private static final int SECOND_INDEX = 1;
  private static final int COUNTER_DEFAULT = 0;
  private static final int COUNTER_INCREMENT = 1;
  private static final String SEPARATOR = "_";

  @Override
  public void generate(GenerateContext context) {
    EntityTableDefinition table = context.getTable();
    if (table.isMapping()) {
      Class<?> type = table.getEntityType();
      EntityGenerationParams params = new EntityGenerationParams();

      MethodCodeGenerator generator = new MethodCodeGenerator()
          .setName(METHOD_NAME)
          .setReturnType(type)
          .setParam(PARAM_NAME, Record.class);

      for (ColumnDefinition column : new TreeSet<>(table.getColumns())) {
        EntityColumnDefinition entityColumn = (EntityColumnDefinition) column;

        if (entityColumn.isEmbedded()) {
          FieldDetails fieldDetails = entityColumn.getFieldDetails();
          Field parentField = null;

          for (Field field : fieldDetails.getParentFields()) {
            getGeneratedEntity(field, params);

            OperatorCodeGenerator getter = getRecordValueGetter(context, table, entityColumn);
            getValueSetter(entityColumn, getter, params);

            getSetterLink(field, parentField, params);
            parentField = field;
          }
        } else {
          OperatorCodeGenerator valueGetter = getRecordValueGetter(context, table, entityColumn);
          getValueSetter(entityColumn, valueGetter, params);
        }
      }

      CodeGeneratorAccumulator accumulator = params.getCodeAccumulator();
      for (String entityName : accumulator.getReversedKeys()) {
        accumulator
            .findByKey(entityName)
            .forEach(generator::setOperator);
        generator
            .setOperator(CodeTarget::writeln);
      }

      generator.setOperator(
          new EndLineCodeOperator(
              new VarDefCodeGenerator(VARIABLE_NAME, type, new NewCodeGenerator(type))
          )
      );

      params
          .getRootCodeAccumulator()
          .forEach(generator::setOperator);

      generator
          .setOperator(
              new EndLineCodeOperator(
                  new ReturnCodeGenerator(VARIABLE_NAME)
              )
          )
          .generate(context.getTarget());
    }
  }

  // Generates code: tableName.setValue(recordValueGetter)
  private void getValueSetter(
      EntityColumnDefinition column,
      OperatorCodeGenerator recordValueGetter,
      EntityGenerationParams params
  ) {
    Map<Field, String> resolver = params.getNameResolver();
    FieldDetails fieldDetails = column.getFieldDetails();

    if (fieldDetails.isEmbedded()) {
      String variableName = resolver.get(fieldDetails.getLastParentField());
      if (Objects.nonNull(variableName)) {
        CodeGeneratorAccumulator accumulator = params.getCodeAccumulator();
        accumulator.accumulate(
            variableName,
            new EndLineCodeOperator(
                new VarMemberCodeGenerator(
                    variableName,
                    new InvokeMethodCodeGenerator(column.getSetterName(), recordValueGetter)
                )
            )
        );
      }
    } else {
      List<OperatorCodeGenerator> accumulator = params.getRootCodeAccumulator();
      accumulator.add(
          new EndLineCodeOperator(
              new VarMemberCodeGenerator(
                  VARIABLE_NAME,
                  new InvokeMethodCodeGenerator(column.getSetterName(), recordValueGetter)
              )
          )
      );
    }
  }

  // Generates code: record.get(TABLE_NAME.VALUE) or record.get(TABLE_NAME.VALUE, CONVERTER)
  private OperatorCodeGenerator getRecordValueGetter(
      GenerateContext context,
      EntityTableDefinition table,
      EntityColumnDefinition column
  ) {
    String tableName = table.getName();
    String columnName = column.getName();

    DataTypeDefinition type = column.getType();
    EntityDataTypeDefinition entityType = (EntityDataTypeDefinition) type;
    ConverterDefinition converterDefinition = entityType.getConverterDefinition();
    if (converterDefinition != null) {
      String converterField = context.getVariable(converterDefinition, String.class);
      return new VarMemberCodeGenerator(
          PARAM_NAME,
          new InvokeMethodCodeGenerator(
              PARAM_METHOD_NAME,
              new VarMemberCodeGenerator(
                  tableName.toUpperCase(),
                  target -> target.write(columnName.toUpperCase())
              ),
              target -> target.write(converterField)
          )
      );
    }

    return new VarMemberCodeGenerator(
        PARAM_NAME,
        new InvokeMethodCodeGenerator(
            PARAM_METHOD_NAME,
            new VarMemberCodeGenerator(
                tableName.toUpperCase(),
                target -> target.write(columnName.toUpperCase())
            )
        )
    );
  }

  // Generates code: Entity entity = new Entity();
  private void getGeneratedEntity(
      Field field,
      EntityGenerationParams params
  ) {
    Map<String, Integer> counter = params.getNameCounter();
    Map<Field, String> resolver = params.getNameResolver();

    String entityName = field.getName();
    String postfix = String.valueOf(
        counter.compute(entityName, (key, value) -> incrementOrDefault(value))
    );

    if (Objects.isNull(resolver.get(field))) {
      entityName = entityName.concat(SEPARATOR).concat(postfix);
      Class<?> entityType = field.getType();
      resolver.put(field, entityName);

      CodeGeneratorAccumulator accumulator = params.getCodeAccumulator();
      accumulator.accumulate(
          entityName,
          new EndLineCodeOperator(
              new VarDefCodeGenerator(entityName, entityType, new NewCodeGenerator(entityType)
              )
          )
      );
    }
  }

  // Generates code: entity.setValue(value);
  private void getSetterLink(
      Field field,
      Field parentField,
      EntityGenerationParams params
  ) {
    LinkPair link;
    Set<LinkPair> entityLinks = params.getEntityLinks();
    Map<Field, String> nameResolver = params.getNameResolver();
    String entityName = nameResolver.get(field);

    if (Objects.nonNull(parentField)) {
      String parentEntityName = nameResolver.get(parentField);
      link = new LinkPair(parentEntityName, entityName);

      if (!entityLinks.contains(link)) {
        CodeGeneratorAccumulator accumulator = params.getCodeAccumulator();
        accumulator.accumulate(
            parentEntityName,
            new EndLineCodeOperator(
                new VarMemberCodeGenerator(
                    parentEntityName,
                    new InvokeMethodCodeGenerator(
                        getSetterName(field.getName()),
                        target -> target.write(entityName)
                    )
                )
            )
        );
      }
    } else {
      link = new LinkPair(VARIABLE_NAME, entityName);
      if (!entityLinks.contains(link)) {
        List<OperatorCodeGenerator> rootAccumulator = params.getRootCodeAccumulator();
        rootAccumulator.add(
            new EndLineCodeOperator(
                new VarMemberCodeGenerator(
                    VARIABLE_NAME,
                    new InvokeMethodCodeGenerator(
                        getSetterName(field.getName()),
                        target -> target.write(entityName)
                    )
                )
            )
        );
      }
    }
    entityLinks.add(link);
  }

  // Constructs the setter method name from the provided name.
  private String getSetterName(String name) {
    name = name
        .substring(FIRST_INDEX, SECOND_INDEX)
        .toUpperCase()
        .concat(name.substring(SECOND_INDEX));
    return SETTER_PREFIX.concat(name);
  }

  // The increment method increases the value of counter or returning default if counter is null.
  private int incrementOrDefault(Integer counter) {
    if (counter == null) {
      return COUNTER_DEFAULT;
    }
    return counter + COUNTER_INCREMENT;
  }
}
