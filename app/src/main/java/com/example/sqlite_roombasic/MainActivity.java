package com.example.sqlite_roombasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Button btnAdd, btnRemove, btnCancel;
    private EditText editValue;
    private List<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database_test").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        list = userDao.getAll();

        recyclerView = findViewById(R.id.rcv_view);
        editValue = findViewById(R.id.editValue);
        Intent intent = getIntent();
        editValue.setText(intent.getStringExtra("name"));
        btnAdd = findViewById(R.id.btnAdd);

        customAdapter = new CustomAdapter(this);
        customAdapter.setData(list);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        btnAdd.setOnClickListener(v -> {
            String value = editValue.getText().toString().trim();
            if(value.isEmpty())
                editValue.setError("Vui lòng chọn User!");
            else
                addUser();
        });

        btnRemove = findViewById(R.id.btnRemove);

        btnRemove.setOnClickListener(v -> {
            String value = editValue.getText().toString().trim();
            if(value.isEmpty())
                editValue.setError("Vui lòng chọn User!");
            else
                deleteUser();
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> editValue.setText(""));
    }

    public void addUser(){
        String value;
        value = editValue.getText().toString().trim();
        User user;
        user = new User(value);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database_test").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        userDao.insertUser(user);
        //list = userDao.getAll();
        list.add(user);
        customAdapter.setData(list);
        Toast.makeText(this," add susscess!", Toast.LENGTH_SHORT).show();
        editValue.setText("");
    }

    public void deleteUser(){
        String value = editValue.getText().toString().trim();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database_test").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        User user = userDao.findByName(value);
        userDao.delete(user);
        list = userDao.getAll();
        customAdapter.setData(list);
        Toast.makeText(this," delete susscess!", Toast.LENGTH_SHORT).show();
        editValue.setText("");
    }
}