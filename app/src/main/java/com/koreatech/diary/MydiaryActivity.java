package com.koreatech.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MydiaryActivity extends AppCompatActivity {
    //firebase auth object
    private static FirebaseAuth firebaseAuth;

    //firebase data object
    private static DatabaseReference mDatabaseReference; // 데이터베이스의 주소를 저장합니다.
    private static FirebaseDatabase mFirebaseDatabase; // 데이터베이스에 접근할 수 있는 진입점 클래스입니다.
    private static FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ArrayList<DiaryData> mList;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DiaryData diaryData;
    private TextView D_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydiary);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationView = (NavigationView) findViewById(R.id.navigation);

        //네비계이션 메뉴 선택시 이벤트 처리
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                int mid = item.getItemId();
                if (mid == R.id.M_diary) { // 다이어리 작성
                    Intent intent = new Intent(MydiaryActivity.this, WDiaryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_tdlist) { // 일정 작성
                    Intent intent = new Intent(MydiaryActivity.this, WScheduleActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_calendar) {//캘린더
                    Intent intent = new Intent(MydiaryActivity.this, Calendar.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_picture) {//갤러리
                    Intent intent = new Intent(MydiaryActivity.this, Gallery.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_home) { // 홈
                    Intent intent = new Intent(MydiaryActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.tomembership) {  // 개인정보
                    Intent intent = new Intent(MydiaryActivity.this, MemberActivity.class);
                    startActivity(intent);
                    return true;
                } else if (mid == R.id.M_setting) {//설정
                    Intent intent = new Intent(MydiaryActivity.this, MemberActivity.class);
                    startActivity(intent);
                    return true;
                }

                return true;
            }
        });


        firstInit();// 리스트에 있는거

        //Diary DB에서 현재 유저의 다이어리 데이터를 가져온다
        mDatabaseReference = mDatabaseReference.child("Diary").child(user.getUid());
        //비어있지 않다면
        if (mDatabaseReference != null) {
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (mList != null)
                        mList.clear();
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren().iterator().next().getChildren()) {
                            //DiaryData형태로 데이터 추출
                            addItem(dataSnapshot.getValue(DiaryData.class));

                        }

                        mRecyclerViewAdapter.notifyDataSetChanged();
                    } catch (NoSuchElementException e) {

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


        //리사이클러뷰에 반영
        mRecyclerViewAdapter = new RecyclerViewAdapter(mList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    public void firstInit() {  // mList를 생성
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mList = new ArrayList<DiaryData>();
    }

    // 리스트에 다이어리 데이터 추가
    public void addItem(DiaryData data) {
        DiaryData item = new DiaryData();
        item.setImagename(data.getImagename());
        item.setImageurl(data.getImageurl());
        item.setTime(data.getTime());
        item.setDay(data.getDay());
        item.setTheme(data.getTheme());
        item.setContent(data.getContent());
        mList.add(item);

    }


    public void onClick(View view) {
        int ViewId = view.getId();
        if (ViewId == R.id.iv_menu) {  // 햄버거 버튼 클릭시 네비게이션 드로어
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
