package com.fgj.lulushe.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.fgj.lulushe.R;
import com.fgj.lulushe.activity.menu.ResideMenu.ResideMenu;
import com.fgj.lulushe.activity.menu.ResideMenu.ResideMenuItem;
import com.fgj.lulushe.activity.menu.HomeFragment;
import com.fgj.lulushe.activity.menu.MessageFragment;
import com.fgj.lulushe.activity.menu.SettingsFragment;
import com.fgj.lulushe.activity.menu.VodlistFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private MainActivity mContext;
    private ResideMenuItem itemDianying,itemLuyou,itemAsia,itemEurope,itemLesbian
    ,itemIncestabuse,itemUniform,itemClassiccode,itemClassicthree,itemLatest,itemMessage;
    private ResideMenuItem itemSettings;
    private TextView titletv;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment("http://www.xixilu.com/dianying.html"),getString(R.string.menu_left_dianying));
    }

    private void setUpMenu() {

    	titletv = (TextView) findViewById(R.id.title);
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemDianying     = new ResideMenuItem(this, R.drawable.icon_home,  getString(R.string.menu_left_dianying));
        itemLuyou  = new ResideMenuItem(this, R.drawable.icon_profile,  getString(R.string.menu_left_luyou));
        itemAsia = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_asia));
        itemEurope = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_europe));
        itemLesbian     = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_lesbian));
        itemIncestabuse  = new ResideMenuItem(this, R.drawable.icon_profile,getString(R.string.menu_left_incestabuse));
        itemUniform = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_uniform));
        itemClassiccode = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_classiccode));
        itemClassicthree  = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_classicthree));
        itemLatest  = new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_latest));
        itemMessage= new ResideMenuItem(this, R.drawable.icon_profile, getString(R.string.menu_left_message));
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, getString(R.string.menu_right_settings));


        itemDianying.setOnClickListener(this);
        itemLuyou.setOnClickListener(this);
        itemAsia.setOnClickListener(this);
        itemEurope.setOnClickListener(this);
        itemLesbian.setOnClickListener(this);
        itemIncestabuse.setOnClickListener(this);
        itemUniform.setOnClickListener(this);
        itemClassiccode.setOnClickListener(this);
        itemClassicthree.setOnClickListener(this);
        itemLatest.setOnClickListener(this);
        itemMessage.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemDianying, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLuyou, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAsia, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemEurope, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLesbian, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemIncestabuse, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemUniform, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemClassiccode, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemClassicthree, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLatest, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMessage, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemDianying){
            changeFragment(new HomeFragment("http://www.xixilu.com/dianying.html"),  getString(R.string.menu_left_dianying));
        }else if (view == itemLuyou){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/2_1.html"),  getString(R.string.menu_left_luyou));
        }else if (view == itemAsia){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/1_1.html"), getString(R.string.menu_left_asia));
        }else if (view == itemEurope){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/3_1.html"), getString(R.string.menu_left_europe));
        }else if (view == itemLesbian){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/10_1.html"), getString(R.string.menu_left_lesbian));
        }else if (view == itemIncestabuse){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/5_1.html"), getString(R.string.menu_left_incestabuse));
        }else if (view == itemUniform){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/6_1.html"), getString(R.string.menu_left_uniform));
        }else if (view == itemClassiccode){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/9_1.html"), getString(R.string.menu_left_classiccode));
        }else if (view == itemClassicthree){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/7_1.html"), getString(R.string.menu_left_classicthree));
        }else if (view == itemLatest){
            changeFragment(new VodlistFragment("http://www.xixilu.us/vodlist/11_1.html"), getString(R.string.menu_left_latest));
        }else if (view == itemMessage){
            changeFragment(new MessageFragment("http://home.xixilu.org/gb-.html"), getString(R.string.menu_left_message));
        }else if (view == itemSettings){
            changeFragment(new SettingsFragment(), getString(R.string.menu_right_settings));
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment,String title){
    	titletv.setText(title);
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
}
