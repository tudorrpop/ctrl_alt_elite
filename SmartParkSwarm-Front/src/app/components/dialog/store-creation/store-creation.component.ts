import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DialogService, DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
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
export class StoreCreationComponent implements OnInit{

  storeName!: string;
  storeAddress!: string;
  parkingLayout!: string;

  edit!: boolean;
  id!: number;

  layoutSelectionDialog: boolean = false;

  layouts = [
    { name: "Grid", image: "/parking_layout/GRID.png"},
    { name: "Stripe", image: "/parking_layout/STRIPE.png"},
    { name: "Greenway", image: "/parking_layout/GREENWAY.png"},
  ];

  constructor(
    private ref: DynamicDialogRef,
    private config: DynamicDialogConfig
  ) {}

  ngOnInit(): void {
    if (this.config.data && this.config.data.storeModel) {
      this.storeName = this.config.data.storeModel.storeName;
      this.storeAddress = this.config.data.storeModel.storeAddress;
      this.parkingLayout = this.getParkingLayout(this.config.data.storeModel.parkingLayoutPath);
      this.id = this.config.data.storeModel.id;
      this.edit = true;
    } else {
      this.edit = false;
    }
  }

  openLayoutDialog() {
    this.layoutSelectionDialog = true;
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

  private getParkingLayout(parkingLayoutPath: string): string {
    console.log(parkingLayoutPath);
    if (parkingLayoutPath === "/parking_layout_svg/parking_layout_1.svg") {
      return "Grid";
    }else if (parkingLayoutPath === "/parking_layout_svg/parking_layout_2.svg") {
      return "Stripe";
    } else if (parkingLayoutPath === "/parking_layout_svg/parking_layout_3.svg") {
      return "Greenway";
    } else {
      return "";
    }
  }
}
