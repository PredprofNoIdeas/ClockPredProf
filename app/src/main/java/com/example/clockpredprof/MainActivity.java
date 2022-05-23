package com.example.clockpredprof;


import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.clockpredprof.Weather.Main;
import com.example.clockpredprof.Weather.WeatherInWorld;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс главного экрана приложения.
 * @autor Пустовалов Данил
 */
public class MainActivity extends AppCompatActivity  {
    private DBManager dbManager;
    /** Поле для реализации времени */
    Time time;
    Handler handler;
    Runnable run;
    Runnable r;
    int hours, minutes, seconds, weekday, date;
    float battery;
    /** Поле для сенсеров */
    Sensor light;
    SensorManager sensorManager;
    /** Поля для дизайна */

    String city;
    String name;

    ConstraintLayout layout;
    @ColorInt
    int colorDark = Color.parseColor("#DDDDDD");
    @ColorInt
    int colorLight = Color.parseColor("#1E1E1E");

    /** При создании приложения */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            name=arguments.get("name").toString();
            Log.d("Name",name);
        }
        setContentView(R.layout.activity_main);

        TextView hum=findViewById(R.id.hum);
        TextView temp= findViewById(R.id.temp);
        TextView press= findViewById(R.id.press);

        TextView timeTV = findViewById(R.id.Time);
        TextView dateTV = findViewById(R.id.Date);
        TextView batteryTV = findViewById(R.id.Battery);

        final String[] pressure = new String[1];
        final String[] temperature = new String[1];
        final String[] humidity = new String[1];
        city=dbManager.getCity(name);
        time = new Time();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://covid-193.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherInterface service = retrofit.create(WeatherInterface.class);
        Call<WeatherInWorld> call = service.weatherInWorld(city, "metric");
        call.enqueue(new Callback<WeatherInWorld>() {
            @Override
            public void onResponse(Call<WeatherInWorld> call, Response<WeatherInWorld> response) {
                WeatherInWorld weatherInWorld = response.body();
                Log.d("RESPONSE", response.body()+"");
                if (weatherInWorld != null) {
                    Main r =  weatherInWorld.getMain();
                    humidity[0] ="Влажность: "+r.getHumidity();
                    pressure[0] ="Давление: "+r.getPressure();
                    if(r.getTemp()>0) {
                        temperature[0] = "Температура: +" + r.getTemp();
                    } else temperature[0] = "Температура: -" + r.getTemp();
                    Log.d("Weather", "Temp: "+r.getTemp()+"\nHumidity:" + r.getHumidity()+"\nPressure:"+ r.getPressure() + "");
                }
            }

            @Override
            public void onFailure(Call<WeatherInWorld> call, Throwable t) {

            }
        });

        SensorEventListener listenerLight = new SensorEventListener() {
            /** При изменении показателей сенсора */
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] > 1000) {
                    layout.setBackgroundColor(Color.parseColor("#B6B5B5"));
                    timeTV.setTextColor(colorLight);
                    dateTV.setTextColor(colorLight);
                    batteryTV.setTextColor(colorLight);
                    temp.setTextColor(colorLight);
                    press.setTextColor(colorLight);
                    hum.setTextColor(colorLight);

                } else {
                    layout.setBackgroundColor(Color.parseColor("#1E1E1E"));
                    timeTV.setTextColor(colorDark);
                    dateTV.setTextColor(colorDark);
                    batteryTV.setTextColor(colorDark);
                    temp.setTextColor(colorDark);
                    press.setTextColor(colorDark);
                    hum.setTextColor(colorDark);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(listenerLight, light, sensorManager.SENSOR_DELAY_NORMAL);

        r=new Runnable() {
            @Override
            public void run() {
               hum.setText(humidity[0]);
               temp.setText(temperature[0]);
               press.setText(pressure[0]);

               hum.setTextSize(18);

               temp.setTextSize(18);

               press.setTextSize(18);

            }
        };

        layout = findViewById(R.id.Main);
        run = new Runnable() {
            /** Отрисовка */
            @Override
            public void run() {
                MainActivity.this.time.setToNow();

                
                hours = MainActivity.this.time.hour + 3;
                minutes = MainActivity.this.time.minute;
                seconds = MainActivity.this.time.second;
                weekday = MainActivity.this.time.weekDay;
                MainActivity.this.date = MainActivity.this.time.monthDay;
                MainActivity.this.battery = getBatteryLevel();

                String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                String[] day_of_week = {"ПОН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
                String text2 = String.format("ДАТА: %s %d", day_of_week[weekday], MainActivity.this.date);
                String batteryLevel = "БАТАРЕЯ: " + (int) MainActivity.this.battery + "%";

                timeTV.setTextSize(36);
                timeTV.setText(text);

                dateTV.setTextSize(24);
                dateTV.setText(text2);

                batteryTV.setTextSize(24);
                batteryTV.setText(batteryLevel);

                handler.postDelayed(run, 1000);
            }
        };
        handler = new Handler();
        handler.postDelayed(run, 1000);
        handler.postDelayed(r,15000);
        Button toreg = findViewById(R.id.ToReg);
        toreg.setOnClickListener(new View.OnClickListener() {
           /** При нажитии на кнопку */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegActivity.class);
                startActivity(intent);
           }
        });
    }
    /**
     * Функция получения значения поля {@link MainActivity#battery}
     * @return возвращает показатель баттареи смартфона
     */
    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }
    
}