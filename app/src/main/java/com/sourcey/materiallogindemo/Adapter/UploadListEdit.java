package com.sourcey.materiallogindemo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sourcey.materiallogindemo.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

public class UploadListEdit extends RecyclerView.Adapter<UploadListEdit.ViewHolder> {
    private List<String> uriList;
    private Context _ctx;
    public UploadListEdit() {
    }

    public UploadListEdit( List<String> uriList, Context _ctx) {
        this.uriList = uriList;
        this._ctx=_ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item_list,viewGroup,false);
        return new UploadListEdit.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(_ctx).load(uriList.get(i)).into(viewHolder.eventImage);
        viewHolder.fileNameView.setText(_ctx.getString(R.string.uploaded));
        viewHolder.eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_ctx);
                AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations=R.style.PauseDialogAnimation;
                View dialogLayout = LayoutInflater.from(_ctx).inflate(R.layout.go_pro_dialog_layout, null);
                ImageView dialogImage=dialogLayout.findViewById(R.id.goProDialogImage);
                Glide.with(_ctx).load(uriList.get(i)).into(dialogImage);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();

            }
        });

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriList.remove(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
         TextView fileNameView;
         ImageView fileDoneView;
         ImageView eventImage;
         ImageView remove;
         ViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
            fileNameView=itemView.findViewById(R.id.fileName);
            fileDoneView=itemView.findViewById(R.id.uplaodCheck);
            eventImage=itemView.findViewById(R.id.eventImage);
            remove=itemView.findViewById(R.id.remove);
        }
    }
}
