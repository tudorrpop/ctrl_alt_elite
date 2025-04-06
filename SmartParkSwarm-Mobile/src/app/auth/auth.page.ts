import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonHeader, IonInput,
  IonItem,
  IonLabel,
  IonSelect, IonSelectOption,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import {AuthService} from "../services/auth.service";
import {IAuthRequest} from "../models/i-auth-request.model";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.page.html',
  styleUrls: ['./auth.page.scss'],
  standalone: true,
  imports: [IonContent, IonHeader, IonTitle, IonToolbar, CommonModule, FormsModule, IonButton, IonItem, IonLabel, IonSelect, IonSelectOption, IonInput]
})
export class AuthPage {
  isLogin = true;
  authData: IAuthRequest = {
    username: '',
    password: '',
    role: 'CUSTOMER'
  };

  constructor(private authService: AuthService) {}

  submit() {
    if (this.isLogin) {
      this.authService.login(this.authData).subscribe({
        next: (res) => {
          console.log('Logged in:', res);
          // Save token to storage
        },
        error: (err) => {
          console.error('Login failed', err);
        }
      });
    } else {
      this.authService.register(this.authData).subscribe({
        next: (res) => {
          console.log('Registered:', res);
          // Save token to storage
        },
        error: (err) => {
          console.error('Registration failed', err);
        }
      });
    }
  }
}
