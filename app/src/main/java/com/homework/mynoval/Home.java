package com.homework.mynoval;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.homework.mynoval.tabbar.RecomBook;
import com.homework.mynoval.tabbar.Profile;
import com.homework.mynoval.tabbar.MyBookList;

public class Home extends AppCompatActivity implements View.OnClickListener {
    LinearLayout recomLayout, listLayout, profileLayout;

    ImageView recomImage, listImage, profileImage, currentImage;
    TextView recomText, listText, profileText, currentText;
    FragmentManager manager;
    Fragment booklist, recombook, profile, currentFragment;
    MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initFragment();
        application = MyApplication.getInstance();
    }

    private void initFragment() {
        manager = getSupportFragmentManager();
        if(booklist == null){
            booklist = new MyBookList();
        }
        if(recombook ==null){
            recombook = new RecomBook();
        }
        if(profile == null) {
            profile = new Profile();
        }

        manager.beginTransaction()
                .add(R.id.mainDisplay, booklist)
                .add(R.id.mainDisplay, recombook)
                .add(R.id.mainDisplay, profile)
                .hide(recombook)
                .hide(profile)
                .commit();
        currentFragment = booklist;
    }

    private void initView() {
        listLayout = findViewById(R.id.listLayout);
        recomLayout = findViewById(R.id.recomLayout);
        profileLayout = findViewById(R.id.profileLayout);

        listImage = findViewById(R.id.listImage);
        recomImage = findViewById(R.id.recomImage);
        profileImage = findViewById(R.id.profileImage);

        listText = findViewById(R.id.listText);
        recomText = findViewById(R.id.recomText);
        profileText = findViewById(R.id.profileText);
        currentImage = listImage;
        currentText = listText;
        currentText.setTextColor(getResources().getColor(R.color.blue));
        currentImage.setSelected(true);
        listLayout.setOnClickListener(this);
        recomLayout.setOnClickListener(this);
        profileLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.recomLayout){
            currentImage.setSelected(false);
            currentText.setTextColor(getResources().getColor(R.color.black));

            recomText.setTextColor(getResources().getColor(R.color.blue));
            recomImage.setSelected(true);
            currentImage = recomImage;
            currentText = recomText;
            switchFragment(recombook);
        } else if (view.getId() == R.id.listLayout) {
            currentImage.setSelected(false);
            currentText.setTextColor(getResources().getColor(R.color.black));

            listText.setTextColor(getResources().getColor(R.color.blue));
            listImage.setSelected(true);
            currentImage = listImage;
            currentText = listText;
            switchFragment(booklist);
        } else if (view.getId() == R.id.profileLayout) {
            currentImage.setSelected(false);
            currentText.setTextColor(getResources().getColor(R.color.black));
            profileText.setTextColor(getResources().getColor(R.color.blue));
            profileImage.setSelected(true);
            currentImage = profileImage;
            currentText = profileText;
            switchFragment(profile);
        }
    }

    public void switchFragment(Fragment fragment) {
        manager.beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit();
        currentFragment = fragment;
    }
}