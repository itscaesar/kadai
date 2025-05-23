/*
 * Copyright [2025] [envite consulting GmbH]
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
 *
 *
 */

package io.kadai.task.api;

import io.kadai.common.api.QueryColumnName;

/**
 * Enum containing the column names for {@link
 * io.kadai.task.internal.ObjectReferenceMapper#queryObjectReferenceColumnValues}.
 */
public enum ObjectReferenceQueryColumnName implements QueryColumnName {
  ID("id"),
  COMPANY("company"),
  SYSTEM("system"),
  SYSTEM_INSTANCE("system_instance"),
  TYPE("type"),
  VALUE("value");

  private final String name;

  ObjectReferenceQueryColumnName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
