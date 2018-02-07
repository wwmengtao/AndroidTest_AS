package com.example.protoui.swipemenu.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.protoui.R;
import com.example.protoui.swipemenu.data.RVData;

import java.util.List;

/**
 * Created by Mengtao1 on 2018/02/07.
 */

public class SwipeMenuLeftAdapter extends RecyclerView.Adapter<SwipeMenuLeftHolder>{
    public static final int VIEW_TYPE_DIVIDER = 0x001;
    public static final int VIEW_TYPE_TITLE = 0x002;
    public static final int VIEW_TYPE_NORMAL = 0x003;
    //
    private Context mContext = null;
    private List<RVData> mData = null;
    private OnItemViewClickListener mOnItemClickListener = null;

    public SwipeMenuLeftAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(List<RVData> mData){
        this.mData = mData;
    }

    @Override
    public SwipeMenuLeftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_swipe_menu_left_rv, parent, false);
        SwipeMenuLeftHolder holder = new SwipeMenuLeftHolder(mView, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(SwipeMenuLeftHolder holder, final int position) {
        RVData data = mData.get(position);
        holder.bindData(data);
        if(VIEW_TYPE_NORMAL != holder.getViewType())return;
        View rootView = holder.getRootView();
        if(null == mOnItemClickListener){
            throw new RuntimeException("must provide not null OnItemClickListener!");
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemViewClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        RVData data = mData.get(position);
        if(RVData.NO_PIC_ID == data.getPic()){
            viewType = (RVData.DIVIDER_RV.equals(data.getTitle())) ? VIEW_TYPE_DIVIDER : VIEW_TYPE_TITLE;
        }else{
            viewType = VIEW_TYPE_NORMAL;
        }
        return viewType;
    }

    public void setOnItemViewClickListener(OnItemViewClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface OnItemViewClickListener{
        void onItemViewClick(int position);
    }
}
