import { Component, OnInit } from '@angular/core';
import { StoreModel } from '../../data/model/store.model';
import { StoreService } from '../../services/store/store.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { NavbarComponent } from "../shared/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ParkingSpotStatus } from '../../data/model/parking-spot-status.model';

@Component({
  selector: 'app-store',
  standalone: true,
  imports: [
    NavbarComponent,
    ButtonModule,
    DividerModule
  ],
  templateUrl: './store.component.html',
  styleUrl: './store.component.scss'
})
export class StoreComponent implements OnInit{

  store: StoreModel = {} as StoreModel;
  eventSource!: EventSource;

  constructor(
    private storeService: StoreService,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService
  ){}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {

      const storeId = params.get('storeId');
      if (storeId) {
      this.storeService.fetchStore(+storeId).subscribe({
        next: (storeModel) => {
          this.store = storeModel;
          this.storeService.initialParkingLotStatus().subscribe({
            next: (statuses) => {
              this.updateParkingSpotColors(statuses);
            }
          });
        },

        error: () => {
          this.messageService.add({
            severity: 'warn',
            summary: 'Oops! Something went wrong.',
            detail: 'Unable to open the store page.',
          });
          this.router.navigate(['/dashboard']);
        }
      });
      }
    });

    // ParkinLot UPDATES
    this.eventSource = new EventSource('http://localhost:8083/sse');
    this.eventSource.addEventListener('message', (event: MessageEvent) => {
      this.updateParkingSpotColors(JSON.parse(event.data));
    });
    
  }

  ngOnDestroy(): void {
    this.eventSource.close();
  }

  public updateParkingSpotColors(statuses: ParkingSpotStatus[]) {
    statuses.forEach(spot => {
      const rect = document.getElementById(spot.id) as SVGRectElement | null;
      if (rect) {
        rect.setAttribute("fill", spot.occupied ? "#DC143C" : "#228B22");
      }
    });
  }

  public goBack(): void{
    this.router.navigate(['/dashboard']);
  }

  public removeStore(): void {
    this.storeService.deleteStore(this.store.storeId).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      }
    });
  }
}
