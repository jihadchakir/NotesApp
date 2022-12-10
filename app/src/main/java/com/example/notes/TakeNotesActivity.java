package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notes.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TakeNotesActivity extends AppCompatActivity {

    EditText et_title, et_notes;
    ImageView imv_save;
    Notes notes;
    boolean b = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_notes);

        imv_save = findViewById(R.id.imv_save);
        et_title = findViewById(R.id.et_title);
        et_notes = findViewById(R.id.et_notes);
        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            et_title.setText(notes.getTitle());
            et_notes.setText(notes.getNotes());
            b = true; // c'est une ancienne note

        }catch(Exception e){
            e.printStackTrace();
        }

        imv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String textnotes = et_notes.getText().toString();

                if(textnotes.isEmpty()){
                    Toast.makeText(TakeNotesActivity.this, "Votre note est vide", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat form = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                Date date = new Date();
                if(!b){
                    notes = new Notes();

                }

                notes.setTitle(title);
                notes.setNotes(textnotes);
                notes.setDate(form.format(date));

                Intent intent = new Intent();
                intent.putExtra("note",notes); // il faut mettre la class Notes Serializable
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
    }
}