package io.github.devastool.entity2jooq.annotation.type;

import java.util.NoSuchElementException;

/**
 * An exception throws when the type is not found.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class NoSuchTypeException extends NoSuchElementException {
  /**
   * Constructs new instance of exception.
   *
   * @param dialect SQL dialect
   * @param type    not found type
   */
  public NoSuchTypeException(String dialect, String type) {
    super(String.join("", "Type: [", type, "] is not found for SQL dialect: [", dialect, "]"));
  }
}
