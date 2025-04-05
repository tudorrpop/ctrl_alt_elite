import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar.component';
import { StoreService } from '../../services/store/store.service';
import { TabsModule } from 'primeng/tabs';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { StoreOverviewModel } from '../../data/model/store-overview.model';
import { ButtonModule } from 'primeng/button';
import { StoreCreationComponent } from '../dialog/store-creation/store-creation.component';
import { DialogService } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    CardModule, 
    CommonModule, 
    TabsModule,
    ButtonModule
  ],
  providers: [
    DialogService
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit{

  stores!: StoreOverviewModel[];

  constructor(
    private storeService: StoreService,
    private dialogService: DialogService,
    private messageService: MessageService
  ){}

  public ngOnInit(): void {
    this.storeService.fetchStores().subscribe({
      next: (response) => {
        this.stores = response;
      }
    });
  }

  public navigateToStore(): void {
    
  }

  public createStore(): void {
    const ref = this.dialogService.open(StoreCreationComponent, {});
    ref.onClose.subscribe((data) => {
      if (data) {
        this.storeService.createStore(data).subscribe({
          next: (response) => {
            this.stores.push(response);

            this.messageService.add({
              severity: 'success',
              summary: 'Created Store!',
              detail: `Created store ${response.storeName} successfully.`,
            });
          },

          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Action Failed!',
              detail: 'Store creation process failed.',
            });
          }
        });

      } else {
        this.messageService.add({
          severity: 'warn',
          summary: 'Store Info!',
          detail: 'Store creation process canceled.',
        });
      }
    });
  }

}
