package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourcey.materiallogindemo.ChatAct;
import com.sourcey.materiallogindemo.Model.User;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {

    private Context _ctx;
    private List<User>mList;

    public UserAdapter(Context _ctx, List<User> mList) {
        this._ctx = _ctx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.user_item,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final User user=mList.get(position);
        holder.usernameText.setText(user.getName()+"( "+user.getType()+" )");
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.sent_id=user.getUserID();
                Intent intent=new Intent(_ctx,ChatAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView usernameText;
        CircleImageView profile;
        RelativeLayout rl;
       public viewholder(View itemView) {
           super(itemView);
           usernameText=itemView.findViewById(R.id.usernameText);
           profile=itemView.findViewById(R.id.profile);
           rl=itemView.findViewById(R.id.rl);
       }
   }
}
