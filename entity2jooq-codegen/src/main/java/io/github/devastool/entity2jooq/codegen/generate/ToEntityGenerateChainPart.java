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
import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.definition.factory.column.FieldDetails;
import io.github.devastool.entity2jooq.codegen.generate.code.MethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.EndLineCodeOperator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.InvokeMethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.NewCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.OperatorCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.ReturnCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarDefCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarMemberCodeGenerator;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.jooq.Converter;
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

  @Override
  public void generate(GenerateContext context) {
    EntityTableDefinition table = context.getTable();
    if (table.isMapping()) {
      Class<?> type = table.getEntityType();

      MethodCodeGenerator generator = new MethodCodeGenerator()
          .setName(METHOD_NAME)
          .setReturnType(type)
          .setParam(PARAM_NAME, Record.class)
          .setOperator(
              new EndLineCodeOperator(
                  new VarDefCodeGenerator(VARIABLE_NAME, type, new NewCodeGenerator(type))
              )
          );

      var embeddedColumns = table
          .getColumns()
          .stream()
          .map(column -> (EntityColumnDefinition) column)
          .filter(EntityColumnDefinition::isEmbedded)
          .collect(Collectors.toList());

      var embeddedFieldMap = getFieldMap(embeddedColumns);

      // Create embedded instances
      for (Entry<String, Field> fieldEntry : embeddedFieldMap.entrySet()) {
        String fieldName = fieldEntry.getKey();
        Field field = fieldEntry.getValue();
        Class<?> fieldType = field.getDeclaringClass();
        generator.setOperator(
            new EndLineCodeOperator(
                new VarDefCodeGenerator(fieldName, fieldType, new NewCodeGenerator(fieldType))
            )
        );
      }

      // Embedded instances initialization
      for (Entry<String, Field> field : embeddedFieldMap.entrySet()) {
        String fieldName = field.getKey();
        var foundColumn = embeddedColumns
            .stream()
            .filter(column -> isFieldMatchingByName(fieldName, column))
            .findFirst()
            .orElse(null);

        if (foundColumn != null) {
          FieldDetails fieldDetails = foundColumn.getFieldDetails();
          generator.setOperator(
              new EndLineCodeOperator(
                  new VarMemberCodeGenerator(
                      fieldDetails.getParentEntityName(),
                      new InvokeMethodCodeGenerator(
                          getGetterName(foundColumn.getName()),
                          new VarCodeGenerator(fieldDetails.getEntityName())
                      )
                  )
              )
          );
        } else {
          generator.setOperator(
              new EndLineCodeOperator(
                  new VarMemberCodeGenerator(
                      VARIABLE_NAME,
                      new InvokeMethodCodeGenerator(
                          getGetterName(field.getKey()),
                          new VarCodeGenerator(field.getKey())
                      )
                  )
              )
          );
        }
      }

      for (ColumnDefinition column : new TreeSet<>(table.getColumns())) {
        if (Objects.equals(EntityColumnDefinition.class, column.getClass())) {
          EntityColumnDefinition entityColumn = (EntityColumnDefinition) column;

          OperatorCodeGenerator valueGetter = getRecordValueGetter(context, table, entityColumn);
          generator.setOperator(getValueSetter(entityColumn, valueGetter));
        }
      }

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
  private OperatorCodeGenerator getValueSetter(
      EntityColumnDefinition column,
      OperatorCodeGenerator recordValueGetter
  ) {
    var fieldDetails = column.getFieldDetails();
    String variableName;

    if (fieldDetails.isEmbedded()) {
      variableName = fieldDetails.getEntityName();
    } else {
      variableName = VARIABLE_NAME;
    }

    return new EndLineCodeOperator(
        new VarMemberCodeGenerator(
            variableName,
            new InvokeMethodCodeGenerator(column.getSetterName(), recordValueGetter)
        )
    );
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
    if (EntityDataTypeDefinition.class.equals(type.getClass())) {
      Converter converter = ((EntityDataTypeDefinition) type).getTypeConverter();
      if (converter != null) {
        String converterField = context.getVariable(converter.getClass(), String.class);
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

  // Creates a mapping where the key is the entity name and the value is the field entity.
  private HashMap<String, Field> getFieldMap(List<EntityColumnDefinition> embeddedColumns) {
    var fieldMap = new HashMap<String, Field>();

    for (EntityColumnDefinition column : embeddedColumns) {
      FieldDetails fieldDetails = column.getFieldDetails();
      String parentKey = fieldDetails.getParentEntityName();
      String fieldKey = fieldDetails.getEntityName();
      Field parentField = fieldDetails.getParentField();
      Field field = fieldDetails.getProcessedField();

      fieldMap.put(parentKey, parentField);
      fieldMap.put(fieldKey, field);
    }
    return fieldMap;
  }

  // Checks if the given field name matches the field name of the specified column.
  private boolean isFieldMatchingByName(String fieldName, EntityColumnDefinition column) {
    String columnFieldName = column
        .getFieldDetails()
        .getEntityName();
    return Objects.equals(fieldName, columnFieldName);
  }

  // Constructs the getter method name from the provided name.
  private String getGetterName(String name) {
    name = name
        .substring(FIRST_INDEX, SECOND_INDEX)
        .toUpperCase()
        .concat(name.substring(SECOND_INDEX));
    return SETTER_PREFIX.concat(name);
  }
}
