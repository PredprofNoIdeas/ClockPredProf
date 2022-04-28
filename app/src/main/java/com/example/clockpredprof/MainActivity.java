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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.clockpredprof.Weather.Main;
import com.example.clockpredprof.Weather.Weather;
import com.example.clockpredprof.Weather.WeatherInWorld;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс главного экрана приложения.
 * @autor Пустовалов Данил
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /** Поле для реализации времени */
    Time time;
    Handler handler;
    Runnable r;
    /** Поле для сенсеров */
    Sensor light;
    SensorManager sensorManager;
    /** Поля для дизайна */
    Button button;
    ConstraintLayout layout;
    int hours, minutes, seconds, weekday, date;
    float battery;
    @ColorInt
    int color = Color.parseColor("#DDDDDD");
    @ColorInt
    int color1 = Color.parseColor("#707070");



    /** При создании приложения */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = new Time();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://community-open-weather-map.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherInterface service = retrofit.create(WeatherInterface.class);
        Call<WeatherInWorld> call = service.weatherInWorld("Moscow");
        call.enqueue(new Callback<WeatherInWorld>() {
            @Override
            public void onResponse(Call<WeatherInWorld> call, Response<WeatherInWorld> response) {
                WeatherInWorld weatherInWorld = response.body();
                if (weatherInWorld != null) {
                    Main r =  weatherInWorld.getMain();
                    Log.d("Weather", r.getTemp() + r.getHumidity()+ r.getPressure() + "");
                }
                else Log.d("ERROR", "null");
            }

            @Override
            public void onFailure(Call<WeatherInWorld> call, Throwable t) {

            }
        });

        SensorEventListener listenerLight = new SensorEventListener() {
            TextView timeTV = findViewById(R.id.Time);
            TextView dateTV = findViewById(R.id.Date);
            TextView batteryTV = findViewById(R.id.Battery);
            /** При изменении показателей сенсора */
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] > 1000) {
                    layout.setBackgroundColor(Color.parseColor("#B6B5B5"));
                    timeTV.setTextColor(Color.parseColor("#1E1E1E"));
                    dateTV.setTextColor(Color.parseColor("#1E1E1E"));
                    batteryTV.setTextColor(Color.parseColor("#1E1E1E"));

                } else {
                    layout.setBackgroundColor(Color.parseColor("#1E1E1E"));
                    timeTV.setTextColor(color);
                    dateTV.setTextColor(color);
                    batteryTV.setTextColor(color);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(listenerLight, light, sensorManager.SENSOR_DELAY_NORMAL);

        TextView textView = findViewById(R.id.Time);
        TextView textView2 = findViewById(R.id.Date);
        TextView textView3 = findViewById(R.id.Battery);
        button = findViewById(R.id.Photo);
        layout = findViewById(R.id.Main);
        r = new Runnable() {
            /** Отрисовка */
            @Override
            public void run() {
                time.setToNow();

                hours = time.hour + 3;
                minutes = time.minute;
                seconds = time.second;
                weekday = time.weekDay;
                date = time.monthDay;
                battery = getBatteryLevel();

                String text = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                String[] day_of_week = {"ПОН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
                String text2 = String.format("ДАТА: %s %d", day_of_week[weekday], date);
                String batteryLevel = "БАТАРЕЯ: " + (int) battery + "%";

                textView.setTextSize(36);
                textView.setText(text);

                textView2.setTextSize(24);
                textView2.setText(text2);

                textView3.setTextSize(24);
                textView3.setText(batteryLevel);

                handler.postDelayed(r, 1000);
            }
        };
        handler = new Handler();
        handler.postDelayed(r, 1000);
        button.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CAMERA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_CAMERA));
        sendOrderedBroadcast(intent, null);
    }
}