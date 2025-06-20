import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {StorageService} from "./storage.service";
import {Observable} from "rxjs";
import {IUser} from "./user.service";
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SmartChatService {
  private apiUrl: string = environment.apiUrl;

  constructor(private httpClient: HttpClient, private storageService: StorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token: string | null = this.storageService.get('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public getChatContext(): Observable<any> {
    const headers: HttpHeaders = this.getAuthHeaders();

    return this.httpClient.get<any>(`${this.apiUrl}/worker/chatprompt`,
      { headers, withCredentials: true }
    );
  }
}
