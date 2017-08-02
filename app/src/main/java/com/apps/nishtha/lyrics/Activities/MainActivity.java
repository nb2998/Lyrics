package com.apps.nishtha.lyrics.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.apps.nishtha.lyrics.Fragments.AllTracksFragment;
import com.apps.nishtha.lyrics.Fragments.SearchFragment;
import com.apps.nishtha.lyrics.Fragments.SettingsFragment;
import com.apps.nishtha.lyrics.R;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity{

    public static int COUNT=3;
    public static boolean permissongranted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,"ca-app-pub-7007784112686547~1172626234");


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            setViewPager();
            permissongranted=true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        (getMenuInflater()).inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.infoMenu){
            AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Information")
                    .setMessage("By enabling the service, you'll get a notification on playing any song in the Music Player. Click on the notification to enjoy the lyrics of the song! You can disable this service anytime using the switch/toggle above.")
                    .setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{


        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Search";
            }else if(position==1){
                return "My Songs";
            } else if(position==2){
                return "Settings";
            }
            return null;
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return new SearchFragment();
            }
            else if(position==1 && permissongranted){

                return new AllTracksFragment();
            }
            else if(position ==1 && !permissongranted){
                return new AllTracksFragment();
            }
            else if(position==2){
                return new SettingsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }

    private class ViewPagerAdapterSecond extends FragmentPagerAdapter{


        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Search";
            }else if(position==1){
                return "Settings";  }
            return null;
        }

        public ViewPagerAdapterSecond(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return new SearchFragment();
            }
            else if(position==1){
                return new SettingsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "onRequestPermissionsResult: granted");
                permissongranted=true;
                setViewPager();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                permissongranted=false;
                Toast.makeText(MainActivity.this, "Sorry, permission to access your music files has been denied", Toast.LENGTH_SHORT).show();
                COUNT=2;
                ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager);
                viewPager.setAdapter(new ViewPagerAdapterSecond(getSupportFragmentManager()));
                TabLayout tabLayout= (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setViewPager(){
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

}
