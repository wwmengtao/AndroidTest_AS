package com.example.testmodule.ui.swipemenu;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testmodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2018/2/7.
 */

public class SwipeMenuRightFragmentMain extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int LAYOUT_FRAGMENT_TYPE1 = 0;
    public static final int LAYOUT_FRAGMENT_TYPE2 = 1;
    public static final int LAYOUT_FRAGMENT_TYPE3 = 2;

    private Context mContext = null;

    private String mParam1;
    private String mParam2;


    public static SwipeMenuRightFragmentMain newInstance(String param1, String param2) {
        SwipeMenuRightFragmentMain fragment = new SwipeMenuRightFragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        int type = LAYOUT_FRAGMENT_TYPE1;
        switch (type){
            case LAYOUT_FRAGMENT_TYPE1:
                view = inflater.inflate(R.layout.fragment_swipe_menu_right_main, container, false);
                break;
            case LAYOUT_FRAGMENT_TYPE2:
                view = inflater.inflate(R.layout.fragment_swipe_menu_right_main2, container, false);
                break;
            case LAYOUT_FRAGMENT_TYPE3:
                view = inflater.inflate(R.layout.fragment_swipe_menu_right_main3, container, false);
                initTabHostViewPager(view);
                break;
        }
        return view;
    }

    private void initTabHostViewPager(View view){
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new SwipeMenuRightSubFragment(), "Suggestions");
        adapter.addFragment(new SwipeMenuRightSubFragment(), "Feature");
        viewPager.setAdapter(adapter);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
