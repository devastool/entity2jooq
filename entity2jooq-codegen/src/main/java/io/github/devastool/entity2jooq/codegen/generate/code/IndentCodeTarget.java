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

package io.github.devastool.entity2jooq.codegen.generate.code;

/**
 * Implementation of {@link CodeTarget} with append indention functionality.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class IndentCodeTarget implements CodeTarget {
  private int level;
  private boolean needIndent;
  private final int indentCount;
  private final CodeTarget target;

  /**
   * Default indents count.
   */
  public static final int DEFAULT_INDENT = 4;

  /**
   * Constructs new instance of {@link IndentCodeTarget}. Uses default indents count, see
   * DEFAULT_INDENT constant.
   *
   * @param target source code destination
   */
  public IndentCodeTarget(CodeTarget target) {
    this(target, DEFAULT_INDENT);
  }

  /**
   * Constructs new instance of {@link IndentCodeTarget}.
   *
   * @param target      source code destination
   * @param indentCount indents count
   */
  public IndentCodeTarget(CodeTarget target, int indentCount) {
    this.target = target;
    this.indentCount = indentCount;
    this.level = 1;
    this.needIndent = true;
  }

  @Override
  public CodeTarget rawWrite(String value) {
    target.rawWrite(value);
    return this;
  }

  @Override
  public CodeTarget write(String value) {
    if ("{".equalsIgnoreCase(value)) {
      level++;
    } else if ("}".equalsIgnoreCase(value)) {
      level--;
    }

    if (needIndent) {
      for (int i = 0; i < indentCount * level; i++) {
        space();
      }
      needIndent = false;
    }
    rawWrite(value);

    return this;
  }

  @Override
  public CodeTarget writeln(String value) {
    write(value);

    needIndent = true;
    target.writeln();
    return this;
  }
}
