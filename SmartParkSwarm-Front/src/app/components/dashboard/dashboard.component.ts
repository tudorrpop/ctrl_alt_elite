import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../shared/navbar/navbar.component';
import { StoreService } from '../../services/store/store.service';
import { TabsModule } from 'primeng/tabs';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { StoreOverviewModel } from '../../data/model/store-overview.model';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent,
    CardModule, 
    CommonModule, 
    TabsModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit{

  stores!: StoreOverviewModel[];

  constructor(
    private storeService: StoreService
  ){}

  public ngOnInit(): void {
    this.storeService.getStores().subscribe({
      next: (response) => {
        this.stores = response;
      }
    });
  }

  public navigateToStore(): void {
    
  }

}
