import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { MonthStatisticsOcuppancy } from '../../data/statistics/overall-stats.model';
import { Observable } from 'rxjs';
import { SpotInfo } from '../../data/statistics/spot-info.model';
import { WeekdayOcuppancy } from '../../data/statistics/weekday-occupancy.model';
import { MonthDayOcuppancy } from '../../data/statistics/monthday-occupancy.model';
import { MonthOcuppancy } from '../../data/statistics/month-occupancy.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StatsService {

  private baseUrl = environment.apiUrl;

  constructor(
    private httpClient: HttpClient,
    private cookieService: CookieService
  ) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.cookieService.get('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  public fetchOverallStats(): Observable<MonthStatisticsOcuppancy[]> {
    const headers = this.getAuthHeaders();
  
    return this.httpClient.get<MonthStatisticsOcuppancy[]>(
      `${this.baseUrl}/worker/statistics/overall`,
      { headers, withCredentials: true }
    );
  }

  public fetchLastThreeDays(id: number): Observable<SpotInfo[]> {
    const headers = this.getAuthHeaders();
  
    return this.httpClient.get<SpotInfo[]>(
      `${this.baseUrl}/worker/metrics/threedays?id=${id}`,
      { headers, withCredentials: true }
    );
  }

  public fetchToday(id: number): Observable<SpotInfo[]> {
    const headers = this.getAuthHeaders();
  
    return this.httpClient.get<SpotInfo[]>(
      `${this.baseUrl}/worker/metrics/today?id=${id}`,
      { headers, withCredentials: true }
    );
  }

  public fetchByMonths(id: number): Observable<MonthOcuppancy[]> {
    const headers = this.getAuthHeaders();
  
    return this.httpClient.get<MonthOcuppancy[]>(
      `${this.baseUrl}/worker/metrics/occupancymonth?id=${id}`,
      { headers, withCredentials: true }
    );
  }

  public fetchByDays(id: number): Observable<MonthDayOcuppancy[]> {
    const headers = this.getAuthHeaders();
  
    return this.httpClient.get<MonthDayOcuppancy[]>(
      `${this.baseUrl}/worker/metrics/occupancymonthday?id=${id}`,
      { headers, withCredentials: true }
    );
  }

  public fetchByWeek(id: number): Observable<WeekdayOcuppancy[]> {
    const headers = this.getAuthHeaders();
  
    return this.httpClient.get<WeekdayOcuppancy[]>(
      `${this.baseUrl}/worker/metrics/occupancyweekday?id=${id}`,
      { headers, withCredentials: true }
    );
  }
  


  // !!! --- HELPER METHODS --- !!!

  prepareWeekdayOccupancyChartData(stats: WeekdayOcuppancy[]) {
  const labels = stats.map(s => s.weekday);
  const values = stats.map(s => s.occupancy_percent);

  const data = {
    labels,
    datasets: [
      {
        label: 'Occupancy %',
        data: values,
        backgroundColor: this.getColor(2),
        borderColor: this.getColor(2),
        fill: false,
        tension: 0.4
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: '#495057'
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: '#495057'
        },
        grid: {
          color: '#ebedef'
        }
      },
      y: {
        ticks: {
          color: '#495057',
          callback: (value: number) => value + '%'
        },
        grid: {
          color: '#ebedef'
        },
        title: {
          display: true,
          text: 'Occupancy %',
          color: '#495057'
        }
      }
    }
  };

  return { data, options };
}

  prepareDailyOccupancyChartData(stats: MonthDayOcuppancy[]) {
  const labels = stats.map(s => s.day);
  const values = stats.map(s => s.occupancy_percent);

  const data = {
    labels,
    datasets: [
      {
        label: 'Occupancy %',
        data: values,
        backgroundColor: this.getColor(1),
        borderColor: this.getColor(1),
        fill: false,
        tension: 0.4
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: '#495057'
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: '#495057'
        },
        grid: {
          color: '#ebedef'
        }
      },
      y: {
        ticks: {
          color: '#495057',
          callback: (value: number) => value + '%'
        },
        grid: {
          color: '#ebedef'
        },
        title: {
          display: true,
          text: 'Occupancy %',
          color: '#495057'
        }
      }
    }
  };

  return { data, options };
}


    prepareMonthlyOccupancyChartData(stats: MonthOcuppancy[]) {
    const labels = stats.map(s => s.month_name);
    const values = stats.map(s => s.occupancy_percent);

    const data = {
      labels,
      datasets: [
        {
          label: 'Occupancy %',
          data: values,
          backgroundColor: this.getColor(0),
          borderColor: this.getColor(0),
          fill: false,
          tension: 0.4
        }
      ]
    };

    const options = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: {
            color: '#495057'
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: '#495057'
          },
          grid: {
            color: '#ebedef'
          }
        },
        y: {
          ticks: {
            color: '#495057',
            callback: (value: number) => value + '%'
          },
          grid: {
            color: '#ebedef'
          },
          title: {
            display: true,
            text: 'Occupancy %',
            color: '#495057'
          }
        }
      }
    };

    return { data, options };
  }

  prepareSpotChartData(stats: SpotInfo[]) {
    const labels = stats.map(s =>
      new Date(s.hour).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    );

    const data = {
      labels,
      datasets: [
        {
          label: 'Free Spots',
          data: stats.map(s => s.free_spots),
          backgroundColor: this.getColor(0),
          borderColor: this.getColor(0),
          fill: false,
          tension: 0.4
        }
      ]
    };

    const options = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: {
            color: '#495057'
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: '#495057'
          },
          grid: {
            color: '#ebedef'
          }
        },
        y: {
          ticks: {
            color: '#495057'
          },
          grid: {
            color: '#ebedef'
          }
        }
      }
    };

    return { data, options };
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
