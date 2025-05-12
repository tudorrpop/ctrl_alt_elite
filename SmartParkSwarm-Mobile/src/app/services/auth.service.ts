import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { catchError, Observable, tap, throwError } from "rxjs";
import { IAuthRequest } from "../models/i-auth-request.model";
import { IUser } from "../models/i-user.model";
import { StorageService } from "./storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://192.168.1.128:8083';

  constructor(private http: HttpClient, private storageService: StorageService) {}

  //Updated to return a Promise<boolean>
  // async isLoggedIn(): Promise<boolean> {
  //   const token = await this.storageService.get<string>('token');
  //   return !!token;
  // }
  isLoggedIn(): boolean {
    return !!this.storageService.get<string>('token');
  }

  login(data: IAuthRequest): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/login`, data).pipe(
      tap(async response => {
        await this.storageService.set('username', response.username);
        await this.storageService.set('userId', response.userId.toString());
        await this.storageService.set('token', response.token);
        await this.storageService.set('role', response.role);
      }),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  register(data: IAuthRequest): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/register`, data).pipe(
      tap(async response => {
        await this.storageService.set('username', response.username);
        await this.storageService.set('userId', response.userId.toString());
        await this.storageService.set('token', response.token);
        await this.storageService.set('role', response.role);
      }),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  logout(): void {
    this.storageService.clear();
  }
}
