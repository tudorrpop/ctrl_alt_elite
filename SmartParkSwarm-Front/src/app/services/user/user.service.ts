import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { CustomerModel } from '../../data/model/customer.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl="http://localhost:8083";

  constructor(
    private httpClient: HttpClient,
    private cookieService: CookieService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public fetchUsers(): Observable<CustomerModel[]> {
      const headers = this.getAuthHeaders();

      return this.httpClient.get<CustomerModel[]>(
        `${this.baseUrl}/customer/users`,
        { headers, withCredentials: true }
      );
  }

  public getCurrentUser(userId: number): Observable<CustomerModel> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<CustomerModel>(
      `${this.baseUrl}/customer/user/${userId}`,
      { headers, withCredentials: true }
    );
  }
}
