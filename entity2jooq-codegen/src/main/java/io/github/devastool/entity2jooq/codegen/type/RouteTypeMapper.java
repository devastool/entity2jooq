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

package io.github.devastool.entity2jooq.codegen.type;

import io.github.devastool.entity2jooq.annotation.type.NoSuchTypeException;
import io.github.devastool.entity2jooq.annotation.type.TypeMapper;
import io.github.devastool.entity2jooq.codegen.type.dialect.DefaultTypeMapper;
import io.github.devastool.entity2jooq.codegen.type.dialect.H2TypeMapper;
import io.github.devastool.entity2jooq.codegen.type.dialect.HSQLDbTypeMapper;
import io.github.devastool.entity2jooq.codegen.type.dialect.MariaDBTypeMapper;
import io.github.devastool.entity2jooq.codegen.type.dialect.MySQLTypeMapper;
import io.github.devastool.entity2jooq.codegen.type.dialect.PostgresTypeMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link TypeMapper} for routing between dialects.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class RouteTypeMapper implements TypeMapper {
  private final Map<String, TypeMapper> dialects = new HashMap<>();

  /**
   * Constructs new instance of {@link RouteTypeMapper}.
   */
  public RouteTypeMapper() {
    DefaultTypeMapper defaultMapper = new DefaultTypeMapper();
    dialects.put(defaultMapper.getDialect(), defaultMapper);

    PostgresTypeMapper postgresMapper = new PostgresTypeMapper();
    dialects.put(postgresMapper.getDialect(), postgresMapper);

    H2TypeMapper h2Mapper = new H2TypeMapper();
    dialects.put(h2Mapper.getDialect(), h2Mapper);
    //    TODO. CUBRID dialect
    //    CubridTypeMapper cubridMapper = new CubridTypeMapper();
    //    dialects.put(cubridMapper.getDialect(), cubridMapper);

    //    TODO. Derby dialect
    //    DerbyTypeMapper derbyMapper = new DerbyTypeMapper();
    //    dialects.put(derbyMapper.getDialect(), derbyMapper);

    //    TODO. Firebird dialect
    //    FirebirdTypeMapper firebirdMapper = new FirebirdTypeMapper();
    //    dialects.put(firebirdMapper.getDialect(), firebirdMapper);

    HSQLDbTypeMapper hsqldbMapper = new HSQLDbTypeMapper();
    dialects.put(hsqldbMapper.getDialect(), hsqldbMapper);

    MariaDBTypeMapper mariaDBMapper = new MariaDBTypeMapper();
    dialects.put(mariaDBMapper.getDialect(), mariaDBMapper);

    MySQLTypeMapper mySQLMapper = new MySQLTypeMapper();
    dialects.put(mySQLMapper.getDialect(), mySQLMapper);

    //    TODO. SQLite dialect
    //    SQLiteTypeMapper sqliteMapper = new SQLiteTypeMapper();
    //    dialects.put(sqliteMapper.getDialect(), sqliteMapper);
  }

  @Override
  public String getSqlType(String dialect, Class<?> type) throws NoSuchTypeException {
    TypeMapper typeMapper = dialects.get(dialect);
    if (typeMapper == null) {
      throw new NoSuchTypeException(dialect, type.getCanonicalName());
    }
    return typeMapper.getSqlType(dialect, type);
  }
}
