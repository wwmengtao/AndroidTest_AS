/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.mt.myapplication.novicetutorial.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link UserModel}.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
  protected Context mContext;
  protected Resources mResources;
  protected List<UserModelNT> usersCollection;
  protected final LayoutInflater layoutInflater;

  protected OnItemClickListener onItemClickListener;

  @Inject
  UsersAdapter(Context context) {
    this.mContext = context;
    mResources = context.getResources();
    this.layoutInflater =
            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.usersCollection = Collections.emptyList();
  }
  public interface OnItemClickListener {
    void onUserAdapterItemClicked(UserModelNT userModel);
  }
  @Override
  public int getItemCount() {
    return (this.usersCollection != null) ? this.usersCollection.size() : 0;
  }

  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.item_novice_grid, parent, false);
    return new UserViewHolder(view);
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, final int position) {
    final UserModelNT mUserModelNT = usersCollection.get(position);
    holder.mTextView.setText(getString(mUserModelNT.getAdjunction()));
    Glide.with(mContext)
            .load("file:///android_asset/"+mUserModelNT.getPic())//加载Asset文件夹下的图片资源
            .into(holder.mImageView);
    ALog.Log("onBindViewHolder:"+mUserModelNT.getPic());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (UsersAdapter.this.onItemClickListener != null) {
          UsersAdapter.this.onItemClickListener.onUserAdapterItemClicked(mUserModelNT);
        }
      }
    });
  }

  protected String getString(String strName){
    int strID = mResources.getIdentifier(strName, "string" ,mContext.getPackageName());
    return mResources.getString(strID);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public void setUsersCollection(Collection<UserModelNT> usersCollection) {
    this.validateUsersCollection(usersCollection);
    this.usersCollection = (List<UserModelNT>) usersCollection;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<UserModelNT> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_grid_image)
    @Nullable
    ImageView mImageView;
    @BindView(R.id.item_grid_tv)
    TextView mTextView;
    UserViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
