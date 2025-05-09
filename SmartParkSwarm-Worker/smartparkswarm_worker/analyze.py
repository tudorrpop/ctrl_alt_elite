import requests
import pandas as pd
from datetime import datetime, timedelta
import pytz
import matplotlib.pyplot as plt
import seaborn as sns

# === Constants === #
ENTRYTIME='entry_time'
EXITTIME='exit_time'
SPOTID='spot_uuid'

# === Config ===
API_URL = "http://127.0.0.1:8000/api/VehicleEntry/"
PARKING_SPOT_COUNT = 50  # TODO Retrieve this with a http request

# TODO These will be passed by the user when running the script
TODAY=False
LAST3DAYS=True
HOURSCROWDED=False
WEEKDAYSCROWDED=False
MONTHDAYSCROWDED=False
YEARDAYSCROWDED=False
YEARMONTHSCROWDED=False

def get_data(URL):
    response = requests.get(API_URL)
    return response.json

if LAST3DAYS:
    # === Fetch Data ===
    # TODO Add requests by timestamp
    entries = get_data(API_URL)

    # === Load into DataFrame ===
    df = pd.DataFrame(entries)
    df[ENTRYTIME] = pd.to_datetime(df[ENTRYTIME], utc=True)
    df[EXITTIME] = pd.to_datetime(df[EXITTIME], utc=True)

    # === Current time (for filtering future hours) ===
    now_utc = datetime.now(pytz.UTC)
    today = now_utc.date()

    # === Filter data: last 3 days ===
    start_date = today - timedelta(days=2)
    df_filtered = df[df[ENTRYTIME].dt.date >= start_date]

    # === Prepare hourly free spot data ===
    hourly_data = []
    daily_hourly_free_spots = {}

    # === Populate metrics ===
    for day_offset in range(3):
        date = start_date + timedelta(days=day_offset)
        hourly_spots = []

        for hour in range(24):
            start = datetime.combine(date, datetime.min.time()).replace(hour=hour, tzinfo=pytz.UTC)
            end = start + timedelta(hours=1)

            # Find overlapping entries
            overlapping = df_filtered[
                (df_filtered[ENTRYTIME] <= end) &
                ((df_filtered[EXITTIME].isna()) | (df_filtered[EXITTIME] >= start))
            ]

            used_spots = overlapping[SPOTID].nunique()
            free_spots = PARKING_SPOT_COUNT - used_spots

            # Set 0 for future hours
            if start > now_utc:
                used_spots = 0
                free_spost = 0

            hourly_data.append({'date': str(date), 'hour': hour, 'free_spots': free_spots})
            hourly_spots.append(free_spots)

            # # Track most crowded hours
            # crowded_hours[day_offset][hour] = used_spots

            # # Track crowded day metrics
            # crowded_days_of_week[start.weekday()] += used_spots
            # crowded_days_of_month[start.day] += used_spots
            # crowded_days_of_year[start.timetuple().tm_yday] += used_spots
            # crowded_months[start.month] += used_spots

        daily_hourly_free_spots[str(date)] = hourly_spots

    # === Save CSV ===
    df_csv = pd.DataFrame(hourly_data)
    df_csv.to_csv("free_spots_by_hour.csv", index=False)

    # === Heatmap for free spots ===
    heatmap_data = pd.DataFrame.from_dict(daily_hourly_free_spots, orient='index')
    heatmap_data = heatmap_data.sort_index()

    plt.figure(figsize=(12, 5))
    sns.heatmap(
        heatmap_data,
        annot=True,
        fmt="d",
        cmap="YlGnBu",
        cbar_kws={'label': 'Free Spots'},
        linewidths=.5
    )

    plt.title("Hourly Free Parking Spots (Last 3 Days)")
    plt.xlabel("Hour of Day")
    plt.ylabel("Date")
    plt.xticks(ticks=[i + 0.5 for i in range(24)], labels=[f"{i:02d}:00" for i in range(24)], rotation=45)
    plt.yticks(rotation=0)
    plt.tight_layout()
    plt.savefig("parking_heatmap.png")
    plt.show()


# TODO

# crowded_hours = {day: [0] * 24 for day in range(3)}  # Most crowded hours in last 3 days
# crowded_days_of_week = {i: 0 for i in range(7)}  # Most crowded days in week
# crowded_days_of_month = {i: 0 for i in range(1, 32)}  # Most crowded days in month (1-31)
# crowded_days_of_year = {i: 0 for i in range(1, 366)}  # Most crowded days in year (1-365)
# crowded_months = {i: 0 for i in range(1, 13)}  # Most crowded months (1-12)

# === Most Crowded Hours of the Day (last 3 days) ===
# top_5_crowded_hours = {}
# for day, hours in crowded_hours.items():
#     top_5_crowded_hours[day] = sorted(range(len(hours)), key=lambda x: hours[x], reverse=True)[:5]

# print("\nTop 5 Most Crowded Hours of the Day (last 3 days):")
# for day, hours in top_5_crowded_hours.items():
#     print(f"Day {start_date + timedelta(days=day)}: Hours {', '.join([f'{h}:00' for h in hours])}")

# # === Most Crowded Days of the Week ===
# top_5_crowded_weekdays = sorted(crowded_days_of_week.items(), key=lambda x: x[1], reverse=True)[:5]
# print("\nTop 5 Most Crowded Days of the Week:")
# for day, count in top_5_crowded_weekdays:
#     print(f"{['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'][day]}: {count} spots used")

# # === Most Crowded Days of the Month ===
# top_5_crowded_days_of_month = sorted(crowded_days_of_month.items(), key=lambda x: x[1], reverse=True)[:5]
# print("\nTop 5 Most Crowded Days of the Month:")
# for day, count in top_5_crowded_days_of_month:
#     print(f"Day {day}: {count} spots used")

# # === Most Crowded Days of the Year ===
# top_5_crowded_days_of_year = sorted(crowded_days_of_year.items(), key=lambda x: x[1], reverse=True)[:5]
# print("\nTop 5 Most Crowded Days of the Year:")
# for day, count in top_5_crowded_days_of_year:
#     print(f"Day {day}: {count} spots used")

# # === Most Crowded Months of the Year ===
# top_5_crowded_months = sorted(crowded_months.items(), key=lambda x: x[1], reverse=True)[:5]
# print("\nTop 5 Most Crowded Months of the Year:")
# for month, count in top_5_crowded_months:
#     print(f"{['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][month-1]}: {count} spots used")
