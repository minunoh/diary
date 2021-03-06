
package com.koreatech.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindActivity extends AppCompatActivity implements View.OnClickListener{

private static final String TAG = "FindActivity";

//define view objects
private EditText editTextUserEmail;
private Button buttonFind;
private TextView textviewMessage;
//define firebase object
private FirebaseAuth firebaseAuth;
private FirebaseAuth.AuthStateListener firebaseAuthListener;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        editTextUserEmail = (EditText) findViewById(R.id.editTextUserEmail);
        buttonFind = (Button) findViewById(R.id.buttonFind);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonFind.setOnClickListener(this);

        }

@Override
public void onClick(View view) {
        if(view == buttonFind){
        //비밀번호 재설정 이메일 보내기
        String emailAddress = editTextUserEmail.getText().toString().trim();
        firebaseAuth.sendPasswordResetEmail(emailAddress)
        .addOnCompleteListener(FindActivity.this, new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                                // 메일 보내기 성공
                                Toast.makeText(FindActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                                //메일 보내기 실패
                                Toast.makeText(FindActivity.this, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                        }
                }
        });

        }
}

}

