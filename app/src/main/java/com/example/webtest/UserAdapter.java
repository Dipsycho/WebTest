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
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by Darkness on 2017/5/24.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<HelpEachOther> mhelpEachOtherList;
    private String objectID;
    Context mcontext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View userView;
        private TextView detailContentUser;
        private TextView detailLocationUser;
        private TextView detailUser;
        private TextView detailDateUser;
        private Button talkTo;
        private Button helpFinish;

        public ViewHolder(View view) {
            super(view);
            userView = view;
            talkTo = (Button) view.findViewById(R.id.talkToUser);
            helpFinish = (Button) view.findViewById(R.id.helpFinish);
            detailContentUser = (TextView) view.findViewById(R.id.detailContentUser);
            detailLocationUser = (TextView) view.findViewById(R.id.detailLocationUser);
            detailUser = (TextView) view.findViewById(R.id.detailUser);
            detailDateUser = (TextView) view.findViewById(R.id.detailDateUser);

        }
    }

    public UserAdapter(Context context, List<HelpEachOther> helpEachOtherList) {
        mhelpEachOtherList = helpEachOtherList;
        mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                HelpEachOther helpEachOther = mhelpEachOtherList.get(position);
                Intent intent = new Intent(mcontext, DetailPage.class);
                intent.putExtra("OBJECT_ID_NO", helpEachOther.getObjectIDnumber());
                mcontext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HelpEachOther helpEachOther = mhelpEachOtherList.get(position);

        holder.detailContentUser.setText("内容：" + helpEachOther.getContent());

        holder.detailLocationUser.setText("位置：" + helpEachOther.getLocation());

        holder.detailDateUser.setText("发布时间：" + helpEachOther.getCreatedAt());

        switch (helpEachOther.getTalkUser()) {
            case 1:
                if (helpEachOther.getHistory() == 1) {
                    holder.detailUser.setText("接受人：" + helpEachOther.getAcceptUser().getUsername());
                } else {
                    holder.detailUser.setText("接受人：NULL");
                    holder.helpFinish.setText("删除");
                }
                break;
            case 2:
                holder.detailUser.setText("发布人：" + helpEachOther.getReleaseUser().getUsername());
                break;
            default:
                break;
        }

        holder.talkTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpEachOther.getHistory() == 0) {
                    Toast.makeText(mcontext,"暂无接受人",Toast.LENGTH_SHORT).show();
                } else {
                    switch (helpEachOther.getTalkUser()) {
                        case 1:
                            Toast.makeText(mcontext, "正在联系接受者", Toast.LENGTH_SHORT).show();

                            AVUser avUser = new AVUser();
                            LCChatKit.getInstance().open(avUser.getCurrentUser().getUsername()
                                    , new AVIMClientCallback() {
                                        @Override
                                        public void done(AVIMClient avimClient, AVIMException e) {
                                            if (null == e) {
                                                Intent intent = new Intent(mcontext, LCIMConversationActivity.class);
                                                intent.putExtra(LCIMConstants.PEER_ID, helpEachOther.getAcceptUser().getUsername());
                                                mcontext.startActivity(intent);
                                            } else {
                                                Toast.makeText(mcontext, e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            break;
                        case 2:
                            Toast.makeText(mcontext, "正在联系发布者", Toast.LENGTH_SHORT).show();

                            avUser = new AVUser();
                            LCChatKit.getInstance().open(avUser.getCurrentUser().getUsername()
                                    , new AVIMClientCallback() {
                                        @Override
                                        public void done(AVIMClient avimClient, AVIMException e) {
                                            if (null == e) {
                                                Intent intent = new Intent(mcontext, LCIMConversationActivity.class);
                                                intent.putExtra(LCIMConstants.PEER_ID, helpEachOther.getReleaseUser().getUsername());
                                                mcontext.startActivity(intent);
                                            } else {
                                                Toast.makeText(mcontext, e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            break;
                        default:
                            break;
                    }
                }
            }
        });

        holder.helpFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpEachOther.getHistory() == 1) {
                    Toast.makeText(mcontext, "确认完成帮助", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, "已删除", Toast.LENGTH_SHORT).show();
                }
                objectID = helpEachOther.getObjectIDnumber();

                AVObject help = AVObject.createWithoutData("Help", objectID);
                help.put("history", 2);
                help.saveInBackground();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mhelpEachOtherList.size();
    }


}
