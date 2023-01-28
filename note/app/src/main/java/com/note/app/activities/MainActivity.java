package com.note.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.note.app.R;
import com.note.app.adapters.NotesAdapter;
import com.note.app.models.NoteDataModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView noNotes;
    RecyclerView recyclerView;
    NotesAdapter adapter;
    ArrayList<NoteDataModel> notesList = new ArrayList<NoteDataModel>();

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.main_progress);
        noNotes = findViewById(R.id.main_no_notes);
        recyclerView = findViewById(R.id.notes_recycler);

        // get notes from firebase database
        getNotes();

    }

    void setRecycler() {

        // if notes is empty
        if (notesList.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            noNotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        } else {  // if get notes succeed
            progressBar.setVisibility(View.GONE);
            noNotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new NotesAdapter(this, notesList);
            recyclerView.setAdapter(adapter);
        }
    }

    public void getNotes() {

        db.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                notesList.clear();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        String id = (String) item.child("id").getValue();
                        String body = (String) item.child("body").getValue();

                        notesList.add(new NoteDataModel(id, body));
                    }
                }
                // set recycler view
                setRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    // create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // options menu on item click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_note) {
            startActivity(new Intent(this, AddNoteActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}