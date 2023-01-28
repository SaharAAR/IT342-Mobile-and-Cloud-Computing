package com.note.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.note.app.R;
import com.note.app.models.NoteDataModel;

public class AddNoteActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button addNote;
    EditText noteText;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.add_note_progress);
        noteText = findViewById(R.id.add_note_text);
        addNote = findViewById(R.id.add_note_button);

        // add note button on click
        addNote.setOnClickListener(view -> addNote());

    }

    void addNote() {

        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        // show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // create unique id
        String id = db.child("notes").push().getKey();

        // create note
        NoteDataModel note = new NoteDataModel(id, noteText.getText().toString());

        // add note to firebase database
        assert id != null;
        db.child("notes").child(id).setValue(note).addOnCompleteListener(task -> {

            // hide progress bar
            progressBar.setVisibility(View.GONE);

            // succeed
            if (task.isSuccessful()) {

                // clear edit text
                noteText.setText("");

                //show succeed toast to user
                Toast.makeText(AddNoteActivity.this, "succeed", Toast.LENGTH_LONG).show();
            }

            // failed
            else {
                Toast.makeText(AddNoteActivity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}