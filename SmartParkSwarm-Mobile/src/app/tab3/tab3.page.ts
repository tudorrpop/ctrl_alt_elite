import {Component, OnInit} from '@angular/core';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonButtons,
  IonItem,
  IonLabel,
  IonInput,
  IonButton,
} from '@ionic/angular/standalone';
import { ExploreContainerComponent } from '../explore-container/explore-container.component';
import {StorageService} from "../services/storage.service";
import {FormsModule} from "@angular/forms";
import {UserService} from "../services/user.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-tab3',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss'],
  imports: [IonButton, IonHeader, IonToolbar, IonTitle, IonContent, ExploreContainerComponent, IonButtons, IonItem, IonLabel, IonInput, FormsModule, NgIf],
})
export class Tab3Page implements OnInit {
  currentUser: any;
  originalUser: any;
  isModified: boolean = false;

  constructor(private storageService: StorageService, private userService: UserService) {}

  ngOnInit() {
    this.setCurrentUser(this.storageService.get('userId'));
  }

  setCurrentUser(userId: any) {
    this.userService.getCurrentUser(userId).subscribe({
      next: (user: any) => {
        this.currentUser = user;
        this.originalUser = user;
      },
      error: (error: any) => {
        console.error('Error fetching current user:', error);
      }
    });
  }

  onInputChange() {
    if(!this.isModified) {
      this.isModified = true;
    }
  }

  saveChanges() {
    this.userService.updateUser(this.currentUser.userId, this.currentUser).subscribe({
      next: (updatedUser: any) => {
        this.originalUser = { ...updatedUser };
        this.currentUser = { ...updatedUser };
        this.isModified = false;
      },
      error: (error: any) => {
        console.error('Error updating user:', error);
      }
    });
  }
}
