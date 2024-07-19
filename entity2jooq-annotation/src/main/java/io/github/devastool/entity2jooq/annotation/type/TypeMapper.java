package io.github.devastool.entity2jooq.annotation.type;

/**
 * Mapping between SQL and Java types.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public interface TypeMapper {
  /**
   * Returns Java type by dialect and SQL type.
   *
   * @param dialect SQL dialect
   * @param sqlType SQL type
   * @return Java type
   * @throws NoSuchTypeException when th type is not found
   */
  String getType(String dialect, String sqlType) throws NoSuchTypeException;

  /**
   * Returns SQL type by dialect and Java type.
   *
   * @param dialect SQL dialect
   * @param type    Java type
   * @return SQL type
   * @throws NoSuchTypeException when the type is not found
   */
  String getSqlType(String dialect, String type) throws NoSuchTypeException;
}
