import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {IUser} from "../models/i-user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl: string = 'http://localhost:8083';

  constructor(private http: HttpClient) { }

  getCurrentUser(): Observable<IUser> {
    return this.http.get<IUser>(`${this.apiUrl}/me`);
  }

  getUserUUID(): Observable<string> {
    return this.http.get<{ uuid: string }>(`${this.apiUrl}/user/uuid`).pipe(
      map(response => 'response.uuid')
    );
  }
}
