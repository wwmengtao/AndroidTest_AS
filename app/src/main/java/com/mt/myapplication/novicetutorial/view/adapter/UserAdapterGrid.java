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
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UsersAdapter：用于主界面的GridView。
 */
public class UserAdapterGrid extends RecyclerView.Adapter<UserAdapterGrid.UserViewHolder> {
  private static final String TAG = "UserAdapterGrid";
  protected Context mContext;
  protected Resources mResources;
  protected List<UserModelNT> usersCollection;
  protected final LayoutInflater layoutInflater;
  //以下定义当前视图的前后选中序号
  private int previousIndex = -1;//之前选中条目的序号
  private int currentIndex = -1;//当前选中条目的序号
  //以下定义当前视图的下级视图的前后选中序号
  private int previousSubLevelIndex = -1;
  private int currentSubLevelIndex = -1;
  //以下计算最优显示高度
  private int rootViewHeight = -1;
  private int spanCount = -1;
  private int optimalViewHeight = -1;
  private int dividerItemHeight = 0;
  /**
   * setRootViewHeight：设置RecyclerView视图的高度
   * @param height
   */
  public void setRootViewHeight(int height){
    rootViewHeight = height;
  }

  /**
   * setLayoutManagerSpanCount：设置Grid类型的RecyclerView数据列数
   * @param spanCount
   */
  public void setLayoutManagerSpanCount(int spanCount){
    this.spanCount = spanCount;
  }

  /**
   * setDividerItemDecorationHeight：设置分隔符宽度
   * @param dividerItemHeight
   */
  public void setDividerItemDecorationHeight(int dividerItemHeight){
    this.dividerItemHeight = dividerItemHeight;
  }

  protected OnAdapterClickListener mOnAdapterClickListener;

  public interface OnAdapterClickListener {
    void onUserAdapterItemClicked(UserModelNT userModel);//说明点击事件来自于Adapter
  }

  public void setCurrentIndex(int currentIndex){
//    ALog.Log1(TAG+"setCurrentIndex: "+currentIndex);
    this.previousIndex = this.currentIndex;
    this.currentIndex = currentIndex;
  }

  public int getCurrentIndex(){
    return currentIndex;
  }

  /**
   * notifyCertainDataChanged：更新界面上有变化的数据
   * 出于性能方面的考虑，只更新有变化的两个item
   */
  public boolean notifyCertainDataChanged(){
    boolean dataChanged = false;
    if(previousIndex != currentIndex){
      if(previousIndex > -1)notifyItemChanged(previousIndex);
      if(currentIndex > -1)notifyItemChanged(currentIndex);
      dataChanged = true;
    }
    return dataChanged;
  }

  public void setCurrentSubLevelIndex(int currentSubLevelIndex){
//    ALog.Log1(TAG+"setCurrentSubLevelIndex: "+currentIndex);
    this.previousSubLevelIndex = this.currentSubLevelIndex;
    this.currentSubLevelIndex = currentSubLevelIndex;
  }

  /**
   * notifyCertainSubLevelDataChanged：更新界面上有变化的数据，此时的数据变化为主界面条目的序号变化，该序号即为下级视图
   * 中用户最后一次浏览条目的序号。
   * 出于性能方面的考虑，只更新有变化的两个item
   */
  public void notifyCertainSubLevelDataChanged(){
    if(previousSubLevelIndex != currentSubLevelIndex){
      if(currentSubLevelIndex > -1){
        //如果下级视图中用户浏览序号有变化那么更新这个位置数据的序号
        usersCollection.get(currentIndex).setIndex(currentSubLevelIndex);
        notifyItemChanged(currentIndex);
      }
    }
  }

  @Inject
  UserAdapterGrid(Context context) {
    this.mContext = context;
    mResources = context.getResources();
    this.layoutInflater =
            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.usersCollection = Collections.emptyList();
  }

