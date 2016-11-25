package com.example.android.picshape.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.picshape.R;

public class ConnectActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        Log.v("CONNECT ACTIVITY","Launch connect activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.connect_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            settings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This function launch Settings activity
     */
    public void settings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        if(intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    /**
     * This function setup ViewPager with Sign Fragment
     * @param viewPager
     */
    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignFragment(), "Sign");
        viewPager.setAdapter(adapter);

    }

    /**
     * This function add fragment SignIn to ViewPager and display it
     */
    public void launchSignInFragment(){

        if(viewPager.getAdapter().getCount() > 1) {{
            ((ViewPagerAdapter) viewPager.getAdapter()).removeFragment(1);
        }}

        Log.v("CONNECT ACTIVITY","Creation signIN  ");
        ((ViewPagerAdapter) viewPager.getAdapter()).addFragment(new SignInFragment(), "Sign in");

        if(viewPager.getAdapter().getCount() > 0) {
            viewPager.setCurrentItem(1);
        }

    }

    /**
     * This function add fragment SignUp to ViewPager and display it
     */
    public void launchSignUpFragment(){

        if(viewPager.getAdapter().getCount() > 1) {{
            ((ViewPagerAdapter) viewPager.getAdapter()).removeFragment(1);

        }}

        Log.v("CONNECT ACTIVITY","Creation signUPPPPP");
        ((ViewPagerAdapter) viewPager.getAdapter()).addFragment(new SignUpFragment(), "Sign up");
        if(viewPager.getAdapter().getCount() > 0) {
            viewPager.setCurrentItem(1);
        }

    }

}
