package com.example.notes.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Models.Notes;
import com.example.notes.NotesClickListener;
import com.example.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder > {

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list , parent, false));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_title.setSelected(true);

        holder.tv_notes.setText(list.get(position).getNotes());
        holder.tv_notes.setSelected(true);

        holder.tv_date.setText(list.get(position).getDate());
        holder.tv_date.setSelected(true);

        if(list.get(position).isPinned()){
            holder.imv_pin.setImageResource(R.drawable.ic_pin);
        }else{
            holder.imv_pin.setImageResource(0);

        }

        int color_code = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });

    }


    private int getRandomColor(){
        List<Integer> clr = new ArrayList<>();
        clr.add(R.color.clr1);
        clr.add(R.color.clr2);
        clr.add(R.color.clr3);
        clr.add(R.color.clr4);
        clr.add(R.color.clr5);
        clr.add(R.color.clr6);

        Random random = new Random();
        int random_color = random.nextInt(clr.size());
        return clr.get(random_color);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterListe(List<Notes> listeFiltrer){
        list = listeFiltrer;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView tv_title, tv_notes, tv_date;
    ImageView imv_pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_notes = itemView.findViewById(R.id.tv_notes);
        tv_date = itemView.findViewById(R.id.tv_date);
        imv_pin = itemView.findViewById(R.id.imv_pin);
    }
}