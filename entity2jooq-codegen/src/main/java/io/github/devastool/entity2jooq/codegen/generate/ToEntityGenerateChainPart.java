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
import io.github.devastool.entity2jooq.codegen.generate.code.MethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.EndLineCodeOperator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.InvokeMethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.NewCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.OperatorCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.ReturnCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarDefCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarMemberCodeGenerator;
import java.util.Objects;
import java.util.TreeSet;
import org.jooq.Converter;
import org.jooq.Record;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;

/**
 * Implementation of {@link GenerateChainPart} for 'toEntity' method generating. Method 'toEntity'
 * maps Jooq record to entity instance.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class ToEntityGenerateChainPart implements GenerateChainPart {
  private static final String METHOD_NAME = "toEntity";
  private static final String VARIABLE_NAME = "entity";
  private static final String PARAM_NAME = "record";
  private static final String PARAM_METHOD_NAME = "get";

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
    return new EndLineCodeOperator(
        new VarMemberCodeGenerator(
            VARIABLE_NAME,
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
}
