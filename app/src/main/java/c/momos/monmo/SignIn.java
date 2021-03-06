package c.momos.monmo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import c.momos.monmo.Model.Common;
import c.momos.monmo.Model.User;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSigniIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtphone);
        btnSigniIn = (Button)findViewById(R.id.btnSignIn);

        //INIT FIREBASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        //Button to sign in On Click Listener
        btnSigniIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait..");
                mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    //Check if User doest exist in database
                    if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                        //Get User Information
                        mDialog.dismiss();
                        User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        user.setPhone(edtPhone.getText().toString()); //Set Phone

                        if (user.getPassword().equals(edtPassword.getText().toString())) {
                        {
                            Intent homeIntent = new Intent(SignIn.this,Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        }
                        } else {
                            Toast.makeText(SignIn.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this, "User Does Not Exists in Database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
        });

    }
}
