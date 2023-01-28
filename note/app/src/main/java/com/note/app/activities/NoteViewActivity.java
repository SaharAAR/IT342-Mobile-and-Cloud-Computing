package com.note.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.note.app.R;
import com.note.app.models.NoteDataModel;

public class NoteViewActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button edit;
    EditText noteText;

    NoteDataModel note;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteText = findViewById(R.id.note_view_text);
        edit = findViewById(R.id.note_view_button);
        progressBar = findViewById(R.id.note_view_progress);

        // get note data which passed by MainActivity
        String id = getIntent().getExtras().getString("id");
        String body = getIntent().getExtras().getString("body");

        note = new NoteDataModel(id, body);

        // show note in edit text
        noteText.setText(note.body);

        // edit button on click
        edit.setOnClickListener(view -> editNote());

    }

    void editNote() {

        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        // show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // edit note in firebase database
        db.child("notes").child(note.id).child("body").setValue(noteText.getText().toString()).addOnCompleteListener(task -> {

            // hide progress bar
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                Toast.makeText(NoteViewActivity.this, "succeed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NoteViewActivity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    void deleteNote() {

        // show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // delete note in firebase database
        db.child("notes").child(note.id).removeValue().addOnCompleteListener(task -> {

            // hide progress bar
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                Toast.makeText(NoteViewActivity.this, "succeed", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(NoteViewActivity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    // create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_note) {
            deleteNote();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}