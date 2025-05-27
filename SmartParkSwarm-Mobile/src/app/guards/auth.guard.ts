import { CanActivateFn, CanMatchFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn & CanMatchFn = (route, stateOrSegments) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  let currentUrl = '';

  if (Array.isArray(stateOrSegments)) {
    currentUrl = '/' + stateOrSegments.map(s => s.path).join('/');
  } else {
    currentUrl = stateOrSegments.url;
  }

  if (authService.isLoggedIn()) {
    return true;
  } else {
    if (currentUrl !== '/auth') {
      router.navigateByUrl('/auth');
    }
    return false;
  }
};
