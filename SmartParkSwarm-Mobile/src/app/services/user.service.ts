import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {IUser} from "../models/i-user.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://192.168.1.128:8083';

  constructor(private http: HttpClient) { }

  getCurrentUser(): Observable<IUser> {
    return this.http.get<IUser>(`${this.apiUrl}/me`);
  }
}
