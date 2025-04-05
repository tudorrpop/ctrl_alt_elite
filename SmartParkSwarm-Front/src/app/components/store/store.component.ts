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

  parkingStatuses: ParkingSpotStatus[] = [
    { id: "restricted_1", occupied: true },
    { id: "restricted_2", occupied: false },

    { id: "electric_1", occupied: false },
    { id: "electric_2", occupied: false },
    { id: "electric_3", occupied: true },
    { id: "electric_4", occupied: false },
    { id: "electric_5", occupied: false },
    { id: "electric_6", occupied: true },
    { id: "electric_7", occupied: true },

    { id: "top_1", occupied: false },
    { id: "top_2", occupied: true },
    { id: "top_3", occupied: false },
    { id: "top_4", occupied: true },
    { id: "top_5", occupied: false },
    { id: "top_6", occupied: false },
    { id: "top_7", occupied: true },

    { id: "coffee_1", occupied: false },
    { id: "coffee_2", occupied: false },
    { id: "coffee_3", occupied: true },

    { id: "bottom_1", occupied: true },
    { id: "bottom_2", occupied: true },
    { id: "bottom_3", occupied: false },
    { id: "bottom_4", occupied: false },
    { id: "bottom_5", occupied: true },
    { id: "bottom_6", occupied: false },
    { id: "bottom_7", occupied: true },
    { id: "bottom_8", occupied: true },
    { id: "bottom_9", occupied: false },
    { id: "bottom_10", occupied: false },
  ];
  

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
          this.updateParkingSpotColors(this.parkingStatuses);
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

    // Just for TESTING
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
}
