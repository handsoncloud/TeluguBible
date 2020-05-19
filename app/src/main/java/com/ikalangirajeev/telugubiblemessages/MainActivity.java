package com.ikalangirajeev.telugubiblemessages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private static final int PICK_IMAGE = 24;
    private FloatingActionButton fab;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;

    TextView textViewNavHeaderUserLoggedIn;
    TextView textViewNavHeaderUserEmail;
    ImageView imageViewNavHeaderUserImage;
    Bitmap bitmap;
    Uri imageUri;
    MaterialToolbar toolbar;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Initializing a banner Ad
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9221045649279850~5748740579");
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("పరిశుద్ధ గ్రంధము");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout).build();

        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);

        View viewNavigationHeader = navigationView.getHeaderView(0);
        textViewNavHeaderUserLoggedIn = viewNavigationHeader.findViewById(R.id.textViewNavHeaderUserLoggedIn);
        textViewNavHeaderUserEmail = viewNavigationHeader.findViewById(R.id.textViewNavHeaderUserEmail);
        imageViewNavHeaderUserImage = viewNavigationHeader.findViewById(R.id.imageViewNavHeaderUserImage);


        if (firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
            Glide.with(MainActivity.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(imageViewNavHeaderUserImage);
        }

        imageViewNavHeaderUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                navController.navigate(R.id.createBlogFragment, null, new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build());
                } else {
                    Toast.makeText(MainActivity.this, "Only Logged In User can See this page", Toast.LENGTH_LONG).show();

                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                navigationView.setCheckedItem(item.getItemId());

                switch (item.getItemId()) {
                    case R.id.bibleFragment:
                        navController.navigate(R.id.bibleFragment, null, new NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_right)
                                .setExitAnim(R.anim.slide_out_left)
                                .setPopEnterAnim(R.anim.slide_in_left)
                                .setPopExitAnim(R.anim.slide_out_right)
                                .build());
                        return true;
                    case R.id.blogsFragment:
                        navController.navigate(R.id.blogsFragment, null, new NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_left)
                                .setExitAnim(R.anim.slide_out_right)
                                .setPopEnterAnim(R.anim.slide_in_right)
                                .setPopExitAnim(R.anim.slide_out_left)
                                .build());
                        return true;
                    case R.id.profileFragment:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                            navController.navigate(R.id.profileFragment, null, new NavOptions.Builder()
                                    .setEnterAnim(R.anim.slide_in_left)
                                    .setExitAnim(R.anim.slide_out_right)
                                    .setPopEnterAnim(R.anim.slide_in_right)
                                    .setPopExitAnim(R.anim.slide_out_left)
                                    .build());
                        }else{
                            Toast.makeText(MainActivity.this, "Only Logged In User can See this page", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case R.id.userBlogsFragment:
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                            navController.navigate(R.id.userBlogsFragment, null, new NavOptions.Builder()
                                    .setEnterAnim(R.anim.slide_in_left)
                                    .setExitAnim(R.anim.slide_out_right)
                                    .setPopEnterAnim(R.anim.slide_in_right)
                                    .setPopExitAnim(R.anim.slide_out_left)
                                    .build());
                        } else {
                            Toast.makeText(MainActivity.this, "Only Logged In User can See this page", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.loginFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        navigationView.setVisibility(View.GONE);
                        break;
                    case R.id.registrationFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        navigationView.setVisibility(View.GONE);
                        break;
                    case R.id.resetPasswordFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        navigationView.setVisibility(View.GONE);
                        break;
                    case R.id.profileFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        navigationView.setVisibility(View.GONE);
                        break;
                    case R.id.verifyEmailFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        navigationView.setVisibility(View.GONE);
                        break;
                    case R.id.bibleFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle("పరిశుద్ధ గ్రంధము");
                        toolbar.setSubtitle("66 పుస్తకములు");
                        if (firebaseUser != null && firebaseUser.getDisplayName() != null) {
                            textViewNavHeaderUserLoggedIn.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            textViewNavHeaderUserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        }
                        break;
                    case R.id.chaptersFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle(Objects.requireNonNull(arguments).getString("BookName"));
                        toolbar.setSubtitle(arguments.getString("ChaptersCount"));
                        break;
                    case R.id.versesFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle(Objects.requireNonNull(arguments).getString("BookName"));
                        int chapterNumber = arguments.getInt("ChapterNumber") + 1;
                        toolbar.setSubtitle(chapterNumber + "వ అధ్యాయము");
                        break;
                    case R.id.searchFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle("పరిశుద్ధ గ్రంథములో ");
                        toolbar.setSubtitle("వెతకండి ఏదైనా...");
                        break;
                    case R.id.blogsFragment:
                        fab.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Posts 2020");
                        toolbar.setSubtitle("Press '+' to create...");
                        break;

                    case R.id.userBlogsFragment:
                        fab.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        toolbar.setSubtitle("Posts...");
                        break;
                    case R.id.createBlogFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Create a Post");
                        toolbar.setSubtitle("As you wish...");
                        break;
                    case R.id.detailBlogFragment:
                        fab.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        navigationView.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Comments Section");
                        toolbar.setSubtitle("");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Glide.with(MainActivity.this)
                    .load(bitmap)
                    .into(imageViewNavHeaderUserImage);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imagebytes = byteArrayOutputStream.toByteArray();

            final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("pics/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

            storageReference.putBytes(imagebytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    imageUri = task.getResult();
                                    Toast.makeText(MainActivity.this, "Image uploaded succesfully @ " + imageUri.getPath(),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser == null) {
            navController.navigate(R.id.loginFragment);
        } else if (!firebaseUser.isEmailVerified()) {

            firebaseUser.getDisplayName();
            firebaseUser.getEmail();
            firebaseUser.isEmailVerified();
            firebaseUser.getPhoneNumber();


            Bundle bundle = new Bundle();
            bundle.putString("email", firebaseUser.getEmail());
            Toast.makeText(MainActivity.this, firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
            navController.navigate(R.id.verifyEmailFragment, bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(navController.getCurrentDestination().getId()==R.id.blogsFragment) {
                    searchView.clearFocus();
                    Bundle bundle = new Bundle();
                    bundle.putString("SearchBlogs", query);
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.blogsFragment, bundle, new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build());
                } else {
                    searchView.clearFocus();
                    Bundle bundle = new Bundle();
                    bundle.putString("SearchData", query);
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    navController.navigate(R.id.searchFragment, bundle, new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (hasFocus) {
                    searchView.clearFocus();
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);

            case R.id.action_logout:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Toast.makeText(MainActivity.this, Objects.requireNonNull(firebaseUser).getEmail()
                            + " is logged out", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    NavController navCtrl = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    Bundle bundle = new Bundle();
                    navCtrl.navigate(R.id.loginFragment, bundle, new NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
                            .setPopEnterAnim(R.anim.slide_in_right).setPopExitAnim(R.anim.slide_out_left)
                            .build());
                } else {
                    Toast.makeText(this, "There is no current logged in user", Toast.LENGTH_LONG).show();
                }
                return true;
        }


        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(this, "Search Telugu Bible Content", Toast.LENGTH_LONG).show();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
        } else if (item.getItemId() == R.id.action_logout) {
            new AlertDialog.Builder(this).setTitle("Logout User").setMessage("Are you sure to Logout")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(MainActivity.this, "You are logged out", Toast.LENGTH_LONG).show();
                            Bundle bundle = new Bundle();
                            navController.navigate(R.id.loginFragment, bundle, new NavOptions.Builder()
                                    .setLaunchSingleTop(true)
                                    .setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
                                    .setPopEnterAnim(R.anim.slide_in_right).setPopExitAnim(R.anim.slide_out_left)
                                    .build());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "User is not logged out", Toast.LENGTH_LONG).show();
                        }
                    }).create();
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
