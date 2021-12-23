package com.example.arduino_led;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView tvLedStatus;
    private ImageView imageLed;
    private Button btnOn;
    private Button btnOff;
    // 파이어베이스와 연동
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLedStatus = (TextView) findViewById(R.id.tv_led_status);
        imageLed = (ImageView) findViewById(R.id.image_led);
        // 웹과는 다르게 시작하면서 불러온 값에 따라서 이미지가 세팅이 안됨. 그래서 초기에 이미지 세팅 해놓음
        imageLed.setImageResource(R.drawable.led_off);

        btnOn = (Button) findViewById(R.id.btnOn);
        btnOff = (Button) findViewById(R.id.btnOff);
        // DB의 "LED_STATUS"의 값이 바뀜에 따라서 실행되는 리스너 (LED Image 변경)
        ref.child("LED_STATUS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ledStatus = snapshot.getValue().toString();
                tvLedStatus.setText(ledStatus);
                Toast.makeText(MainActivity.this, ledStatus, Toast.LENGTH_SHORT).show();
                if (ledStatus == "ON")
                    imageLed.setImageResource(R.drawable.led_on);
                else
                    imageLed.setImageResource(R.drawable.led_off);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // On 버튼을 눌렀을 때 실행되는 리스너와 값이 정상적으로 들어갔을 때와 그렇지 않을 때 실행되는 리스너
        btnOn.setOnClickListener(v -> {
            ref.child("LED_STATUS").setValue("ON").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    Toast.makeText(MainActivity.this, "Led On", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
        // Off 버튼을 눌렀을 때 실행되는 리스너와 값이 정상적으로 들어갔을 때와 그렇지 않을 때 실행되는 리스너
        btnOff.setOnClickListener(v -> {
            ref.child("LED_STATUS").setValue("OFF").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    Toast.makeText(MainActivity.this, "Led Off", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}