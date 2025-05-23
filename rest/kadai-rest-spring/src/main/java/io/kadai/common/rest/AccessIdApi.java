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

package io.kadai.common.rest;

import io.kadai.common.api.exceptions.InvalidArgumentException;
import io.kadai.common.api.exceptions.NotAuthorizedException;
import io.kadai.common.rest.models.AccessIdRepresentationModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.naming.InvalidNameException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface AccessIdApi {

  /**
   * This endpoint searches a provided access Id in the configured ldap.
   *
   * @param searchFor the Access Id which should be searched for.
   * @return a list of all found Access Ids
   * @throws InvalidArgumentException if the provided search for Access Id is shorter than the
   *     configured one.
   * @throws NotAuthorizedException if the current user is not ADMIN or BUSINESS_ADMIN.
   * @throws InvalidNameException if name is not a valid dn.
   * @title Search for Access Id (users and groups and permissions)
   */
  @Operation(
      summary = "Search for Access Id (users and groups)",
      description = "This endpoint searches a provided access Id in the configured ldap.",
      parameters = {
        @Parameter(
            name = "search-for",
            description = "the Access Id which should be searched for.",
            example = "max",
            required = true)
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "a list of all found Access Ids",
            content =
                @Content(
                    mediaType = MediaTypes.HAL_JSON_VALUE,
                    schema = @Schema(implementation = AccessIdRepresentationModel[].class))),
        @ApiResponse(
            responseCode = "400",
            description = "INVALID_ARGUMENT",
            content = {
              @Content(schema = @Schema(implementation = InvalidArgumentException.class))
            }),
        @ApiResponse(
            responseCode = "403",
            description = "NOT_AUTHORIZED",
            content = {@Content(schema = @Schema(implementation = NotAuthorizedException.class))})
      })
  @GetMapping(path = RestEndpoints.URL_ACCESS_ID)
  ResponseEntity<List<AccessIdRepresentationModel>> searchUsersAndGroupsAndPermissions(
      @RequestParam("search-for") String searchFor)
      throws InvalidArgumentException, NotAuthorizedException, InvalidNameException;

  /**
   * This endpoint searches AccessIds for a provided name or Access Id. It will only search and
   * return users and members of groups which are configured with the requested KADAI role. This
   * search will only work if the users in the configured LDAP have an attribute that shows their
   * group memberships, e.g. "memberOf"
   *
   * @param nameOrAccessId the name or Access Id which should be searched for.
   * @param role the role for which all users should be searched for
   * @return a list of all found Access Ids (users)
   * @throws InvalidArgumentException if the provided search for Access Id is shorter than the
   *     configured one.
   * @throws NotAuthorizedException if the current user is not member of role USER, BUSINESS_ADMIN
   *     or ADMIN
   * @title Search for Access Id (users) in KADAI user role
   */
  @Operation(
      summary = "Search for Access Id (users) in KADAI user role",
      description =
          "This endpoint searches AccessIds for a provided name or Access Id. It will only search "
              + "and return users and members of groups which are configured with the requested "
              + "KADAI role. This search will only work if the users in the configured LDAP have"
              + " an attribute that shows their group memberships, e.g. \"memberOf\"",
      parameters = {
        @Parameter(
            name = "search-for",
            description = "the name or Access Id which should be searched for.",
            example = "user-1",
            required = true),
        @Parameter(
            name = "role",
            description = "the role for which all users should be searched for",
            example = "user",
            required = true)
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "a list of all found Access Ids (users)",
            content =
                @Content(
                    mediaType = MediaTypes.HAL_JSON_VALUE,
                    schema = @Schema(implementation = AccessIdRepresentationModel[].class))),
        @ApiResponse(
            responseCode = "400",
            description = "INVALID_ARGUMENT",
            content = {
              @Content(schema = @Schema(implementation = InvalidArgumentException.class))
            }),
        @ApiResponse(
            responseCode = "403",
            description = "NOT_AUTHORIZED",
            content = {@Content(schema = @Schema(implementation = NotAuthorizedException.class))})
      })
  @GetMapping(path = RestEndpoints.URL_ACCESS_ID_WITH_NAME)
  ResponseEntity<List<AccessIdRepresentationModel>> searchUsersByNameOrAccessIdForRole(
      @RequestParam("search-for") String nameOrAccessId, @RequestParam("role") String role)
      throws InvalidArgumentException, NotAuthorizedException;

  /**
   * This endpoint retrieves all groups a given Access Id belongs to.
   *
   * @param accessId the Access Id whose groups should be determined.
   * @return a list of the group Access Ids the requested Access Id belongs to
   * @throws InvalidArgumentException if the requested Access Id does not exist or is not unique.
   * @throws NotAuthorizedException if the current user is not ADMIN or BUSINESS_ADMIN.
   * @throws InvalidNameException if name is not a valid dn.
   * @title Get groups for Access Id
   */
  @Operation(
      summary = "Get groups for Access Id",
      description = "This endpoint retrieves all groups a given Access Id belongs to.",
      parameters = {
        @Parameter(
            name = "access-id",
            description = "the Access Id whose groups should be determined.",
            example = "teamlead-1",
            required = true)
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "a list of the group Access Ids the requested Access Id belongs to",
            content =
                @Content(
                    mediaType = MediaTypes.HAL_JSON_VALUE,
                    schema = @Schema(implementation = AccessIdRepresentationModel[].class))),
        @ApiResponse(
            responseCode = "400",
            description = "INVALID_ARGUMENT",
            content = {
              @Content(schema = @Schema(implementation = InvalidArgumentException.class))
            }),
        @ApiResponse(
            responseCode = "403",
            description = "NOT_AUTHORIZED",
            content = {@Content(schema = @Schema(implementation = NotAuthorizedException.class))})
      })
  @GetMapping(path = RestEndpoints.URL_ACCESS_ID_GROUPS)
  ResponseEntity<List<AccessIdRepresentationModel>> getGroupsByAccessId(
      @RequestParam("access-id") String accessId)
      throws InvalidArgumentException, NotAuthorizedException, InvalidNameException;

  /**
   * This endpoint retrieves all permissions a given Access Id belongs to.
   *
   * @param accessId the Access Id whose permissions should be determined.
   * @return a list of the permission Access Ids the requested Access Id belongs to
   * @throws InvalidArgumentException if the requested Access Id does not exist or is not unique.
   * @throws NotAuthorizedException if the current user is not ADMIN or BUSINESS_ADMIN.
   * @throws InvalidNameException if name is not a valid dn.
   * @title Get permissions for Access Id
   */
  @Operation(
      summary = "Get permissions for Access Id",
      description = "This endpoint retrieves all permissions a given Access Id belongs to.",
      parameters = {
        @Parameter(
            name = "access-id",
            description = "the Access Id whose permissions should be determined.",
            example = "user-1-2",
            required = true)
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "a list of the permission Access Ids the requested Access Id belongs to",
            content =
                @Content(
                    mediaType = MediaTypes.HAL_JSON_VALUE,
                    schema = @Schema(implementation = AccessIdRepresentationModel[].class))),
        @ApiResponse(
            responseCode = "400",
            description = "INVALID_ARGUMENT",
            content = {
              @Content(schema = @Schema(implementation = InvalidArgumentException.class))
            }),
        @ApiResponse(
            responseCode = "403",
            description = "NOT_AUTHORIZED",
            content = {@Content(schema = @Schema(implementation = NotAuthorizedException.class))})
      })
  @GetMapping(path = RestEndpoints.URL_ACCESS_ID_PERMISSIONS)
  ResponseEntity<List<AccessIdRepresentationModel>> getPermissionsByAccessId(
      @RequestParam("access-id") String accessId)
      throws InvalidArgumentException, NotAuthorizedException, InvalidNameException;
}
