package com.example.notes;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.Adapters.NotesListAdapter;
import com.example.notes.Database.RoomDB;
import com.example.notes.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB db;
    FloatingActionButton add;
    SearchView recherche;
    Notes selectionner;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        add = findViewById(R.id.add);
        recherche = findViewById(R.id.recherche);

        db = RoomDB.getInstance(this);
        notes = db.mainDAO().getAll();
         updateRecyler(notes);

         add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this, TakeNotesActivity.class);
                 startActivityForResult(intent, 101); // 101 : Request, pour ajouter une note

             }
         });

         recherche.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 filter(newText);
                 return true;
             }
         });
    }

    private void filter(String newText) {
        List<Notes> filterListe = new ArrayList<>();
        for(Notes singleNote : notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){

                filterListe.add(singleNote);
            }
        }
        notesListAdapter.filterListe(filterListe);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){

                    Notes nouv = (Notes) data.getSerializableExtra("note");
                    db.mainDAO().insert(nouv);
                    notes.clear();
                    notes.addAll(db.mainDAO().getAll());
                    notesListAdapter.notifyDataSetChanged();
            }
        }

        else if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Notes nouv = (Notes) data.getSerializableExtra("note");
                db.mainDAO().update(nouv.getID(), nouv.getTitle(), nouv.getNotes());
                notes.clear();
                notes.addAll(db.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();

            }
        }
    }

    private void updateRecyler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {

            Intent intent = new Intent(MainActivity.this, TakeNotesActivity.class);
            intent.putExtra("old_not", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectionner = new Notes();
            selectionner = notes;
            showPopup(cardView);

        }

        private void showPopup(CardView cardView) {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, cardView);
            popupMenu.setOnMenuItemClickListener(MainActivity.this);
            popupMenu.inflate(R.menu.popup);
            popupMenu.show();

        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectionner.isPinned()){
                    db.mainDAO().pin(selectionner.getID(), false);
                    Toast.makeText(MainActivity.this, "Desépingler", Toast.LENGTH_SHORT).show();


                }
                else{
                    db.mainDAO().pin(selectionner.getID(), true);
                    Toast.makeText(MainActivity.this, "Epingler", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(db.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.supp:
                db.mainDAO().delete(selectionner);
                notes.remove(selectionner);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note supprimée", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }
}