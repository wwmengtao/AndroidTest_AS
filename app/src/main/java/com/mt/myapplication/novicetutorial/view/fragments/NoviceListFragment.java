package com.mt.myapplication.novicetutorial.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.UserComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.presenter.NoviceListPresenter;
import com.mt.myapplication.novicetutorial.view.adapter.UserAdapterList;
import com.mt.myapplication.novicetutorial.view.adapter.UsersAdapterGrid;
import com.mt.myapplication.novicetutorial.view.interfaces.NoviceRecyclerView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mt.myapplication.novicetutorial.view.activities.NoviceListActivity.NOVICE_LIST_ACTIVITY_KEY;


public class NoviceListFragment extends BaseFragment implements NoviceRecyclerView {
    public Unbinder unbinder;
    Activity mActivity = null;
    @Inject Context mContext;
    @BindView(R.id.novice_grid_recyclerview) RecyclerView mRecyclerView;
    @Inject UserAdapterList usersAdapter;
    @Inject NoviceListPresenter mNoviceListPresenter;
    private OnUserClickedListener mOnUserClickedListener;
    private Intent mIntent = null;

    /**
     * Interface for listening user list events.
     */
    public interface OnUserClickedListener {
        void onUserClicked(final UserModelNT userModel);
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (activity instanceof OnUserClickedListener) {
            this.mOnUserClickedListener = (OnUserClickedListener) activity;
        }
        mIntent = mActivity.getIntent();
        UserModelNT userModel = (UserModelNT)mIntent.getParcelableExtra(NOVICE_LIST_ACTIVITY_KEY);
//        ALog.Log("NoviceListFragment_onAttach: "+userModel.toString());
        mActivity.setTitle(getString(userModel.getAdjunction()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(UserComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_novice_recview, container, false);
        unbinder = ButterKnife.bind(this, v);
        setupRecyclerView();
        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mNoviceListPresenter.setView(this);
        if (savedInstanceState == null) {
            this.loadUserList();
        }
    }

    /**
     * Loads all users.
     */
    private void loadUserList() {
        this.mNoviceListPresenter.initialize();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        unbinder.unbind();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.mNoviceListPresenter.destroy();
    }

    @Override public void onDetach() {
        super.onDetach();
        onItemClickListener = null;
        this.mOnUserClickedListener = null;
    }

    private UsersAdapterGrid.OnItemClickListener onItemClickListener =
            new UsersAdapterGrid.OnItemClickListener() {
                @Override public void onUserAdapterItemClicked(UserModelNT userModel) {
                    if (NoviceListFragment.this.mNoviceListPresenter != null && userModel != null) {
                        NoviceListFragment.this.mNoviceListPresenter.onUserClicked(userModel);
                    }
                }
            };

    private void setupRecyclerView() {
        usersAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(usersAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL |
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public Intent getViewIntent() {
        return mIntent;
    }

    @Override
    public void setUserList(Collection<UserModelNT> userModelCollection) {
        if (userModelCollection != null) {
            this.usersAdapter.setUsersCollection(userModelCollection);
        }
    }

    @Override
    public void viewUser(UserModelNT userModel) {
        if (this.mOnUserClickedListener != null) {
            this.mOnUserClickedListener.onUserClicked(userModel);
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return mContext;
    }

}
