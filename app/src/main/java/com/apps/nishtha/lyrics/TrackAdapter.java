package com.apps.nishtha.lyrics;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nishtha on 20/7/17.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder>{
    Context context;
    ArrayList<Track> trackArrayList;

    public TrackAdapter(Context context, ArrayList<Track> trackArrayList) {
        this.context = context;
        this.trackArrayList = trackArrayList;
    }

    @Override
    public TrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=(LayoutInflater.from(context)).inflate(R.layout.single_track,parent,false);
        TrackHolder trackHolder=new TrackHolder(view);
        return trackHolder;
    }

    @Override
    public void onBindViewHolder(TrackHolder holder, int position) {
        final Track track=trackArrayList.get(position);
        holder.nameTv.setText("Track Name : "+track.getTrack_name());
        holder.artistTv.setText("Artist Name : "+track.getArtist_name());
        holder.trackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(track.getTrack_share_url()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackArrayList.size();
    }

    class TrackHolder extends RecyclerView.ViewHolder{
        TextView nameTv,artistTv;
        CardView trackCardView;
        public TrackHolder(View itemView) {
            super(itemView);
            nameTv= (TextView) itemView.findViewById(R.id.nameTv);
            artistTv= (TextView) itemView.findViewById(R.id.artistTv);
            trackCardView= (CardView) itemView.findViewById(R.id.trackCardView);
        }
    }
}
