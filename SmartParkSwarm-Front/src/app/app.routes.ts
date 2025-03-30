import { Routes } from '@angular/router';
import { AuthenticationComponent } from './components/authentication/authentication.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

export const routes: Routes = [
    { path: '', redirectTo: '/authentication', pathMatch: 'full' },
    { path: 'authentication', component: AuthenticationComponent },
    { path: 'dashboard', component: DashboardComponent },
];
