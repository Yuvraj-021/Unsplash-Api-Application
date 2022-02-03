package com.example.Unsplash_Api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.Unsplash_Api.Screens.HomeFragment;
import com.example.Unsplash_Api.Screens.SearchFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;



public class MainActivity extends AppCompatActivity {

    private static final String TAG=MainActivity.class.getSimpleName();
   ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipNavigationBar=findViewById(R.id.chipnavbar);

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment=null;
                switch (id){
                    case R.id.home:
                        fragment=new HomeFragment();
                        break;
                    case R.id.search:
                        fragment=new SearchFragment();
                }
                if(fragment!=null)
                {
                    fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.constraint_layout1,fragment).commit();
                }else{
                    Log.e(TAG,"Error in creating fragments");
                }
            }
        });
    }
}