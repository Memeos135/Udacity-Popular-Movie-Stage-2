package com.example.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class DetailsRecyclerAdapter extends RecyclerView.Adapter<DetailsRecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TrailerInfo> strings;
    private ArrayList<ReviewInfo> reviews;
    private String selection;

    public DetailsRecyclerAdapter(Context context, ArrayList<TrailerInfo> strings, ArrayList<ReviewInfo> reviews, String selection) {
        this.context = context;
        if(selection.equals("review")){
            this.reviews = reviews;
        }else {
            this.strings = strings;
        }
        this.selection = selection;
    }


    @NonNull
    @Override
    public DetailsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        if(selection.equals("review")){
            view = mInflater.inflate(R.layout.review_card, viewGroup, false);
        }else {
            view = mInflater.inflate(R.layout.trailer_card, viewGroup, false);
        }
        return new DetailsRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsRecyclerAdapter.MyViewHolder myViewHolder, final int i) {

        if(selection.equals("review")){
            myViewHolder.author.setText(reviews.get(i).getAuthor());
            myViewHolder.content.setText(reviews.get(i).getContent());
        }else {
            myViewHolder.name.setText(strings.get(i).getName());
            myViewHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.youtube.com/watch?v=" + strings.get(i).getKey();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(selection.equals("review")){
            return reviews.size();
        }else{
            return strings.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView play;
        TextView author;
        TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            if(selection.equals("review")){
                author = itemView.findViewById(R.id.author);
                content = itemView.findViewById(R.id.content);
            }else {
                name = itemView.findViewById(R.id.trailerName);
                play = itemView.findViewById(R.id.playImage);
            }
        }
    }
}