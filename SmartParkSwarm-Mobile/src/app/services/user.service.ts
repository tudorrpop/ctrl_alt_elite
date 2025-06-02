import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {StorageService} from "./storage.service";
import { environment } from 'src/environments/environment';

export interface IUser {
  userId: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  membership: string;
  active: boolean;
  uuid: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl: string = environment.apiUrl;

  constructor(private httpClient: HttpClient, private storageService: StorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token: string | null = this.storageService.get('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public getCurrentUser(userId: number): Observable<IUser> {
    const headers: HttpHeaders = this.getAuthHeaders();

    return this.httpClient.get<IUser>(`${this.apiUrl}/customer/user/${userId}`,
      { headers, withCredentials: true }
    );
  }

  public updateUser(userId: number, user: IUser): Observable<IUser> {
    const headers: HttpHeaders = this.getAuthHeaders();

    return this.httpClient.put<IUser>(`${this.apiUrl}/customer/user/${userId}`, user,
      { headers, withCredentials: true }
    );
  }
}
