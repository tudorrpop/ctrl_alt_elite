import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {IAuthRequest} from "../models/i-auth-request.model";
import {IUser} from "../models/i-user.model";
import {StorageService} from "./storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8083';

  constructor(private http: HttpClient, private storageService: StorageService) {}

  isLoggedIn(): boolean {
    return !!this.storageService.get<string>('token');
  }

  login(data: IAuthRequest): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/login`, data).pipe(
      tap(async (response) => {
        await this.storageService.set('token', response.token);
      })
    );
  }

  register(data: IAuthRequest): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/register`, data);
  }
}
