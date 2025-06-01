import pandas as pd
from datetime import datetime, timedelta, timezone
import numpy as np
import os
import argparse
import requests
from urllib.parse import urlencode

def get_time_interval(metric):
    now = datetime.now(timezone.utc)
    if metric == 'free-spots-today':
        start_time = datetime.combine(now.date(), datetime.min.time(), tzinfo=timezone.utc)
    elif metric == 'free-spots-3-days':
        start_time = now - timedelta(days=3)
    elif metric in ['occupancy-by-weekday']:
        start_time = now - timedelta(weeks=4)
    elif metric in ['occupancy-by-month-day']:
        start_time = now - timedelta(days=4*30)
    elif metric in ['occupancy-by-month']:
        start_time = now - timedelta(days=365)
    else:
        raise ValueError("Unknown metric for date range")
    return start_time.isoformat(), now.isoformat()

def fetch_vehicle_data(api_url, metric):
    start_time, end_time = get_time_interval(metric)
    params = {
        "start_time": start_time,
        "end_time": end_time
    }
    full_url = f"{api_url}?{urlencode(params)}"
    response = requests.get(full_url)

    if response.status_code != 200:
        raise Exception(f"Failed to fetch data: {response.status_code} - {response.text}")
    
    data = response.json()
    return pd.DataFrame(data)


def calculate_metrics(df, metric, total_spots):
    now = datetime.now(timezone.utc)
    result_df = None
    MINUTES_PER_DAY = 24 * 60
    MAX_SPOT_MINUTES_PER_DAY = total_spots * MINUTES_PER_DAY

    df['entry_time'] = pd.to_datetime(df['entry_time'])
    df['exit_time'] = pd.to_datetime(df['exit_time'])
    df['exit_time'] = df['exit_time'].fillna(now + timedelta(hours=1))
    df['duration_minutes'] = (df['exit_time'] - df['entry_time']).dt.total_seconds() / 60.0

    if metric == 'free-spots-today':
        today = now.date()
        hours = pd.date_range(start=datetime.combine(today, datetime.min.time(), tzinfo=timezone.utc), end=(now), freq='h')
        result = []

        for hour in hours:
            in_hour = df[(df['entry_time'] <= hour) & (df['exit_time'] > hour)]
            free_spots = total_spots - in_hour['spot_uuid'].nunique()
            result.append({'hour': hour, 'free_spots': free_spots})

        result_df = pd.DataFrame(result)

    elif metric == 'free-spots-3-days':
        start_time = now - timedelta(days=3)
        hours = pd.date_range(start=start_time, end=now, freq='h')
        result = []

        for hour in hours:
            in_hour = df[(df['entry_time'] <= hour) & (df['exit_time'] > hour)]
            free_spots = total_spots - in_hour['spot_uuid'].nunique()
            result.append({'hour': hour, 'free_spots': free_spots})

        result_df = pd.DataFrame(result)
    
    elif metric == 'occupancy-by-weekday':
        four_weeks_ago = now - timedelta(weeks=4)
        df_filtered = df[df['entry_time'] >= four_weeks_ago]
        df_filtered['weekday'] = df_filtered['entry_time'].dt.day_name()
        df_filtered['day'] = df_filtered['entry_time'].dt.date
        agg = df_filtered.groupby(['weekday', 'day'])['duration_minutes'].sum().reset_index()
        daily = agg.groupby('weekday')['duration_minutes'].mean().reset_index()
        daily['occupancy_percent'] = (daily['duration_minutes'] / MAX_SPOT_MINUTES_PER_DAY) * 100
        result_df = daily[['weekday', 'occupancy_percent']].sort_values(by='occupancy_percent', ascending=False)

    elif metric == 'occupancy-by-month-day':
        four_months_ago = now - timedelta(days=4*30)
        df_filtered = df[df['entry_time'] >= four_months_ago].copy()
        df_filtered['day'] = df_filtered['entry_time'].dt.day

        agg = df_filtered.groupby('day')['duration_minutes'].sum().reset_index()
        agg['occupancy_percent'] = (agg['duration_minutes'] / MAX_SPOT_MINUTES_PER_DAY) * 100
        result_df = agg[['day', 'occupancy_percent']].sort_values(by='day')

    elif metric == 'occupancy-by-month':
        one_year_ago = now - timedelta(days=365)
        df_filtered = df[df['entry_time'] >= one_year_ago].copy()
        df_filtered['month_name'] = df_filtered['entry_time'].dt.strftime('%B')
        df_filtered['year_month'] = df_filtered['entry_time'].dt.to_period('M')

        # Sum duration per actual month
        monthly_agg = df_filtered.groupby(['month_name', 'year_month'])['duration_minutes'].sum().reset_index()

        # Average across same month names (e.g., average all Januarys)
        month_avg = monthly_agg.groupby('month_name')['duration_minutes'].mean().reset_index()
        month_avg['occupancy_percent'] = (month_avg['duration_minutes'] / MAX_SPOT_MINUTES_PER_DAY) * 100
        result_df = month_avg[['month_name', 'occupancy_percent']].sort_values(by='occupancy_percent', ascending=False)

    else:
        raise ValueError("Unknown metric")

    # Save to CSV
    filename = f'../metrics/{metric}.csv'
    result_df.to_csv(filename, index=False)
    return filename

def main():
    parser = argparse.ArgumentParser(description="Calculate parking metrics")
    parser.add_argument('--metric', required=True, help="Metric to calculate")
    parser.add_argument('--total-spots', type=int, required=True, help="Total number of parking spots")
    args = parser.parse_args()

    api_url = "http://127.0.0.1:8000/api/VehicleEntry"
    df = fetch_vehicle_data(api_url, args.metric)

    # Ensure datetime format
    df['entry_time'] = pd.to_datetime(df['entry_time'])
    df['exit_time'] = pd.to_datetime(df['exit_time'])

    filename = calculate_metrics(df, args.metric, args.total_spots)
    
    print(f"Metric '{args.metric}' written to {filename}")

if __name__ == "__main__":
    main()
