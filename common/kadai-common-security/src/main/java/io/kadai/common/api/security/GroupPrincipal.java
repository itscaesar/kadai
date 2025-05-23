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

package io.kadai.common.api.security;

import java.security.Principal;
import java.util.Objects;

/** Represents a group with a name. */
public class GroupPrincipal implements Principal {

  private final String name;

  public GroupPrincipal(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof GroupPrincipal)) {
      return false;
    }
    GroupPrincipal other = (GroupPrincipal) obj;
    return Objects.equals(name, other.name);
  }

  @Override
  public String toString() {
    return "GroupPrincipal [name=" + name + "]";
  }
}
