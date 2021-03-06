package com.example.panlin_pan.myapplication.scenario_rotate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.panlin_pan.myapplication.R;


public class MyRotateActivity extends Activity {
void detachAllFragments(FragmentManager fragmentManager,FragmentTransaction fragmentTransaction){
    Fragment fragment = fragmentManager.findFragmentById(android.R.id.content);
    if(fragment!=null){
        fragmentTransaction.detach(fragment);
    }
    fragment = fragmentManager.findFragmentById(android.R.id.content);
    if(fragment!=null){
        fragmentTransaction.detach(fragment);
    }
    fragment = fragmentManager.findFragmentById(android.R.id.content);
    if(fragment!=null){
        fragmentTransaction.detach(fragment);
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_my_rotate);

        FragmentManager fragmentManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        Configuration configuration= getResources().getConfiguration();



        MyLandscapeFrag landscapeFrag = new MyLandscapeFrag();
        MyLandscapeFrag2 landscape2 =  MyLandscapeFrag2.newInstance("l2");

        Bundle b = new Bundle();
        b.putString("which", "2");
        MyPortraitFrag portrait = new MyPortraitFrag();
        portrait.setWhich("2");
        portrait.setArguments(b);
        //this.detachAllFragments(fragmentManager,fragmentTransaction);
        if (configuration.orientation== Configuration.ORIENTATION_LANDSCAPE){
            //fragmentTransaction.remove(portrait);

            fragmentTransaction.replace(android.R.id.content, landscapeFrag);

            fragmentTransaction.replace(android.R.id.content, landscape2);

        }else
        {
            /*fragmentTransaction.remove(landscapeFrag);
            fragmentTransaction.remove(landscape2);*/
            fragmentTransaction.replace(android.R.id.content, portrait);
            fragmentTransaction.replace(android.R.id.content, portrait);
        }

        fragmentTransaction.commit();

    }


}
