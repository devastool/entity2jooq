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

import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.CodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.MethodCodeGenerator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.ReturnCodeGeneratorOperator;
import io.github.devastool.entity2jooq.codegen.generate.code.operator.VariableDefCodeOperatorGenerator;
import java.util.Objects;
import org.jooq.Record;
import org.jooq.meta.TableDefinition;

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

  @Override
  public void generate(TableDefinition table, CodeTarget target) {
    if (Objects.equals(EntityTableDefinition.class, table.getClass())) {
      EntityTableDefinition entity = (EntityTableDefinition) table;
      Class<?> entityType = entity.getEntityType();
      new MethodCodeGenerator()
          .setName(METHOD_NAME)
          .setReturnType(entityType)
          .setParam(PARAM_NAME, Record.class)
          .setOperator(new VariableDefCodeOperatorGenerator(VARIABLE_NAME, entityType))
          .setOperator(new ReturnCodeGeneratorOperator(VARIABLE_NAME))
          .generate(target);
    }
  }
}
