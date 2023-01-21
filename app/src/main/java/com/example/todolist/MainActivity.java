package com.example.todolist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.adapters.TaskRecycleViewAdapter;
import com.example.todolist.models.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> tasks = new ArrayList<>();
    RecyclerView recyclerView;
    TaskRecycleViewAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.task_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasks = new ArrayList<>();
        adapter = new TaskRecycleViewAdapter(tasks, MainActivity.this);
        recyclerView.setAdapter(adapter);

        eventChangeListener();

    }

    private void eventChangeListener() {
        db.collection("tasks")
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w("errore", "Listen failed.", e);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange documentChange: value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                tasks.add(documentChange.getDocument().toObject(Task.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

}