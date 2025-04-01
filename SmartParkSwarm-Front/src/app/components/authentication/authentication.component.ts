import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { MessageService } from 'primeng/api';
import { AuthenticationRequest } from '../../data/request/authentication.request';
import { Router } from '@angular/router';

@Component({
  selector: 'app-authentication',
  standalone: true,
  imports: [
    FormsModule, 
    InputTextModule,
    PasswordModule,
    ButtonModule,
    CommonModule
  ],
  templateUrl: './authentication.component.html',
  styleUrl: './authentication.component.scss'
})
export class AuthenticationComponent{

  username!: string;
  password!: string;

  login: boolean = true;

  constructor(
    private authenticationService: AuthenticationService,
    private messageService: MessageService,
    private router: Router
  ) {}

  public changePerspective(): void {
    this.login = !this.login; 
  }

  public registerUser(): void {
    if(!this.username || !this.password) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid Registration!',
        detail: 'Please enter all details.',
      });
      return;
    }

    const authRequest = new AuthenticationRequest(this.username, this.password);

    this.authenticationService.createUser(authRequest).subscribe({
      next: (userOverviewModel) => {
        this.router.navigate(['/dashboard']);
        this.messageService.add({
          severity: 'success',
          summary: 'Successful Registration!',
          detail: `Glad to have you here, ${userOverviewModel.username}!`,
        });
      },

      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error!',
          detail: 'Something went wrong. Please try again.',
        });
      }
    });
  }

 public loginUser(): void {
  if (!this.username || !this.password) {
    this.messageService.add({
      severity: 'error',
      summary: 'Invalid Authentication!',
      detail: 'Please enter your credentials.',
    });
    return;
  }

  const authRequest = new AuthenticationRequest(this.username, this.password);

  this.authenticationService.loginUser(authRequest).subscribe({
    next: (userOverviewModel) => {
      this.router.navigate(['/dashboard']);
      this.messageService.add({
        severity: 'success',
        summary: 'Successful Login!',
        detail: `Glad to see you back, ${userOverviewModel.username}!`,
      });
    },

    error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Login Failed!',
          detail: 'Invalid username or password.',
        });
    }
  });
}

}
