import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonContent,
  IonInput,
  IonItem,
  IonLabel
} from '@ionic/angular/standalone';
import {AuthService} from "../services/auth.service";
import {IAuthRequest} from "../models/i-auth-request.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.page.html',
  styleUrls: ['./auth.page.scss'],
  standalone: true,
  imports: [IonContent, CommonModule, FormsModule, IonButton, IonItem, IonLabel, IonInput]
})
export class AuthPage {
  isLogin: boolean = true;
  authData: IAuthRequest = {
    username: '',
    password: '',
    role: 'CUSTOMER'
  };

  constructor(private authService: AuthService, private router: Router) {}

  submit(): void {
    if (this.isLogin) {
      this.authService.login(this.authData).subscribe({
        next: (res) => {
          this.router.navigate(['/tabs/tab1']).then(() => {
            console.log('Navigation complete');
          });
        },
        error: (err) => {
          console.error('Login failed', err);
        }
      });
    } else {
      this.authService.register(this.authData).subscribe({
        next: (res) => {
          console.log('Registered:', res);
        },
        error: (err) => {
          console.error('Registration failed', err);
        }
      });
    }
  }
}
