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

package io.kadai.routing.dmn.rest;

public class RoutingRestEndpoints {

  public static final String API_V1 = "/api/v1/";

  public static final String URL_ROUTING_RULES = API_V1 + "routing-rules";
  public static final String URL_ROUTING_RULES_DEFAULT = URL_ROUTING_RULES + "/default";
  public static final String ROUTING_REST_ENABLED = URL_ROUTING_RULES + "/routing-rest-enabled";

  private RoutingRestEndpoints() {}
}
