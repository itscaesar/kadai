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

import { Observable, Subject, throwError as observableThrowError } from 'rxjs';
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { Workbasket } from 'app/shared/models/workbasket';
import { WorkbasketAccessItems } from 'app/shared/models/workbasket-access-items';
import { WorkbasketSummaryRepresentation } from 'app/shared/models/workbasket-summary-representation';
import { WorkbasketAccessItemsRepresentation } from 'app/shared/models/workbasket-access-items-representation';
import { WorkbasketDistributionTargets } from 'app/shared/models/workbasket-distribution-targets';
import { Sorting, WorkbasketQuerySortParameter } from 'app/shared/models/sorting';

import { DomainService } from 'app/shared/services/domain/domain.service';
import { catchError, mergeMap, tap } from 'rxjs/operators';
import { WorkbasketRepresentation } from '../../models/workbasket-representation';
import { WorkbasketQueryFilterParameter } from '../../models/workbasket-query-filter-parameter';
import { QueryPagingParameter } from '../../models/query-paging-parameter';
import { asUrlQueryString } from '../../util/query-parameters-v2';

@Injectable({
  providedIn: 'root'
})
export class WorkbasketService {
  public workBasketSelected = new Subject<string>();
  public workBasketSaved = new Subject<number>();
  public workbasketActionToolbarExpanded = new Subject<boolean>();
  private httpClient = inject(HttpClient);
  private domainService = inject(DomainService);
  private workbasketSummaryRef: Observable<WorkbasketSummaryRepresentation> = new Observable();

  // #region "REST calls"
  // GET
  getWorkBasketsSummary(
    forceRequest: boolean = false,
    filterParameter?: WorkbasketQueryFilterParameter,
    sortParameter?: Sorting<WorkbasketQuerySortParameter>,
    pagingParameter?: QueryPagingParameter
  ): Observable<WorkbasketSummaryRepresentation> {
    if (this.workbasketSummaryRef && !forceRequest) {
      return this.workbasketSummaryRef;
    }

    return this.domainService.getSelectedDomain().pipe(
      mergeMap((domain) => {
        this.workbasketSummaryRef = this.httpClient.get<WorkbasketSummaryRepresentation>(
          `${environment.kadaiRestUrl}/v1/workbaskets${asUrlQueryString({
            ...filterParameter,
            ...sortParameter,
            ...pagingParameter
          })}`
        );
        return this.workbasketSummaryRef;
      }),
      tap(() => {
        this.domainService.domainChangedComplete();
      })
    );
  }

  // GET
  getWorkBasket(id: string): Observable<Workbasket> {
    return this.httpClient.get<Workbasket>(`${environment.kadaiRestUrl}/v1/workbaskets/${id}`);
  }

  // GET
  getAllWorkBaskets(): Observable<WorkbasketRepresentation> {
    return this.httpClient.get<WorkbasketRepresentation>(
      `${environment.kadaiRestUrl}/v1/workbaskets?required-permission=OPEN`
    );
  }

  // POST
  createWorkbasket(workbasket: Workbasket): Observable<Workbasket> {
    return this.httpClient.post<Workbasket>(`${environment.kadaiRestUrl}/v1/workbaskets`, workbasket);
  }

  // PUT
  updateWorkbasket(url: string, workbasket: Workbasket): Observable<Workbasket> {
    return this.httpClient.put<Workbasket>(url, workbasket).pipe(catchError(this.handleError));
  }

  // delete
  markWorkbasketForDeletion(url: string): Observable<any> {
    return this.httpClient.delete<any>(url, { observe: 'response' });
  }

  // GET
  getWorkBasketAccessItems(url: string): Observable<WorkbasketAccessItemsRepresentation> {
    return this.httpClient.get<WorkbasketAccessItemsRepresentation>(url);
  }

  // POST
  createWorkBasketAccessItem(
    url: string,
    workbasketAccessItem: WorkbasketAccessItems
  ): Observable<WorkbasketAccessItems> {
    return this.httpClient.post<WorkbasketAccessItems>(url, workbasketAccessItem);
  }

  // PUT
  updateWorkBasketAccessItem(
    url: string,
    workbasketAccessItem: WorkbasketAccessItemsRepresentation
  ): Observable<string> {
    return this.httpClient.put<string>(url, workbasketAccessItem);
  }

  // GET
  getWorkBasketsDistributionTargets(
    url: string,
    filterParameter?: WorkbasketQueryFilterParameter,
    sortParameter?: Sorting<WorkbasketQuerySortParameter>,
    pagingParameter?: QueryPagingParameter
  ): Observable<WorkbasketDistributionTargets> {
    return this.httpClient.get<WorkbasketDistributionTargets>(
      `${url}${asUrlQueryString({
        ...filterParameter,
        ...sortParameter,
        ...pagingParameter
      })}`
    );
  }

  // PUT
  updateWorkBasketsDistributionTargets(
    url: string,
    distributionTargetsIds: Set<string>
  ): Observable<WorkbasketDistributionTargets> {
    return this.httpClient.put<WorkbasketDistributionTargets>(url, Array.from(distributionTargetsIds));
  }

  // DELETE
  removeDistributionTarget(url: string) {
    return this.httpClient.delete<string>(url);
  }

  // #endregion
  // #region "Service extras"
  selectWorkBasket(id?: string) {
    this.workBasketSelected.next(id);
  }

  getSelectedWorkBasket(): Observable<string> {
    return this.workBasketSelected.asObservable();
  }

  expandWorkbasketActionToolbar(value: boolean) {
    this.workbasketActionToolbarExpanded.next(value);
  }

  getWorkbasketActionToolbarExpansion(): Observable<boolean> {
    return this.workbasketActionToolbarExpanded.asObservable();
  }

  triggerWorkBasketSaved() {
    this.workBasketSaved.next(Date.now());
  }

  workbasketSavedTriggered(): Observable<number> {
    return this.workBasketSaved.asObservable();
  }

  // #endregion

  // #region private

  private handleError(error: Response | any) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    return observableThrowError(errMsg);
  }

  // #endregion
}
