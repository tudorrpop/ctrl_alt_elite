import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { UserOverviewModel } from '../../data/model/user-overview.model';
import { AuthenticationRequest } from '../../data/request/authentication.request';
import { catchError, Observable, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private baseUrl = 'http://localhost:8083';

  constructor(
    private httpClient: HttpClient,
    private cookieService: CookieService,
  ) {}

  public createUser(authenticationRequest: AuthenticationRequest): Observable<UserOverviewModel> {
    return this.httpClient.post<UserOverviewModel>(
      `${this.baseUrl}/register`, authenticationRequest
    ).pipe(
      tap(response => {
        this.cookieService.set('username', response.username);
        this.cookieService.set('userId', response.userId.toString());
        this.cookieService.set('token', response.token);
        this.cookieService.set('role', response.role);
      }),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  public loginUser(authenticationRequest: AuthenticationRequest): Observable<UserOverviewModel> {
    return this.httpClient.post<UserOverviewModel>(
      `${this.baseUrl}/login`, authenticationRequest
    ).pipe(
      tap(response => {
        this.cookieService.set('username', response.username);
        this.cookieService.set('userId', response.userId.toString());
        this.cookieService.set('token', response.token);
        this.cookieService.set('role', response.role);
      }),
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error);
      })
    );
  }

  public logout(): void {
    
  }

}
