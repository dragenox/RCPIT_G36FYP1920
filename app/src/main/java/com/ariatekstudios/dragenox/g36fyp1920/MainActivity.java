package com.ariatekstudios.dragenox.g36fyp1920;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ariatekstudios.dragenox.g36fyp1920.fragments.ChatsFragment;
import com.ariatekstudios.dragenox.g36fyp1920.fragments.UsersFragment;
import com.ariatekstudios.dragenox.g36fyp1920.models.User;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ChatsFragment.OnFragmentInteractionListener, UsersFragment.OnFragmentInteractionListener {

    CircleImageView profile_image;
    TextView username;
    FrameLayout fragmentContainer;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);
        loadFragment(new ChatsFragment());

        try {
            checkConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.mipmap.ic_user_icon);
                    } else {
                        Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkConnection() throws IOException, InterruptedException {
        if(!isInternetAvailable()){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Unable to Login!")
                    .setMessage("Login failed, due to no internet access, you can try changing the network settings or retry.")
                    .setPositiveButton(R.string.settings, (dialog, which) -> {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    })
                    .setOnDismissListener(dialog -> {
                        try {
                            checkConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean isInternetAvailable() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Do you want to logout from your account?")
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                        finish();
                    })
                    .setNegativeButton(R.string.no,null)
                    .show();
        }
        if (item.getItemId() == R.id.main_menu_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }



    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_chats:
                return loadFragment(new ChatsFragment());
            case R.id.navigation_users:
                return loadFragment(new UsersFragment());
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
