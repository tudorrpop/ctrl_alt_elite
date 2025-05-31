import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { CustomerModel } from '../../../data/model/customer.model';
import { DialogService, DynamicDialogConfig } from 'primeng/dynamicdialog';

@Component({
  selector: 'app-customer-info',
  imports: [
    DialogModule, 
    ButtonModule, 
    CommonModule],
  providers: [
    DialogService
  ],
  standalone: true,
  templateUrl: './customer-info.component.html',
  styleUrl: './customer-info.component.scss'
})
export class CustomerInfoComponent {

  user!: CustomerModel;

  constructor(
      private config: DynamicDialogConfig
    ) {}
  
    ngOnInit(): void {
      if (this.config.data && this.config.data.user) {
        this.user = this.config.data.user;
        console.log(this.user);
      }
    }
}
