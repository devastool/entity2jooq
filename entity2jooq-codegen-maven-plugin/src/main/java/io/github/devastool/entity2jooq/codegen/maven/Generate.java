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
//  private String dialect; TODO. Add dialect configuration, by default autodetect
  private String className;
  private String packageName;
  private String target;

  private static final String DEFAULT_PACKAGE_NAME = "org.jooq.generated";
  private static final String DEFAULT_TARGET = "/generated-sources/jooq";

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getClassName() {
    return className;
  }

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
   *
   * @param project information of maven project
   */
  public void init(MavenProject project) {
    Build build = project.getBuild();
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
