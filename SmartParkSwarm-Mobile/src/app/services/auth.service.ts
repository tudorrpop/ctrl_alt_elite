import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { IAuthRequest } from '../models/i-auth-request.model';
import { User } from '../models/i-user.model';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string = 'http://localhost:8083';

  constructor(private http: HttpClient, private storageService: StorageService) {}

  isLoggedIn(): boolean {
    const token = this.storageService.get('token');
    return !!token;
  }

  login(data: IAuthRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, data).pipe(
      tap(response => {
        this.storageService.set('username', response.username);
        this.storageService.set('userId', response.userId.toString());
        this.storageService.set('token', response.token);
        this.storageService.set('role', response.role);
        this.storageService.set('uuid', response.uuid);
      }),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  register(data: IAuthRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, data).pipe(
      tap(response => {
        this.storageService.set('username', response.username);
        this.storageService.set('userId', response.userId.toString());
        this.storageService.set('token', response.token);
        this.storageService.set('role', response.role);
        this.storageService.set('uuid', response.uuid);
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
