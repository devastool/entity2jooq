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
 * @since 0.0.1
 * @author Andrey_Yurzanov
 */
public class Entity2JooqJavaGenerator extends JavaGenerator {
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
    if (Objects.equals(EntityDataTypeDefinition.class, type.getClass())) {
      return ((EntityDataTypeDefinition) type).getJavaTypeReference();
    }
    return super.getJavaTypeReference(db, type);
  }

  @Override
  protected void generateTableClassFooter(TableDefinition table, JavaWriter out) {
    super.generateTableClassFooter(table, out);

    // TODO. Mapping functionality
    out.println(
        "public %s map(Record record) { throw new UnsupportedOperationException(); }",
        Object.class
    );
  }
}
