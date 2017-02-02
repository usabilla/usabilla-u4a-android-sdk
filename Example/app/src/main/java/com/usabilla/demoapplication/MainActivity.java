package com.usabilla.demoapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.usabilla.demoapplication.Fragments.HomeFragment;
import com.usabilla.sdk.ubform.UBFormClient;
import com.usabilla.sdk.ubform.UBFormInterface;
import com.usabilla.sdk.ubform.controllers.Form;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UBFormInterface {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


        setUpBroadcastReceivers();
    }

    @Override
    public void formLoadedSuccessfully(Form form, boolean b) {
        if (fragments.size() > 1) {
            fragments.remove(1);
        }
        fragments.add(form);
        mPagerAdapter.notifyDataSetChanged();

    }

    private void setUpBroadcastReceivers() {
        BroadcastReceiver mCloser, mPlayStore;

        mCloser = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mViewPager.setCurrentItem(0);
                if (fragments.size() > 1) fragments.remove(1);
                mPagerAdapter.notifyDataSetChanged();
            }
        };

        mPlayStore = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "I should redirect to the play store now", Toast.LENGTH_SHORT).show();
            }
        };
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mPlayStore, new IntentFilter("com.usabilla.redirectToPlayStore"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mCloser, new IntentFilter("com.usabilla.closeForm"));
    }

    @Override
    public void formFailedLoading(Form form) {
        //This returns a default form
    }

    @Override
    public void textForMainButtonUpdated(String s) {
        //Irrelevant in this case
    }

    public void launchNewActivity() {
        startActivity(new Intent(getApplicationContext(), FormInActivty.class));
    }


    public void showLateralForm() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UBFormClient.loadFeedbackForm("58930de09172337051e8d3cc", getApplicationContext(), MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }
}
