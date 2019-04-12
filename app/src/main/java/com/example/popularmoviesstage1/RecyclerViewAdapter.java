package com.example.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MovieProperties> list;

    RecyclerViewAdapter(Context context, ArrayList<MovieProperties> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.recycler_image, viewGroup, false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder myViewHolder, final int i) {
        myViewHolder.imageView.setImageBitmap(list.get(i).getBitmap());

        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MovieDetailsActivity.class)
                        .putExtra("bitmap", list.get(i).getBitmap())
                        .putExtra("rating", list.get(i).getRating())
                        .putExtra("release_date", list.get(i).getReleaseDate())
                        .putExtra("description", list.get(i).getDescription())
                        .putExtra("title", list.get(i).getTitle())
                        .putExtra("id", list.get(i).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
