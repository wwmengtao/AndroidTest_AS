package com.mt.myapplication.novicetutorial.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import javax.inject.Inject;

/**
 * Created by mengtao1 on 2017/11/9.
 */

public class UserAdapterList extends UsersAdapter {
    @Inject
    public UserAdapterList(Context context) {
        super(context);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.item_novice_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        final UserModelNT mUserModelNT = usersCollection.get(position);
        holder.mTextView.setText(getString(mUserModelNT.getKey()));
//        ALog.Log("UserAdapterList_onBindViewHolder:"+mUserModelNT.getKey());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserAdapterList.this.onItemClickListener != null) {
                    UserAdapterList.this.onItemClickListener.onUserAdapterItemClicked(mUserModelNT);
                }
            }
        });
    }
}
