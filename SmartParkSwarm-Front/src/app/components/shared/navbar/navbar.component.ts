import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../../services/authentication/authentication.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  
  username!: string;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private messageService: MessageService
  ){ 
    this.username = authenticationService.getUsername();
  }

  showProfile(): void{

  }

  goDashboard(){
    this.router.navigate(['dashboard']);
  }

  logout(){
    this.authenticationService.logout();
    this.router.navigate(['authentication']);
    this.messageService.add({
      severity: 'success',
      summary: 'Successful Logout!',
      detail: `See you soon, ${this.username}!`,
    });
  }

}
