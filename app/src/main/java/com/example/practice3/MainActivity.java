package com.example.practice3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.practice3.DBHelper.KEY_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnRead, btnChange, btnClear;
    EditText etFIO;
    int elCount = 0;
    DBHelper dbHelper;
    ArrayList names = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etFIO = (EditText) findViewById(R.id.etFIO);

        dbHelper = new DBHelper(this);

        String[] rndNames = new String[]{"Дмитрий", "Сергей", "Иван", "Артём", "Вячеслав", "Илья", "Даниил", "Григорий", "Евгений", "Никита", "Николай"};
        String[] rndSurnames = new String[]{"Пучков", "Большаков", "Васильев", "Андреев", "Петров", "Иванов", "Синицын", "Сергеев"};
        String[] rndSecondnames = new String[]{"Витальевич", "Иванович", "Ильич", "Сергеевич", "Альбертович", "Николаевич", "Евгеньевич"};

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        final String finalDateAndTime = dateText + ", " + timeText;

        database.delete(DBHelper.TABLE_STUDENTS, null, null);



        for (int i = 0; i < 5; i++)
        {
            int n1 = (int)Math.floor(Math.random() * rndNames.length);
            int n2 = (int)Math.floor(Math.random() * rndSurnames.length);
            int n3 = (int)Math.floor(Math.random() * rndSecondnames.length);
            String rndFIO = rndSurnames[n2] + " " + rndNames[n1] + " " + rndSecondnames[n3];
            names.add(rndFIO);
            contentValues.put(DBHelper.KEY_FIO, rndFIO);
            contentValues.put(DBHelper.KEY_TIME, finalDateAndTime);
            database.insert(DBHelper.TABLE_STUDENTS, null, contentValues);
        }
        elCount = 5;
    }

    @Override
    public void onClick(View v)
    {

        String fio = etFIO.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        final String finalDateAndTime = dateText + ", " + timeText;

        switch (v.getId())
        {
            case R.id.btnAdd:
                if (fio.equals(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Вы не заполнили поле 'ФИО'.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (names.contains(fio))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Такой человек уже есть в базе.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    contentValues.put(DBHelper.KEY_FIO, fio);
                    contentValues.put(DBHelper.KEY_TIME, finalDateAndTime);
                    names.add(fio);
                    database.insert(DBHelper.TABLE_STUDENTS, null, contentValues);
                    elCount++;
                }
                break;

            case R.id.btnRead:
                Intent intent = new Intent(MainActivity.this, TableActivityV1.class);
                startActivity(intent);
                break;

            case R.id.btnChange:
                if (names.contains("Иванов Иван Иванович"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Такой человек уже есть в базе.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    contentValues.put(DBHelper.KEY_FIO, "Иванов Иван Иванович");
                    String where = KEY_ID + "=" + elCount;
                    names.set(elCount - 1, "Иванов Иван Иванович");
                    database.update(DBHelper.TABLE_STUDENTS, contentValues, where, null);
                }
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_STUDENTS, null, null);
                names.clear();
                elCount = 0;
                break;
        }
        dbHelper.close();
    }
}
