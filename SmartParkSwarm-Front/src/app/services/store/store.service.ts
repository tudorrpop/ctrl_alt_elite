import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { StoreOverviewModel } from '../../data/model/store-overview.model';

@Injectable({
  providedIn: 'root'
})
export class StoreService {
  
  private baseUrl="http://localhost:8083";

  constructor(
    private httpClient: HttpClient,
    private cookieService: CookieService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public getStores(): Observable<StoreOverviewModel[]> {
    const headers = this.getAuthHeaders();    
    
    return this.httpClient.get<StoreOverviewModel[]>(
      `${this.baseUrl}/stores`, 
      { headers, withCredentials: true }
    );
  }

}
