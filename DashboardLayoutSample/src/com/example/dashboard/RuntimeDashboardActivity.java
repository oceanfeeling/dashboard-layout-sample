package com.example.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class RuntimeDashboardActivity extends ActionBarActivity {

    ViewPager mPager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_dashboard);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ButtonAdapter(this));
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewParent parent = v.getParent();
            if(parent instanceof HomeButton) {
                Toast.makeText(RuntimeDashboardActivity.this,
                        ((HomeButton) parent).getLabel(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private static HomeButtonInfo[] sHomeButtonInfos = new HomeButtonInfo[] {
            new HomeButtonInfo(R.id.home_btn_1, R.string.homedash_button_1, R.drawable.ic_dashboard_button),
            new HomeButtonInfo(R.id.home_btn_2, R.string.homedash_button_2, R.drawable.ic_dashboard_button),
            new HomeButtonInfo(R.id.home_btn_3, R.string.homedash_button_3, R.drawable.ic_dashboard_button),
            new HomeButtonInfo(R.id.home_btn_4, R.string.homedash_button_4, R.drawable.ic_dashboard_button),
            new HomeButtonInfo(R.id.home_btn_5, R.string.homedash_button_5, R.drawable.ic_dashboard_button),
            new HomeButtonInfo(R.id.home_btn_6, R.string.homedash_button_6, R.drawable.ic_dashboard_button),
    };

    private ArrayList<HomeButton> createHomeButtons(Context context, View.OnClickListener listener) {
        ArrayList<HomeButton> buttons = new ArrayList<HomeButton>();
        for(int i = 0; i < sHomeButtonInfos.length; i++) {
            HomeButton btn = new HomeButton(context);
            btn.setId(sHomeButtonInfos[i].getId());
            btn.setButtonBackground(
                    context.getResources().getDrawable(
                            sHomeButtonInfos[i].getIcon()));
            btn.setLabel(sHomeButtonInfos[i].getText());

            buttons.add(btn);
        }

        return buttons;
    }

    private ArrayList<Button> createButtons(Context context, View.OnClickListener listener) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        for(int i = 0; i < sHomeButtonInfos.length; i++) {
            Button btn = new Button(context, null, R.attr.homeDashboardButtonStyle);
            btn.setId(sHomeButtonInfos[i].getId());
            btn.setBackgroundDrawable(context.getResources().getDrawable(
                    sHomeButtonInfos[i].getIcon()));
            btn.setText(sHomeButtonInfos[i].getText());

            buttons.add(btn);
        }

        return buttons;
    }

    private class ButtonAdapter extends PagerAdapter implements
            DashboardLayout.Callbacks {

        private Context mContext;
        private int mCount = 1;

        public ButtonAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            DashboardLayout dashboard = new DashboardLayout(mContext);
            dashboard.setPage(position);
            dashboard.setCallbacks(this);

            container.addView(dashboard, position);

            ArrayList<HomeButton> buttons = createHomeButtons(mContext, mListener);
            for(View button : buttons) {
                dashboard.addView(button);
            }

            dashboard.requestLayout();

            return dashboard;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void onLayoutComplete(int renderedIcons) {
            mCount = (int )Math.ceil((double) sHomeButtonInfos.length / (double) renderedIcons);
            notifyDataSetChanged();
        }
    }
}
