package com.sourcey.materiallogindemo.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sourcey.materiallogindemo.R;

import java.util.ArrayList;
import java.util.List;

public class DialogImageAdapter extends RecyclerView.Adapter<DialogImageAdapter.ViewHolder>{
    private List<String>imgList;
    private Context _ctx;
    private Dialog dialog;
    public DialogImageAdapter(List<String>imgList, Context _ctx, Dialog dialog){
        this._ctx=_ctx;
        this.imgList=imgList;
        this.dialog=dialog;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.img_dialog_item, viewGroup,          false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogImageAdapter.ViewHolder viewHolder, int i) {
        Glide.with(_ctx).load(imgList.get(i)).into(viewHolder.img);
        viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        ImageView img_close;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            img_close=itemView.findViewById(R.id.img_close);
        }
    }
}
