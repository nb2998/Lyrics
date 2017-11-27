package com.apps.nishtha.lyrics.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.nishtha.lyrics.Model.FavModel;
import com.apps.nishtha.lyrics.R;

import java.util.ArrayList;

/**
 * Created by nishtha on 21/11/17.
 */

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavHolder> {
    Context context;
    ArrayList<FavModel> favModelArrayList;

    public FavAdapter(Context context, ArrayList<FavModel> favModelArrayList) {
        this.context = context;
        this.favModelArrayList = favModelArrayList;
    }

    @Override
    public FavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavHolder(LayoutInflater.from(context).inflate(R.layout.single_row_fav,parent,false));
    }

    @Override
    public void onBindViewHolder(final FavHolder holder, int position) {
        final boolean[] selectedOnce = {true};
        final FavModel favModel=favModelArrayList.get(position);
        holder.nameTvFav.setText(favModel.getTitle());
        holder.artistTvFav.setText("Artist");
        holder.trackCardViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 23/11/17 display and hide lyrics on clicking
                holder.lyricsTvFav.setText(favModel.getLyrics());
                if(selectedOnce[0]) {
                    holder.lyricsTvFav.setVisibility(View.VISIBLE);
                    selectedOnce[0] =false;
                }else {
                    // TODO: 23/11/17 Change height of cardview accordingly  
                    holder.lyricsTvFav.setVisibility(View.GONE);
                    selectedOnce[0] =true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favModelArrayList.size();
    }

    class FavHolder extends RecyclerView.ViewHolder{
        TextView nameTvFav,artistTvFav, lyricsTvFav;
        CardView trackCardViewFav;
        public FavHolder(View itemView) {
            super(itemView);
            nameTvFav = (TextView) itemView.findViewById(R.id.nameTvFav);
            artistTvFav = (TextView) itemView.findViewById(R.id.artistTvFav);
            trackCardViewFav = (CardView) itemView.findViewById(R.id.trackCardViewFav);
            lyricsTvFav = (TextView) itemView.findViewById(R.id.lyricsTvFav);
        }
    }
}
