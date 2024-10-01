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
import io.github.devastool.entity2jooq.codegen.definition.type.ConverterDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.MethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.EndLineCodeOperator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.InvokeMethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.OperatorCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.ReturnCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.TypeCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarDefCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VarMemberCodeGenerator;
import java.util.TreeSet;
import org.jooq.Record;
import org.jooq.meta.ColumnDefinition;

/**
 * Implementation of {@link GenerateChainPart} for 'toRecord' method generating. Method 'toRecord'
 * maps entity instance to Jooq record.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class ToRecordGenerateChainPart implements GenerateChainPart {
  private static final String METHOD_NAME = "toRecord";
  private static final String PARAM_NAME = "entity";
  private static final String VARIABLE_NAME = "record";
  private static final String NEW_VARIABLE_METHOD_NAME = "newRecord";
  private static final String VARIABLE_SET_VALUE_METHOD_NAME = "setValue";

  @Override
  public void generate(GenerateContext context) {
    EntityTableDefinition table = context.getTable();
    if (table.isMapping()) {
      VarDefCodeGenerator record = generateRecordVariable(table);
      MethodCodeGenerator method = new MethodCodeGenerator(METHOD_NAME)
          .setReturnType(Record.class)
          .setParam(PARAM_NAME, table.getEntityType())
          .setOperator(new EndLineCodeOperator(record));

      for (ColumnDefinition column : new TreeSet<>(table.getColumns())) {
        // TODO. Delete condition after: https://github.com/devastool/entity2jooq/issues/51
        if (!((EntityColumnDefinition) column).isEmbedded()) {
          method.setOperator(
              new EndLineCodeOperator(
                  generateSetValue(context, table, (EntityColumnDefinition) column)
              )
          );
        }
      }

      method
          .setOperator(new EndLineCodeOperator(new ReturnCodeGenerator(VARIABLE_NAME)))
          .generate(context.getTarget());
    }
  }

  // Generates code: Record record = TABLE_NAME.newRecord()
  private VarDefCodeGenerator generateRecordVariable(EntityTableDefinition table) {
    String tableName = table.getName();
    return new VarDefCodeGenerator(
        VARIABLE_NAME,
        new TypeCodeGenerator(Record.class),
        new VarMemberCodeGenerator(
            tableName.toUpperCase(),
            new InvokeMethodCodeGenerator(NEW_VARIABLE_METHOD_NAME)
        )
    );
  }

  // Generates code: record.setValue(COLUMN_NAME, entity.getColumnName()) or
  // record.setValue(COLUMN_NAME, entity.getColumnName(), CONVERTER)
  private OperatorCodeGenerator generateSetValue(
      GenerateContext context,
      EntityTableDefinition table,
      EntityColumnDefinition column
  ) {
    String tableName = table.getName();
    String columnName = column.getName();
    String getterName = column.getGetterName();

    EntityDataTypeDefinition type = (EntityDataTypeDefinition) column.getType();
    ConverterDefinition converter = type.getConverterDefinition();
    if (converter != null) {
      String field = context.getVariable(converter, String.class);
      return new VarMemberCodeGenerator(
          VARIABLE_NAME,
          new InvokeMethodCodeGenerator(
              VARIABLE_SET_VALUE_METHOD_NAME,
              new VarMemberCodeGenerator(
                  tableName.toUpperCase(),
                  target -> target.write(columnName.toUpperCase())
              ),
              new VarMemberCodeGenerator(
                  PARAM_NAME,
                  new InvokeMethodCodeGenerator(getterName)
              ),
              target -> target.write(field)
          )
      );
    }

    return new VarMemberCodeGenerator(
        VARIABLE_NAME,
        new InvokeMethodCodeGenerator(
            VARIABLE_SET_VALUE_METHOD_NAME,
            new VarMemberCodeGenerator(
                tableName.toUpperCase(),
                target -> target.write(columnName.toUpperCase())
            ),
            new VarMemberCodeGenerator(
                PARAM_NAME,
                new InvokeMethodCodeGenerator(getterName)
            )
        )
    );
  }
}
