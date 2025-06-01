import { Component, OnInit } from '@angular/core';
import { StoreModel } from '../../data/model/store.model';
import { StoreService } from '../../services/store/store.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { NavbarComponent } from "../shared/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ParkingSpotStatus } from '../../data/model/parking-spot-status.model';
import { DialogService } from 'primeng/dynamicdialog';
import { StoreCreationComponent } from '../dialog/store-creation/store-creation.component';
import { HttpClient } from '@angular/common/http';
import { TabsModule } from 'primeng/tabs';
import { SelectModule} from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { ChartModule } from 'primeng/chart';
import { StatsService } from '../../services/stats/stats.service';
import { SpotInfo } from '../../data/statistics/spot-info.model';
import { environment } from '../../../environments/environment';

interface Report {
    name: string;
    type: string;
}

@Component({
  selector: 'app-store',
  standalone: true,
  imports: [
    NavbarComponent,
    ButtonModule,
    DividerModule,
    TabsModule,
    SelectModule,
    FormsModule,
    DropdownModule,
    ChartModule
  ],
  providers: [
    DialogService
  ],
  templateUrl: './store.component.html',
  styleUrl: './store.component.scss'
})

export class StoreComponent implements OnInit {

  apiUrl = environment.apiUrl;
  store: StoreModel = {} as StoreModel;
  eventSource!: EventSource;
  svgContent: string = '';
  
  data: any;
  options: any;

  cities: Report[] | undefined = [
    { name: 'Free Spots - Last 3 days', type: 'last-3-days' },
    { name: 'Free Spots - Today', type: 'today' },
    { name: 'Occupancy by months', type: 'by-months' },
    { name: 'Occupancy by days', type: 'by-days' },
    { name: 'Occupancy by week', type: 'by-week' }
  ];

  selectedCity: Report | undefined;

  constructor(
    private storeService: StoreService,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private dialogService: DialogService,
    private http: HttpClient,
    private statsService: StatsService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const storeId = params.get('storeId');
      if (storeId) {
        this.storeService.fetchStore(+storeId).subscribe({
          next: (storeModel) => {
            this.store = storeModel;

            this.http.get(storeModel.parkingLayoutPath.toString(), { responseType: 'text' }).subscribe({
              next: (svg) => {
                const container = document.getElementById('svg-container');
                if (container) {
                  container.innerHTML = svg;

                  console.log(this.store.storeId);
                  this.storeService.initialParkingLotStatus(this.store.storeId).subscribe({
                    next: (statuses) => {
                      this.updateParkingSpotColors(statuses);
                    }
                  });


                  this.eventSource = new EventSource(`${this.apiUrl}/sse`);
                  this.eventSource.addEventListener('message', (event: MessageEvent) => {
                    this.updateParkingSpotColors(JSON.parse(event.data));
                  });
                }
              },
              error: () => {
                console.error('Failed to load SVG');
              }
            },
            error: () => {
              console.error('Failed to load SVG');
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
    this.eventSource = new EventSource(`${this.apiUrl}/sse`);
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

  public goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  public removeStore(): void {
    this.storeService.deleteStore(this.store.storeId).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      }
    });
  }

  public updateStore(): void {
    const ref = this.dialogService.open(StoreCreationComponent, {
      closable: true,
      modal: true,
      data: {
        storeModel: this.store,
      }
    });
    ref.onClose.subscribe((data) => {
      if (data) {
        this.storeService.updateStore(this.store.storeId, data).subscribe({
          next: (response) => {
            this.store = response;

            this.messageService.add({
              severity: 'success',
              summary: 'Created Store!',
              detail: `Created store ${response.storeName} successfully.`,
            });
          },
        });

      }
    });
  }

  public fetchReport(): void {
  switch (this.selectedCity?.type) {
    case 'last-3-days':
      this.statsService.fetchLastThreeDays(this.store.storeId).subscribe({
        next: (response) => {
          const chart = this.statsService.prepareSpotChartData(response);
          this.data = chart.data;
          this.options = chart.options;
        }
      });
      break;

    case 'today':
      this.statsService.fetchToday(this.store.storeId).subscribe({
        next: (response) => {
          const chart = this.statsService.prepareSpotChartData(response);
          this.data = chart.data;
          this.options = chart.options;
        }
      });
    break;

    case 'by-months':
      this.statsService.fetchByMonths(this.store.storeId).subscribe({
        next: (response) => {
          const chart = this.statsService.prepareMonthlyOccupancyChartData(response);
          this.data = chart.data;
          this.options = chart.options;
        }
      });
    break;

    case 'by-days':
      this.statsService.fetchByDays(this.store.storeId).subscribe({
        next: (response) => {
          const chart = this.statsService.prepareDailyOccupancyChartData(response);
          this.data = chart.data;
          this.options = chart.options;
        }
      });
    break;

    case 'by-week':
      this.statsService.fetchByWeek(this.store.storeId).subscribe({
        next: (response) => {
          const chart = this.statsService.prepareWeekdayOccupancyChartData(response);
          this.data = chart.data;
          this.options = chart.options;
        }
      });
    break;

    default:
      console.warn('Unknown report type:', this.selectedCity?.type);
      break;
  }
  }

}
