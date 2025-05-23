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

import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Settings } from '../models/settings';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SettingsRepresentation } from '../models/settings-representation';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private httpClient = inject(HttpClient);

  // GET
  getSettings(): Observable<Settings> {
    return this.httpClient
      .get<SettingsRepresentation>(`${environment.kadaiRestUrl}/v1/config/custom-attributes`)
      .pipe(map((b) => b.customAttributes));
  }

  // PUT
  updateSettings(settings: Settings) {
    return this.httpClient.put<Settings>(`${environment.kadaiRestUrl}/v1/config/custom-attributes`, {
      customAttributes: settings
    });
  }
}
