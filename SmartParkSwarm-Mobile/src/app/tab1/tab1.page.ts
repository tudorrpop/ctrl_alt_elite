import { Component, OnInit } from '@angular/core';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonCardHeader,
  IonCard,
  IonCardTitle, IonCardContent, IonIcon, IonButtons
} from '@ionic/angular/standalone';
import {NgOptimizedImage} from "@angular/common";
import {ExploreContainerComponent} from "../explore-container/explore-container.component";
import {UserService} from "../services/user.service";
import {StorageService} from "../services/storage.service";

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss'],
  imports: [IonHeader, IonToolbar, IonTitle, IonContent, IonCard, IonCardHeader, IonCard, IonCardTitle, IonCardContent, IonIcon, NgOptimizedImage, ExploreContainerComponent, IonButtons],
})
export class Tab1Page implements OnInit {
  currentUser: any;
  currentUserName: any;
  isFlipped = false;
  qrData = 'https://your-customer-link-or-info.com';

  constructor(private userService: UserService, private storageService: StorageService) {}

  async ngOnInit() {
    await this.setCurrentUser();
    await this.setUserName();
  }

  flipCard() {
    this.isFlipped = !this.isFlipped;
  }

  async setCurrentUser() {
    this.userService.getCurrentUser().subscribe((user) => {
      this.currentUser = user;
      console.log(this.currentUser);
    });
  }

  async setUserName() {
    this.currentUserName = await this.storageService.get('username');
    console.log(this.currentUserName);
  }
}
