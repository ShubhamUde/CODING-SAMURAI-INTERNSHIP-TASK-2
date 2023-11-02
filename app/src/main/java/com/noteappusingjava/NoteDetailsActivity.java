package com.noteappusingjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageView saveNoteBtn;
    TextView pageTitleText;
    Button deleteNoteBtn;
    String title, content, docId;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.note_title_txt);
        contentEditText = findViewById(R.id.note_content_txt);
        saveNoteBtn = findViewById(R.id.note_save_btn);
        pageTitleText = findViewById(R.id.addNewNote);
        deleteNoteBtn = findViewById(R.id.deleteTextView);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("id");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode) {
            pageTitleText.setText("Edit your note");
            deleteNoteBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        deleteNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNoteFirebase();
            }
        });
    }

    private void deleteNoteFirebase() {
        DocumentReference documentReference;

        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NoteDetailsActivity.this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NoteDetailsActivity.this, "Failed to delete to note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteFirebase(note);
    }
    void saveNoteFirebase(Note note) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(NoteDetailsActivity.this, "Note Add Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NoteDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}