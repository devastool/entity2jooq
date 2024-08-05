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

package io.github.devastool.entity2jooq.codegen.maven;

import io.github.devastool.entity2jooq.codegen.Entity2JooqJavaGenerator;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.jooq.meta.jaxb.Target;

/**
 * Java code generator configuration.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class Generate {
  private String dialect; // TODO. Add autodetect
  private String className;
  private String packageName;
  private String target;

  private static final String DEFAULT_DIALECT = "";
  private static final String DEFAULT_PACKAGE_NAME = "org.jooq.generated";
  private static final String DEFAULT_TARGET = "/generated-sources/jooq";

  /**
   * Returns SQL dialect.
   *
   * @return SQL dialect
   */
  public String getDialect() {
    return dialect;
  }

  /**
   * Sets SQL dialect.
   *
   * @param dialect SQL dialect
   */
  public void setDialect(String dialect) {
    this.dialect = dialect;
  }

  /**
   * Returns the package name for generated classes.
   *
   * @return package name for generated classes
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Sets the package name for generated classes.
   *
   * @param packageName package name for generated classes
   */
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  /**
   * Returns directory for generated classes.
   *
   * @return directory for generated classes
   */
  public String getTarget() {
    return target;
  }

  /**
   * Sets directory for generated classes.
   *
   * @param target directory for generated classes
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Returns class name of the jooq generator.
   *
   * @return class name of the jooq generator
   */
  public String getClassName() {
    return className;
  }

  /**
   * Sets class name of the jooq generator.
   *
   * @param className class name of the jooq generator
   */
  public void setClassName(String className) {
    this.className = className;
  }

  /**
   * Constructs instance of {@link Target} by configuration.
   *
   * @return instance of {@link Target}
   */
  public Target getGenerateTarget() {
    Target target = new Target();
    target.setDirectory(this.target);
    target.setPackageName(packageName);
    return target;
  }

  /**
   * Configuration initialization.
   * <br/>
   * Default values:
   * <br/>
   * dialect = ""
   * <br/>
   * packageName = "org.jooq.generated"
   * <br/>
   * target = "/generated-sources/jooq"
   * <br/>
   * className = "io.github.devastool.entity2jooq.codegen.Entity2JooqJavaGenerator"
   *
   * @param project information of maven project
   */
  public void init(MavenProject project) {
    Build build = project.getBuild();
    if (dialect == null || dialect.isEmpty()) {
      dialect = DEFAULT_DIALECT;
    }

    if (packageName == null || packageName.isEmpty()) {
      packageName = DEFAULT_PACKAGE_NAME;
    }

    if (target == null || target.isEmpty()) {
      target = build.getDirectory() + DEFAULT_TARGET;
    }

    if (className == null || className.isEmpty()) {
      className = Entity2JooqJavaGenerator.class.getCanonicalName();
    }
  }
}
