import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { TooltipModule } from 'primeng/tooltip';
import { StoreRequest } from '../../../data/request/store.request';

@Component({
  selector: 'app-store-creation',
  standalone: true,
  imports: [
    FormsModule,
    ButtonModule,
    DialogModule,
    CommonModule,
    TooltipModule
  ],
  providers: [
    DialogService
  ],
  templateUrl: './store-creation.component.html',
  styleUrl: './store-creation.component.scss'
})
export class StoreCreationComponent {

  storeName!: string;
  storeAddress!: string;
  parkingLayout!: string;

  layoutSelectionDialog: boolean = false;

  layouts = [
    { name: "Grid", image: "/parking_layout/GRID.png" },
    { name: "Stripe", image: "/parking_layout/STRIPE.png" },
    { name: "Greenway", image: "/parking_layout/GREENWAY.png" }
  ];

  constructor(
    private ref: DynamicDialogRef
  ) {}

  openLayoutDialog() {
    this.layoutSelectionDialog = true;
    console.log(this.layoutSelectionDialog);
  }

  selectLayout(layoutName: string) {
    this.parkingLayout = layoutName;
    this.layoutSelectionDialog = false;
  }

  public cancelAction(): void{
    this.ref.close(null);
  }

  saveAction(): void {
    this.ref.close({
      storeName: this.storeName,
      storeAddress: this.storeAddress,
      parkingLayout: this.parkingLayout
    } as StoreRequest);
  }
}
