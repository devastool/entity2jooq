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

import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.generate.ConverterGenerateChainPart;
import io.github.devastool.entity2jooq.codegen.generate.GenerateChainPart;
import io.github.devastool.entity2jooq.codegen.generate.GenerateContext;
import io.github.devastool.entity2jooq.codegen.generate.ToEntityGenerateChainPart;
import io.github.devastool.entity2jooq.codegen.generate.code.BufferedCodeTarget;
import io.github.devastool.entity2jooq.codegen.generate.code.IndentCodeTarget;
import java.util.List;
import java.util.Objects;
import org.jooq.codegen.GeneratorStrategy.Mode;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.Database;
import org.jooq.meta.TableDefinition;

/**
 * Generator of Java source code by annotations.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class Entity2JooqJavaGenerator extends JavaGenerator {
  private final List<GenerateChainPart> methods = List.of(
      new ConverterGenerateChainPart(),
      new ToEntityGenerateChainPart()
  );

  @Override
  public boolean generateRecords() {
    return false;
  }

  @Override
  protected String getJavaType(DataTypeDefinition type) {
    return type.getJavaType();
  }

  @Override
  protected String getJavaType(DataTypeDefinition type, Mode udtMode) {
    return type.getJavaType();
  }

  @Override
  protected String getJavaTypeReference(Database db, DataTypeDefinition type) {
    Class<? extends DataTypeDefinition> typeClass = type.getClass();
    if (Objects.equals(EntityDataTypeDefinition.class, typeClass)) {
      return ((EntityDataTypeDefinition) type).getJavaTypeReference();
    }
    throw new IllegalArgumentException("Unsupported type class: [" + typeClass + "]");
  }

  @Override
  protected void generateTableClassFooter(TableDefinition table, JavaWriter out) {
    super.generateTableClassFooter(table, out);

    if (EntityTableDefinition.class.equals(table.getClass())) {
      EntityTableDefinition entity = (EntityTableDefinition) table;

      BufferedCodeTarget target = new BufferedCodeTarget();
      GenerateContext context = new GenerateContext(entity, new IndentCodeTarget(target));
      for (GenerateChainPart method : methods) {
        method.generate(context);
      }
      out.print(target.getBuffer());
    }
  }
}
