package com.example.aplikasidaftarbatik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class BatikUAdapter extends RecyclerView.Adapter<BatikUAdapter.myViewHolder> {
    List<BatikU> dataBatik;
    Context context;

    public BatikUAdapter(List<BatikU> dataBatik, Context context) {
        this.dataBatik = dataBatik;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_home, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        BatikU currentBatik = dataBatik.get(position);
        holder.bindTo(currentBatik);

    }

    @Override
    public int getItemCount() {
        return dataBatik.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView myNamaBatik, myDaerahBatik;
        ImageView myImage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            myNamaBatik = itemView.findViewById(R.id.namaBatik);
            myDaerahBatik= itemView.findViewById(R.id.daerahBatik);
            myImage = itemView.findViewById(R.id.gambarBatik);
        }

        public void bindTo(BatikU currentBatik) {
            myNamaBatik.setText(currentBatik.getNamaBatik());
            myDaerahBatik.setText(currentBatik.getDaerahBatik());

            Glide.with(context)
                    .load(currentBatik.getLinkBatik())
                    .placeholder(R.drawable.ic_action_eror)
                    .centerCrop()
                    .into(myImage);
        }
    }
}