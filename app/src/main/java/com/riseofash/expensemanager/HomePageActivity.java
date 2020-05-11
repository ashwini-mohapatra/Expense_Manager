package com.riseofash.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riseofash.expensemanager.model.Data;

import java.text.DateFormat;
import java.util.Date;
import java.util.zip.Inflater;

public class HomePageActivity extends AppCompatActivity {

    private Toolbar toolbar;
private FloatingActionButton floatingActionButton;
    EditText aa,bb;
    Spinner cc;
    String[] arraySpinner = new String[] {"---","Expense", "Saving","Salary"};
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    AlertDialog dialog;
    String uid,sd;
    int totalamount1;
    private TextView giid;
    private RecyclerView recyclerView;
    ArrayAdapter<String> adapter;
    private String type,itemname,post_key;
    private int cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar=findViewById(R.id.hometoolbar);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        recyclerView=findViewById(R.id.recycle);
        giid=findViewById(R.id.ttlamt);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expense Manager");

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        uid=mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Expense Manager").child(uid);
        mDatabase.keepSynced(true);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Data data=snap.getValue(Data.class);
                    if(data.getType().equals("Expense")){
                        totalamount1=totalamount1-data.getAmount();
                    }
                    else if(data.getType().equals("Undetermined")){
                        totalamount1=totalamount1-data.getAmount();
                    }
                    else if(data.getType().equals("Saving")){
                        totalamount1=(totalamount1)+(data.getAmount());
                    } else if(data.getType().equals("Salary")){
                        totalamount1=totalamount1+data.getAmount();
                    }
                    sd=String.valueOf(totalamount1);
                    sd="Rs."+sd;
                    giid.setText(sd);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void inputdatum(View view){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(HomePageActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomePageActivity.this);
        View myView = inflater.inflate(R.layout.input_data,null);
        dialog=mDialog.create();
        dialog.setView(myView);
        dialog.show();
        aa=myView.findViewById(R.id.itemname);
        bb=myView.findViewById(R.id.totalamount);
        cc=myView.findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cc.setAdapter(adapter);
    }
    public void adddata(View view){
        String dd[]=new String[3];
        dd[0]=aa.getText().toString();
        dd[1]=bb.getText().toString();
        int amt=Integer.parseInt(dd[1]);
        dd[2]=cc.getSelectedItem().toString();
        if(aa.getText().toString().equals("---")){
            aa.setError("Entry Type is Required");
            return;
        }
        if(TextUtils.isEmpty(bb.getText().toString())){
            bb.setError("Amount Required");
            return;
        }

        if(dd[2].equals("---")){
            dd[2]="Undetermined";
        }
        String id=mDatabase.push().getKey();
        String date= DateFormat.getDateInstance().format(new Date());
        Data data=new Data(id,dd[0],dd[2],amt,date);
        mDatabase.child(id).setValue(data);

        Toast.makeText(this,"Data Added to Database",Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.item_data,
                MyViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder ViewHolder, final Data data, final int i) {
                ViewHolder.setAmount(data.getAmount());
                ViewHolder.setName(data.getName());
                ViewHolder.setType(data.getType());
                ViewHolder.setdate(data.getDate());

                ViewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(i).getKey();
                        cost=data.getAmount();
                        type=data.getType();
                        itemname=data.getName();
                        updatedata();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;
        public MyViewHolder(View itemView) {
            super(itemView);
            myview=itemView;
        }
        public void setType(String type){
            TextView mtxt=myview.findViewById(R.id.type);
            mtxt.setText(type);
        }
        public void setName(String name){
            TextView mtxt2=myview.findViewById(R.id.title);
            mtxt2.setText(name);
        }
        public void setAmount(int amt){
            TextView mtxt3=myview.findViewById(R.id.amt);

            String h="Rs."+String.valueOf(amt);
            mtxt3.setText(h);
        }
        public void setdate(String date){
            TextView mtxt4=myview.findViewById(R.id.date);
            mtxt4.setText(date);
        }

    }
    public void updatedata(){
        }
    public void logout(View view){
        mAuth.signOut();
        startActivity(new Intent(this,MainActivity.class));
    }
}
