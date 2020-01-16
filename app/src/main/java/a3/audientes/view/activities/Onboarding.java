package a3.audientes.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import a3.audientes.R;
import a3.audientes.view.adapter.OnboardingSliderAdapter;
import a3.audientes.utils.SharedPrefUtil;

import static java.lang.Thread.sleep;

public class Onboarding extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private OnboardingSliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mNextBtn, mSkipBtn;
    private int mCurrentPage;
    private final int NUM_OF_DOTS = 5;
    private boolean newVisitor;
    private int currentAudiogramId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        mNextBtn = findViewById(R.id.nextbtn);
        mSkipBtn = findViewById(R.id.skipbtn);

        mNextBtn.setOnClickListener(this);
        mSkipBtn.setOnClickListener(this);

        sliderAdapter = new OnboardingSliderAdapter(getBaseContext());
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);
        newVisitor = Boolean.valueOf(SharedPrefUtil.readSharedSetting(this, getString(R.string.new_visitor_pref), "true"));

    }


    public void addDotsIndicator(int position){
        mDots = new TextView[NUM_OF_DOTS];
        mDotLayout.removeAllViews();

        for (int i = 0; i < NUM_OF_DOTS; i++){
            mDots[i] = new TextView(getBaseContext());
            mDots[i].setText(Html.fromHtml("&#8226;"));


            mDots[i].setTextSize(50);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.darkerOrange));
        }
    }


    @Override
    public void onClick(View v) {
        boolean lastPage = mCurrentPage == mDots.length-1;

        if (v == mNextBtn){
            if (lastPage){
                launchAcitivity();
            }
            else {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        }
        else if (v == mSkipBtn){
            launchAcitivity();
        }
    }

    private void launchAcitivity(){
        Intent intent;
        currentAudiogramId = Integer.parseInt(SharedPrefUtil.readSharedSetting(getBaseContext(), "currentAudiogram", "0"));
        if (!isHearableConnected())
            intent = new Intent(getBaseContext(), BluetoothPairing.class);
        else if (newVisitor || currentAudiogramId == 0){
            SharedPrefUtil.saveSharedSetting(Objects.requireNonNull(this), getString(R.string.new_visitor_pref), "false");
            intent = new Intent(this, HearingTest.class);
        }
        else{
            SharedPrefUtil.saveSharedSetting(Objects.requireNonNull(this), getString(R.string.new_visitor_pref), "false");
            intent = new Intent(this, HearingProfile.class);
        }

        startActivity(intent);
        Objects.requireNonNull(this).finish();
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int i) {
            boolean firstPage = i == 0, lastPage = i == mDots.length-1;

            addDotsIndicator(i);
            mCurrentPage = i;

            if (firstPage){
                mNextBtn.setEnabled(true);
                mNextBtn.setText(R.string.next);
            }
            else if (lastPage){
                mNextBtn.setEnabled(true);
                mNextBtn.setText(R.string.start);
            }
            else {
                mNextBtn.setEnabled(true);
                mNextBtn.setText(R.string.next);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    private boolean isHearableConnected() {
        // TODO: somehow check if the correct device is already connected
        return false;
    }
}