import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar.component';
import { StoreService } from '../../services/store/store.service';
import { TabsModule } from 'primeng/tabs';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { StoreOverviewModel } from '../../data/model/store-overview.model';
import { ButtonModule } from 'primeng/button';
import { StoreCreationComponent } from '../dialog/store-creation/store-creation.component';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { CustomerModel } from '../../data/model/customer.model';
import { UserService } from '../../services/user/user.service';
import { GoogleMapsModule } from '@angular/google-maps';
import { CustomerInfoComponent } from '../dialog/customer-info/customer-info.component';
import { StatsService } from '../../services/stats/stats.service';
import { MonthStatisticsOcuppancy } from '../../data/statistics/overall-stats.model';
import { ChartModule } from 'primeng/chart';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    CardModule,
    CommonModule,
    TabsModule,
    ButtonModule,
    DynamicDialogModule,
    GoogleMapsModule,
    ChartModule
  ],
  providers: [
    DialogService
  ],
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit{

  stores!: StoreOverviewModel[];
  customers!: CustomerModel[];
  stats!: MonthStatisticsOcuppancy[]; 

  data: any;
  options: any;

  
  mapCenter: google.maps.LatLngLiteral = { lat: 45.7540, lng: 21.2275 }; 
  zoom = 12;

  markers = [
  {
    position: { lat: 45.7542, lng: 21.2265 },
    label: 'Kaufland Timișoara 1',
    title: 'Kaufland Timișoara Calea Martirilor 1989'
  },
  {
    position: { lat: 45.7498, lng: 21.2124 },
    label: 'Kaufland Timișoara 2',
    title: 'Kaufland Timișoara Strada Chimiștilor 5-9'
  },
  {
    position: { lat: 45.7612, lng: 21.2220 },
    label: 'Kaufland Timișoara 3',
    title: 'Kaufland Timișoara Calea Aradului 110'
  },
  {
    position: { lat: 45.7530, lng: 21.2300 },
    label: 'Kaufland Timișoara 4',
    title: 'Kaufland Timișoara Bd. Take Ionescu 11'
  }
];

  constructor(
    private router: Router,
    private storeService: StoreService,
    private userService: UserService,
    private dialogService: DialogService,
    private messageService: MessageService,
    private statsService: StatsService
  ) { 
  }

  public ngOnInit(): void {
  this.storeService.fetchStores().subscribe({
    next: async (response) => {
      this.stores = response;
    }
  });

  this.userService.fetchUsers().subscribe({
    next: (response) => {
      this.customers = response;
    }
  });

  this.statsService.fetchOverallStats().subscribe({
    next: (response) => {
      this.stats = response;
      this.prepareChartData(this.stats);
    }
  });
}


  public navigateToStore(storeId: number): void {
    this.router.navigate([`/store/${storeId}`]);
  }

  public createStore(): void {
    const ref = this.dialogService.open(StoreCreationComponent, {
      modal: true,
      closable: true
    });
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
  
  public showUserInfo(id: number): void {
    this.userService.fetchCustomer(id).subscribe({
    next: (response) => {
      const user = response;

      this.dialogService.open(CustomerInfoComponent, {
        data: { user: user },
        header: 'User Information',
        modal: true,
        closable: true
      });
      }
     });
  }

  prepareChartData(stats: MonthStatisticsOcuppancy[]) {
    const months = [...new Set(stats.map(stat => stat.month_name))];
    const stores = [...new Set(stats.map(stat => stat.store))];

    this.data = {
        labels: months,
        datasets: stores.map((store, index) => {
            return {
                label: store,
                data: months.map(month => {
                    const match = stats.find(stat => stat.store === store && stat.month_name === month);
                    return match ? match.occupancy_percent : 0;
                }),
                backgroundColor: this.getColor(index),
                borderColor: this.getColor(index)
            };
        })
    };
}

getColor(index: number): string {
    const colors = [
        '#06b6d4', // cyan
        '#9ca3af', // gray
        '#10b981', // green
        '#f59e0b', // amber
        '#ef4444', // red
        '#8b5cf6', // violet
    ];
    return colors[index % colors.length];
}



}
