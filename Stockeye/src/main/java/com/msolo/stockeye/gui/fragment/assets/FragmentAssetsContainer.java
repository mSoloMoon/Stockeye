package com.msolo.stockeye.gui.fragment.assets;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msolo.stockeye.R;
import com.msolo.stockeye.gui.fragment.publ.FragmentViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/10/27.
 */
public class FragmentAssetsContainer extends Fragment {

    private View mContainer;

    private ViewPager assetViewPager;

    public FragmentAssetsContainer() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContainer = inflater.inflate(R.layout.fragment_asset_container, container, false);

        ArrayList<View> viewList = new ArrayList<View>();

        assetViewPager = (ViewPager) mContainer.findViewById(R.id.viewpager);
        assetViewPager.setAdapter(new FragmentViewPagerAdapter(viewList));
        assetViewPager.setCurrentItem(0);

        return mContainer;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
