package com.example.clockpredprof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * Класс активности для регистрации пользователя.
 * @autor Пустовалов Данил
 */
public class RegActivity extends AppCompatActivity {
    /** Поле Менеджера базы данных */
    static DBManager dbManagerж;
    /** Поля дизайна */
    EditText mail;
    EditText name1;
    Button con;
    /** При создании приложения */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        con=findViewById(R.id.Continue);
        mail=findViewById(R.id.Mail);
        name1=findViewById(R.id.NameForReg);
        con.setOnClickListener(new View.OnClickListener() {
            /** При нажатии на кнопку */
            @Override
            public void onClick(View v) {
                dbManager.addResult(RegActivity.this.name1.getText().toString(),
                        RegActivity.this.mail.getText().toString());
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}