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
import {HttpClient} from "@angular/common/http";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-tab3',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss'],
  imports: [IonButton, IonHeader, IonToolbar, IonTitle, IonContent, ExploreContainerComponent, IonButtons, IonItem, IonLabel, IonInput, FormsModule],
})
export class Tab3Page implements OnInit {
  accountData = {
    username: '',
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: ''
  };

  constructor(private storageService: StorageService, private http: HttpClient) {}

  async ngOnInit() {
    const username = await this.storageService.get<string>('username');
    const userId = await this.storageService.get<string>('userId');
    this.accountData.username = username || '';

    // Fetch existing user details (fake api call example)
    this.http.get<any>(`http://localhost:8083/users/${userId}`).subscribe({
      next: (user) => {
        this.accountData.firstName = user.firstName;
        this.accountData.lastName = user.lastName;
        this.accountData.email = user.email;
        this.accountData.phoneNumber = user.phoneNumber;
      },
      error: (err) => {
        console.error('Failed to load user details', err);
      }
    });
  }

  updateAccount() {
    const userId = this.storageService.get<string>('userId');

    Promise.resolve(userId).then(id => {
      if (id) {
        this.http.put(`http://localhost:8083/users/${id}`, this.accountData).subscribe({
          next: () => {
            console.log('Account updated successfully');
          },
          error: (err) => {
            console.error('Failed to update account', err);
          }
        });
      }
    });
  }
}
