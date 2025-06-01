import {Component} from '@angular/core';
import {IonButton, IonIcon} from "@ionic/angular/standalone";
import {Router} from "@angular/router";
import {addIcons} from "ionicons";
import {logOutOutline} from "ionicons/icons";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-explore-container',
  templateUrl: './explore-container.component.html',
  styleUrls: ['./explore-container.component.scss'],
  imports: [IonButton, IonIcon],
})
export class ExploreContainerComponent {
  constructor(private authService: AuthService, private router: Router) {
    addIcons({ logOutOutline });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/auth', { replaceUrl: true }).then(() => {
      console.log('Logged out and navigated to /auth');
    });
  }

}
