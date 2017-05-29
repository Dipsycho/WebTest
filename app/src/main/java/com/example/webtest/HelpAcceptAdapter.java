package com.example.webtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by Darkness on 2017/5/22.
 */

public class HelpAcceptAdapter extends RecyclerView.Adapter<HelpAcceptAdapter.ViewHolder> {
    private List<HelpEachOther> mhelpEachOtherList;
    private String objectID;
    Context mcontext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View acceptView;
        private TextView detailContent;
        private TextView detailRemark;
        private TextView detailLocation;
        private TextView detailDate;
        private TextView detailRelease;
        private TextView detailID;
        private Button button;

        public ViewHolder(View view) {
            super(view);
            acceptView=view;
            button = (Button) view.findViewById(R.id.acceptHelp);
            detailContent = (TextView) view.findViewById(R.id.detailContent);
            detailRemark = (TextView) view.findViewById(R.id.detailRemark);
            detailLocation = (TextView) view.findViewById(R.id.detailLocation);
            detailDate = (TextView) view.findViewById(R.id.detailDate);
            detailRelease = (TextView) view.findViewById(R.id.detailRelease);
            detailID = (TextView) view.findViewById(R.id.detailID);
        }
    }


    public HelpAcceptAdapter(Context context,List<HelpEachOther> helpEachOtherList) {
        mhelpEachOtherList = helpEachOtherList;
        mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accept_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.acceptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                HelpEachOther helpEachOther=mhelpEachOtherList.get(position);
                Intent intent=new Intent(mcontext,DetailPage.class);
                intent.putExtra("OBJECT_ID_NO", helpEachOther.getObjectIDnumber());
                mcontext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HelpEachOther helpEachOther = mhelpEachOtherList.get(position);

        holder.detailContent.setText("内容：" + helpEachOther.getContent());
        holder.detailRemark.setText("备注：" + helpEachOther.getRemark());
        holder.detailLocation.setText("位置：" + helpEachOther.getLocation());
        holder.detailRelease.setText("发布人：" + helpEachOther.getReleaseUser().getUsername());
        holder.detailDate.setText("发布时间：" + helpEachOther.getCreatedAt());
        holder.detailID.setText("编号：" + helpEachOther.getObjectIDnumber());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objectID = helpEachOther.getObjectIDnumber();

                Toast.makeText(MyLeanCloudApp.getContext(), "接受请求", Toast.LENGTH_LONG)
                        .show();

                AVObject help=AVObject.createWithoutData("Help",objectID);
                AVUser avUser=new AVUser();
                help.put("accept", avUser.getCurrentUser());
                help.put("history",1);
                help.saveInBackground();



            }
        });

    }

    @Override
    public int getItemCount() {
        return mhelpEachOtherList.size();
    }
}
