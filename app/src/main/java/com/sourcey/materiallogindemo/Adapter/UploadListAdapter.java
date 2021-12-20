package com.sourcey.materiallogindemo.Adapter;

import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourcey.materiallogindemo.R;

import java.util.List;
public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<Uri> uriList;

    public UploadListAdapter() {
    }

    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList, List<Uri> uriList) {
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
        this.uriList=uriList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final String fileName=fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);
        String fileDont= fileDoneList.get(i);
        if(fileDont.equals("progress")){
            viewHolder.fileDoneView.setImageResource(R.drawable.ic_upload_gray);
        }
       else if(fileDont.equals("uploading")){
            viewHolder.fileDoneView.setImageResource(R.drawable.ic_upload_progress);
        }else {
            viewHolder.fileDoneView.setImageResource(R.drawable.ic_upload_green);
        }
        viewHolder.eventImage.setImageURI(uriList.get(i));
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriList.remove(i);
                fileDoneList.remove(i);
                fileNameList.remove(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
        public TextView fileNameView;
        public ImageView fileDoneView;
        public ImageView eventImage;
        public ImageView remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
            fileNameView=itemView.findViewById(R.id.fileName);
            fileDoneView=itemView.findViewById(R.id.uplaodCheck);
            eventImage=itemView.findViewById(R.id.eventImage);
            remove=itemView.findViewById(R.id.remove);
        }
    }
}
