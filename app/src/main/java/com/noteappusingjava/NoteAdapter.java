package com.noteappusingjava;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import org.checkerframework.checker.nullness.qual.NonNull;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTxt.setText(note.title);
        holder.contentText.setText(note.content);
        holder.timeText.setText(Utility.timetampString(note.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteDetailsActivity.class);
                intent.putExtra("title", note.title);
                intent.putExtra("content", note.content);
                String docId = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("id", docId);
                context.startActivity(intent);
            }
        });
    }

    @androidx.annotation.NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleTxt, contentText, timeText;
        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleTxt = itemView.findViewById(R.id.note_titleText);
            contentText = itemView.findViewById(R.id.note_contentText);
            timeText = itemView.findViewById(R.id.note_timeText);
        }
    }
}
