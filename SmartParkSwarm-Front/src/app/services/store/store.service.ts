import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { StoreOverviewModel } from '../../data/model/store-overview.model';
import { StoreRequest } from '../../data/request/store.request';
import { StoreModel } from '../../data/model/store.model';
import { ParkingSpotStatus } from '../../data/model/parking-spot-status.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StoreService {

  private baseUrl = environment.apiUrl;

  constructor(
    private httpClient: HttpClient,
    private cookieService: CookieService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public fetchStores(): Observable<StoreOverviewModel[]> {
    const headers = this.getAuthHeaders();

    return this.httpClient.get<StoreOverviewModel[]>(
      `${this.baseUrl}/stores`,
      { headers, withCredentials: true }
    );
  }

  public createStore(storeRequest: StoreRequest): Observable<StoreOverviewModel> {
    const headers = this.getAuthHeaders();

    return this.httpClient.post<StoreOverviewModel>(
      `${this.baseUrl}/worker/initialize`,
      storeRequest,
      { headers, withCredentials: true }
    );
  }

  public fetchStore(storeId: number): Observable<StoreModel> {
    const headers = this.getAuthHeaders();

    return this.httpClient.get<StoreModel>(
      `${this.baseUrl}/stores/${storeId}`,
      { headers, withCredentials: true }
    );
  }

  public deleteStore(storeId: number): Observable<HttpResponse<any>> {
    const headers = this.getAuthHeaders();

    return this.httpClient.delete<HttpResponse<any>>(
      `${this.baseUrl}/stores/${storeId}`,
      { headers, withCredentials: true, observe: 'response' }
    );
  }

  public initialParkingLotStatus(id: number): Observable<ParkingSpotStatus[]> {
    const headers = this.getAuthHeaders();
    console.log(id);

    // const params = new HttpParams().set('id', id);

    return this.httpClient.put<ParkingSpotStatus[]>(
      `${this.baseUrl}/worker/update?id=${id}`,

      { headers, withCredentials: true }
    );
  }

  public updateStore(id: number, storeRequest: StoreRequest): Observable<StoreModel> {
    const headers = this.getAuthHeaders();

    return this.httpClient.put<StoreModel>(
      `${this.baseUrl}/stores/${id}`,
      storeRequest,
      { headers, withCredentials: true }
    );
  }

}
