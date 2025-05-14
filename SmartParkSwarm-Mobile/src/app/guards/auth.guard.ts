import {CanActivateFn, CanMatchFn, Router} from '@angular/router';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {from, map} from "rxjs";

export const authGuard: CanActivateFn & CanMatchFn = (route, stateOrSegments) => {
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);

  let currentUrl: string = '';

  if (Array.isArray(stateOrSegments)) {
    currentUrl = '/' + stateOrSegments.map(s => s.path).join('/');
  } else {
    currentUrl = stateOrSegments.url;
  }

  return from(authService.isLoggedIn()).pipe(
    map(isLoggedIn => {
      if (isLoggedIn) {
        return true;
      } else {
        if (currentUrl !== '/auth') {
          router.navigateByUrl('/auth');
        }
        return false;
      }
    })
  );
};
