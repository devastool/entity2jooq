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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

/**
 * Java code compiler configuration.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class Compile {
  private String target;
  private List<String> classpath;

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public void setClasspath(List<String> classpath) {
    this.classpath = classpath;
  }

  public List<String> getClasspath() {
    return classpath;
  }

  /**
   * Returns compiler output directories.
   *
   * @return compiler output directories
   */
  public List<File> getTargetLocation() {
    return Collections.singletonList(new File(target));
  }

  /**
   * Returns list of classpath files.
   *
   * @return list of classpath files
   */
  public List<File> getClasspathFiles() {
    return classpath
        .stream()
        .map(File::new)
        .collect(Collectors.toList());
  }

  /**
   * Configuration initialization.
   *
   * @param project information of maven project
   */
  public void init(MavenProject project) throws DependencyResolutionRequiredException {
    if (target == null || target.isEmpty()) {
      target = project.getBuild().getOutputDirectory();
    }

    if (classpath == null || classpath.isEmpty()) {
      classpath = project.getCompileClasspathElements();
    } else {
      HashSet<String> newClasspath = new HashSet<>(classpath);
      newClasspath.addAll(project.getCompileClasspathElements());
      classpath = new ArrayList<>(newClasspath);
    }
  }
}
