package com.faayda.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.faayda.MainActivity;
import com.faayda.R;
import com.faayda.adapter.DashBoardPagerAdapter;
import com.faayda.customviews.SlidingTabLayout;
import com.faayda.utils.CommonUtils;
import com.faayda.utils.Constants;

/**
 * Created by Aashutosh @ vinove on 7/31/2015.
 */
public final class DashBoardFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private LinearLayout llButtonBar;
    private ImageView addTransaction;
    private Button btRating, btShare;
    private SlidingTabLayout slidingTabStrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.dashboard, container, false);
        ((MainActivity) getActivity()).saveButton.setVisibility(View.GONE);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        btRating = (Button) layoutView.findViewById(R.id.btRating);
        btShare = (Button) layoutView.findViewById(R.id.btShare);
        llButtonBar = (LinearLayout) layoutView.findViewById(R.id.llButtonBar);
        viewPager = (ViewPager) layoutView.findViewById(R.id.vpDashboard);
        addTransaction = (ImageView) layoutView.findViewById(R.id.iv_add_transaction);
        slidingTabStrip = (SlidingTabLayout) layoutView.findViewById(R.id.pagerSlidingTabs);


        DashBoardPagerAdapter pagerAdapter = new DashBoardPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        addTransaction.setOnClickListener(this);
        btRating.setOnClickListener(this);
        btShare.setOnClickListener(this);
        slidingTabStrip.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        return layoutView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_transaction:
                ((MainActivity) getActivity()).onFragmentAdd(new AddTransaction(), Constants.ADD_TRANSACTION);
                break;
            case R.id.btRating:
                CommonUtils.rateApplication(getActivity());
                break;
            case R.id.btShare:
                CommonUtils.shareApplication(getActivity());
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setTopBarTitle(Constants.DASHBOARD);
        ((MainActivity) getActivity()).ibBackIcon.setVisibility(View.GONE);
        ((MainActivity) getActivity()).ibNotificationIcon.setVisibility(View.GONE);
//        ((MainActivity) getActivity()).invalidateTopBar(Constants.TITLE_BAR.NOTIFICATION_ONLY);
    }

    public void setFloatingButtonsVisibility(int visibility) {
        llButtonBar.setVisibility(visibility);
        addTransaction.setVisibility(visibility);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        setFloatingButtonsVisibility(View.VISIBLE);
    }
}
