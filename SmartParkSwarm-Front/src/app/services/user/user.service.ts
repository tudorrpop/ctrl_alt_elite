import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { CustomerModel } from '../../data/model/customer.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = environment.apiUrl;

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

  public fetchCustomer(id: number): Observable<CustomerModel> {
    console.log(id);
    const headers = this.getAuthHeaders();

    return this.httpClient.get<CustomerModel>(
      `${this.baseUrl}/customer/user/${id}`,
      { headers, withCredentials: true }
    );
  }
}
