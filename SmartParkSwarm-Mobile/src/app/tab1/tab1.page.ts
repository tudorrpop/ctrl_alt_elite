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
import {QRCodeComponent} from "angularx-qrcode";

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss'],
  imports: [IonHeader, IonToolbar, IonTitle, IonContent, IonCard, IonCardHeader, IonCard, IonCardTitle, IonCardContent, IonIcon, ExploreContainerComponent, IonButtons, QRCodeComponent],
})
export class Tab1Page implements OnInit {
  currentUserName: any;
  currentMembership: any;
  isFlipped: boolean = false;
  qrData: any;

  constructor(private userService: UserService, private storageService: StorageService) {}

  ngOnInit() {
    this.setUserName();
    this.setUserUUID();
    this.setCurrentMembership(this.storageService.get('userId'));
  }

  flipCard() {
    this.isFlipped = !this.isFlipped;
  }

  setUserName() {
    this.currentUserName = this.storageService.get('username');
  }

  setUserUUID() {
    this.qrData = this.storageService.get('uuid');
    console.log(this.qrData);
  }

  setCurrentMembership(userId: any) {
    this.userService.getCurrentUser(userId).subscribe({
      next: (user: any) => {
        this.currentMembership = user.membership;
      },
      error: (error: any) => {
        console.error('Error fetching current user:', error);
      }
    });
  }
}
