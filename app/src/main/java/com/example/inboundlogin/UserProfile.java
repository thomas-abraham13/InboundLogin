package com.example.inboundlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    ConstraintLayout userprofile;
    private EditText nam;
    private EditText usernam;
    private EditText pass;
    private EditText newpass;
    private EditText confpass;
    private Button changepass;
    private Button btnupdate;

    DatabaseReference databaseUsers ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String id = getIntent().getStringExtra("useid");

        userprofile = findViewById(R.id.userprofile);
        nam=findViewById(R.id.name);
        usernam=findViewById(R.id.username);
        pass=findViewById(R.id.oldpassword);
        newpass=findViewById(R.id.newpassword);
        confpass=findViewById(R.id.confirmpassword);
        changepass=findViewById(R.id.cbpassword);
        btnupdate=findViewById(R.id.update);

        databaseUsers= FirebaseDatabase.getInstance().getReference("user").child(id);

        changepass.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(userprofile);
            btnupdate.setVisibility(View.VISIBLE);
            newpass.setVisibility(View.VISIBLE);
            confpass.setVisibility(View.VISIBLE);
            changepass.setVisibility(View.GONE);
        });
        getData(id);

        Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener((view) -> {
            checkData(id);
            Toast.makeText(this, "User Details Updated", Toast.LENGTH_SHORT).show();
        });

    }

    private void getData(final String useid){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                if(user.getId().matches(useid))
                {
                    usernam.setText(user.getEmailid());
                    pass.setText(user.getPass());
                    nam.setText(user.getFullname());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void checkData(final  String useid){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                if (pass.getText().toString().matches(user.getPass()) && (!nam.getText().toString().matches(user.getFullname()) || !usernam.getText().toString().matches((user.getEmailid()))))
                {
                    updateData(useid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateData(final String useid){

        String username = usernam.getText().toString();
        String password = newpass.getText().toString();
        String name = nam.getText().toString();

        Users user = new Users(username,password,name,useid);

        databaseUsers.setValue(user);
    }
}