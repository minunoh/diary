package com.koreatech.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Gallery extends AppCompatActivity {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser user;
    private static FirebaseStorage storage;
    private static ArrayList<GalleryData> arrayList = new ArrayList<>();
    ;
    private DrawerLayout drawerLayout;// ??????????????? ???
    NavigationView navigationView;
    private Toolbar toolbar;// ?????????(??????)
    private static GridViewAdapter galleryadapter = null;
    private static GridView gridView = null;


    private static DatabaseReference mDatabaseReference; // ????????????????????? ????????? ???????????????.
    private static FirebaseDatabase mFirebaseDatabase; // ????????????????????? ????????? ??? ?????? ????????? ??????????????????.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        storage = FirebaseStorage.getInstance();
        galleryadapter = new GridViewAdapter();
        gridView = findViewById(R.id.gallery_gridview);

        // initializing database
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();


        //url ????????????????????? ???????????? ??????
        galleryadapter.items = showGallery();


        //???????????? ???????????? ?????????,
        if (galleryadapter.items.size() != 0) {

            //???????????? ???????????? ????????? ??????
            galleryadapter.notifyDataSetChanged();
            gridView.setAdapter(galleryadapter);
            galleryadapter.notifyDataSetChanged();
        }

        //?????????????????? ????????? ?????????
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                int mid = item.getItemId();
                Intent intent = null;

                if (mid == R.id.M_diary) { // ???????????? ??????
                    intent = new Intent(Gallery.this, WDiaryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_mydiary) { //?????? ???????????? ?????????
                    intent = new Intent(Gallery.this, MydiaryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_calendar) {//?????????
                    intent = new Intent(Gallery.this, Calendar.class);
                    startActivity(intent);
                }
                 else if (mid == R.id.M_home) { // ???
                    intent = new Intent(Gallery.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (mid == R.id.tomembership) {  // ????????????
                    intent = new Intent(Gallery.this, MemberActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_setting) {//??????
                    intent = new Intent(Gallery.this, MemberActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (mid == R.id.M_tdlist) { // ?????? ??????
                    intent = new Intent(Gallery.this, WScheduleActivity.class);
                    startActivity(intent);
                    return true;
                }

                return true;
            }
        });


    }


    //????????? ??? ?????????
    public static class GridViewAdapter extends BaseAdapter {


        AlertDialog dialog;


        //GalleryData ????????? ????????? ?????????
        ArrayList<GalleryData> items = new ArrayList<GalleryData>();


        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(GalleryData item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final GalleryData Item = items.get(position);


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gallery_item, viewGroup, false);


                ImageView iv_icon = (ImageView) convertView.findViewById(R.id.gallery_image);

                //iv_icon imageView??? Glide??? ???????????? ????????? ??????
                Glide.with(context)
                        .load(Uri.parse(Item.getImaguri()))
                        .into(iv_icon);


            } else {
                View view = new View(context);
                view = (View) convertView;
            }

            //??? ????????? ?????? event
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ImageView image = new ImageView(context);
                    image.setImageResource(R.drawable.ic_sprout);

                    //??????????????? ????????? ????????? 700 * 700 ????????? ???????????? ???????????? ??????
                    Glide.with(context)
                            .load(Uri.parse(Item.getImaguri()))
                            .override(700, 700)
                            .into(image);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("\n" + "\n");
                    builder.setView(image);

                    builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
            });

            return convertView;  //??? ?????? ??????
        }
    }


    //Gallery DB?????? url??? ???????????? ??????
    public static ArrayList<GalleryData> showGallery() {
        mDatabaseReference = mFirebaseDatabase.getReference()
                .child("Gallery").child(user.getUid());

        // DB??? ???????????? ?????????
        if (mDatabaseReference != null) {
            mFirebaseDatabase.getReference().child("Gallery").child(user.getUid())
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // firebase ????????????????????? ???????????? ???????????? ???
                    if (arrayList != null)
                        arrayList.clear();
                    gridView.setAdapter(galleryadapter);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // ??????????????? ????????? List??? ????????????


                        //GalleryData ???????????? ???????????? ??????
                        arrayList.add(snapshot.getValue(GalleryData.class));
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // ????????? ???????????? ??? ?????? ?????? ???
                    Log.e("MainActivity", String.valueOf(error.toException())); // ?????? ??????
                }
            });

            return arrayList;
        } else {
            return null;
        }
    }

    public void onClick(View view) {
        int ViewId = view.getId();
        if (ViewId == R.id.iv_menu) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }


    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

}