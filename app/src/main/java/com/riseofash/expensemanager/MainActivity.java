package com.riseofash.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText a,b;
    Button c;
    TextView d,e;
    int f=0;
    private FirebaseAuth mAuth;
    private ProgressDialog mProcessDialog;
    LinearLayout ab,cd;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            homepage();
        }

        mProcessDialog=new ProgressDialog(this);
        a=(EditText)findViewById(R.id.emailid);
        b=(EditText)findViewById(R.id.password);
        c=(Button)findViewById(R.id.loginbutton);
        d=(TextView)findViewById(R.id.txt1);
        e=(TextView)findViewById(R.id.registerbutton);
        if(savedInstanceState!=null){
            a.setText(savedInstanceState.getString("email"," "));
            b.setText(savedInstanceState.getString("password"," "));
            c.setText(savedInstanceState.getString("button","Log In"));
            d.setText(savedInstanceState.getString("txt1","Login"));
            e.setText(savedInstanceState.getString("register","Don't have an account? Sign Up"));
            f=savedInstanceState.getInt("counter",0);
        }

    }

    public void login(View view){
        if(f==0){
            flogin();
        }
        else {
            fregister();
        }
    }
    public void signup(View view){
        if(f==0){
            f=1;
            d.setText(R.string.registration);
            c.setText(R.string.register);
            e.setText(R.string.already);
        }else if(f==1){
            f=0;
            d.setText(R.string.login);
            c.setText(R.string.log_in);
            e.setText(R.string.dont);
        }
    }
    public void flogin(){

        if(TextUtils.isEmpty(a.getText().toString())){
            a.setError("Email id Required");
            return;
        }
        if(TextUtils.isEmpty(b.getText().toString())){
            b.setError("Password Required");
            return;
        }
        mProcessDialog.setMessage("Processing");
        mProcessDialog.show();
        mAuth.signInWithEmailAndPassword(a.getText().toString(),b.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    homepage();
                    Toast.makeText(MainActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();
                    mProcessDialog.dismiss();
                }
                else{
                    Toast.makeText(MainActivity.this,R.string.login_unsuccess,Toast.LENGTH_SHORT).show();
                    mProcessDialog.dismiss();
                }
            }
        });
    }
    public void fregister(){
        if(TextUtils.isEmpty(a.getText().toString())){
            a.setError("Email id Required");
            return;
        }
        if(TextUtils.isEmpty(b.getText().toString())){
            b.setError("Password Required");
            return;
        }
        mProcessDialog.setMessage("Processing");
        mProcessDialog.show();
        mAuth.createUserWithEmailAndPassword(a.getText().toString(),b.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    homepage();
                    Toast.makeText(MainActivity.this,R.string.registration_success,Toast.LENGTH_SHORT).show();
                    mProcessDialog.dismiss();
                }
                else{
                    Toast.makeText(MainActivity.this,R.string.registration_unsuccess,Toast.LENGTH_SHORT).show();
                    mProcessDialog.dismiss();
                }
            }
        });
    }

    void homepage(){
        startActivity(new Intent(this,HomePageActivity.class));
    }
    @Override
    protected void onSaveInstanceState(Bundle abcd){
        super.onSaveInstanceState(abcd);
        abcd.putString("email",a.getText().toString());
        abcd.putString("password",b.getText().toString());
        abcd.putString("button",c.getText().toString());
        abcd.putString("txt1",d.getText().toString());
        abcd.putString("register",e.getText().toString());
        abcd.putInt("counter",f);
    }
}
