package com.note.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note.app.R;
import com.note.app.activities.NoteViewActivity;
import com.note.app.models.NoteDataModel;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private final Context context;
    private final List<NoteDataModel> list;

    public NotesAdapter(Context context, List<NoteDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        holder.textView.setText(this.list.get(position).body);
        holder.parent.setOnClickListener(view ->
                NotesAdapter.this.context.startActivity(new Intent(NotesAdapter.this.context, NoteViewActivity.class)
                .putExtra("id", NotesAdapter.this.list.get(position).id)
                .putExtra("body", NotesAdapter.this.list.get(position).body)
                ));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout parent;
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            this.parent = view.findViewById(R.id.item_card);
            this.textView = view.findViewById(R.id.item_body);
        }
    }
}
