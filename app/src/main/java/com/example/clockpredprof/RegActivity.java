package com.example.clockpredprof;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Класс активности для регистрации пользователя.
 * @autor Пустовалов Данил
 */
public class RegActivity extends AppCompatActivity {
    /** Поле Менеджера базы данных */
    private DBManager dbManager;
    /** Поле для сенсеров */
    Sensor light;
    SensorManager sensorManager;
    /** Поля дизайна */
    ConstraintLayout layout;
    EditText city;
    EditText name1;
    Button con;
    @ColorInt
    int color = Color.parseColor("#DDDDDD");
    /** При создании приложения */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        dbManager = DBManager.getInstance(this);
        con=findViewById(R.id.Continue);
        city =findViewById(R.id.City);
        name1=findViewById(R.id.NameForReg);
        layout=findViewById(R.id.layoutReg);
        con.setOnClickListener(new View.OnClickListener() {
            /** При нажатии на кнопку */
            @Override
            public void onClick(View v) {
                dbManager.addResult(RegActivity.this.name1.getText().toString(),
                        RegActivity.this.city.getText().toString());
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        SensorEventListener listenerLight = new SensorEventListener() {
            EditText userName = findViewById(R.id.NameForReg);
            TextView City = findViewById(R.id.City);
            /** При изменении показателей сенсора */
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] > 1000) {
                    layout.setBackgroundColor(Color.parseColor("#B6B5B5"));
                    userName.setTextColor(Color.parseColor("#1E1E1E"));
                    City.setTextColor(Color.parseColor("#1E1E1E"));

                } else {
                    layout.setBackgroundColor(Color.parseColor("#1E1E1E"));
                    userName.setTextColor(color);
                    City.setTextColor(color);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(listenerLight, light, sensorManager.SENSOR_DELAY_NORMAL);
    }
}