  @Override
  public int getItemCount() {
    return (this.usersCollection != null) ? this.usersCollection.size() : 0;
  }

  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.item_novice_grid, parent, false);
    setViewOptimalHeight(view);
    return new UserViewHolder(view);
  }

  /**
   * setViewOptimalHeight：根据RecyclerView以及分隔符还有view的固有高度确定最优显示行数。
   * 注意：分隔符高度dividerItemHeight没参与计算的原因是分隔符占用的就是RecyclerView中基本item的空间，
   * 不会另外占用高度空间
   * @param view
   */
  protected void setViewOptimalHeight(View view){
    if(rootViewHeight <=0 || spanCount <= 0)return;
    ViewGroup.LayoutParams mViewParams = view.getLayoutParams();
    if(optimalViewHeight <= 0){
      //以下根据屏幕大小及数据占据的行数来设置主界面上Grid的行数
      int dataRows = usersCollection.size() / spanCount;//首先计算所有数据应该显示的行数
      if((usersCollection.size() % spanCount) > 0)dataRows++;
      int finalRows = -1;//最终显示的最优行数
      int itemHeight = mViewParams.height;//首先获取基本item和分割线的高度数值
      for(int i = 1; i < dataRows; i++){//dividerItemHeight
        if(itemHeight*i < rootViewHeight && itemHeight*(i+1) >= rootViewHeight){//如果显示不下i+1个但是可以显示i个
          if(itemHeight*(i+0.5) <= rootViewHeight){
            finalRows = i+1;
          }else{
            finalRows = i;
          }
          break;
        }//end if
      }//end for
      if(-1 == finalRows){
        if(itemHeight >= rootViewHeight){//当前视图高度太大
          optimalViewHeight = rootViewHeight;
        }else{//当前视图高度太小
          optimalViewHeight = mViewParams.height;//仍然使用自身的高度数值
        }
      }else{
        int remainder = rootViewHeight % finalRows;
        optimalViewHeight = rootViewHeight / finalRows + ((remainder > 0)? 1:0);
      }
      ALog.Log(TAG+"_setViewOptimalHeight_rootViewHeight: "+rootViewHeight +
              " dividerItemHeight: "+dividerItemHeight+" optimalViewHeight:"+optimalViewHeight +
              " dataRows: "+dataRows+" finalRows: "+finalRows);
    }
    //设置最终的最优化高度
    if(optimalViewHeight > 0){
      mViewParams.height = optimalViewHeight;
      view.setLayoutParams(mViewParams);
    }
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, final int position) {
    final UserModelNT mUserModelNT = usersCollection.get(position);
    holder.mTextView.setText(getString(mUserModelNT.getAdjunction())+"\n"+
            (mUserModelNT.getIndex()+1)+"/"+mUserModelNT.getSum());
    Glide.with(mContext)
            .load("file:///android_asset/"+mUserModelNT.getPic())//加载Asset文件夹下的图片资源
            .into(holder.mImageView);
//    ALog.Log(TAG+"_onBindViewHolder: "+mUserModelNT.getPic());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (UserAdapterGrid.this.mOnAdapterClickListener != null) {
          UserAdapterGrid.this.setCurrentIndex(position);
          //记录下主界面用户当前点击的item的序号，该序号不同于mUserModelNT.getIndex()，后者记录的是二级菜单之前选中的item序号
          UserAdapterGrid.this.setCurrentSubLevelIndex(mUserModelNT.getIndex());
          UserAdapterGrid.this.mOnAdapterClickListener.onUserAdapterItemClicked(mUserModelNT);
//          ALog.Log(TAG+"_onClick: "+mUserModelNT.getKey()+" "+mUserModelNT.getIndex());
//          ALog.visitCollection2(TAG, usersCollection);
        }
      }
    });
  }

  /**
   * getString：根据字符串名称strName获取字符串内容
   * @param strName
   * @return
   */
  protected String getString(String strName){
    int strID = mResources.getIdentifier(strName, "string" ,mContext.getPackageName());
    return mResources.getString(strID);
  }

  /**
   * getColorId：根据颜色ID，即R.color.xxx获取控件setTextColor可以使用的颜色资源。
   * @param id
   * @return
   */
  protected int getColorId(int id){
    int colorID = mResources.getColor(id);
    return colorID;
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

  public void clearData(){
    if(null != usersCollection && usersCollection.size() > 0) {
      usersCollection.clear();
    }
  }

  public void setOnItemClickListener (OnAdapterClickListener mOnAdapterClickListener) {
    this.mOnAdapterClickListener = mOnAdapterClickListener;
  }

  private void validateUsersCollection(Collection<UserModelNT> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {
    private View mView = null;
    @BindView(R.id.item_grid_image)
    @Nullable
    ImageView mImageView;
    @BindView(R.id.item_grid_tv)
    TextView mTextView;
    UserViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
      ButterKnife.bind(this, itemView);
    }
    public void setBackGround(boolean itemSelected){
      mView.setBackgroundColor(mView.getResources().getColor(itemSelected ? R.color.lightskyblue
              : android.R.color.transparent));
    }
  }
}
