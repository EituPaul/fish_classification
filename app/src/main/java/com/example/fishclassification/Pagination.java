package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fishclassification.ModelForPage.UserData;
import com.example.fishclassification.MyadapterForPage.UserAdapter;
import com.example.fishclassification.Utils.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;

public class Pagination extends AppCompatActivity {


    Button prev, next, page;
    DatabaseReference mUserReference;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<UserData,MyViewHolder> adapter;



    @Override
  protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_pagination);
        recyclerView = findViewById(R.id.recycler_viewpagination);
        recyclerView.setLayoutManager(new LinearLayoutManager(Pagination.this,LinearLayoutManager.VERTICAL,false));//shudhu this chilo

        mUserReference = FirebaseDatabase.getInstance().getReference().child("UserData");

        prev = findViewById(R.id.previous_btn);
               next = findViewById(R.id.next_btn);
               page = findViewById(R.id.show_page_nmbr);
               prev.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       int pageNo = Integer.parseInt(page.getText().toString());
                       if (pageNo == 1) {
                           return;
                       }
                       pageNo -= 1;
                       //displayPost(pageNo);

                       page.setText(pageNo + "");
                   }
               });
               next.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       int pageNo = Integer.parseInt(page.getText().toString());
                       pageNo += 1;
                       page.setText(pageNo + "");
                      // displayPost(pageNo);
                   }
               });

              // displayPost(1);



//           private void displayPost(int pageNo) {
//               Cursor cursor = databaseHelper.getdata(pageNo);
//               if(cursor.getCount()==0){
//
//               }
//               else{
//                   userName.clear();
//                   questionTitle.clear();
//                   date.clear();
//                   ids.clear();
//                   questionDescription.clear();
//                   while(cursor.moveToNext()){
//                       userName.add(cursor.getString(1));
//                       questionTitle.add(cursor.getString(2));
//                       questionDescription.add(cursor.getString(3));
//                       date.add(cursor.getString(4));
//                       ids.add(cursor.getInt(0));
//                   }
//                   adapter = new Adapter(this, userName, questionTitle, questionDescription,date, ids);
//                   recyclerView.setAdapter(adapter);
//                   recyclerView.setLayoutManager(new LinearLayoutManager(this));
//               }
//           }



   }

    private void loaduserdata() {

        FirebaseRecyclerOptions<UserData> options = new FirebaseRecyclerOptions.Builder<UserData>().setQuery(mUserReference,UserData.class).build(); //chamge from video//age"postref"chilo

        adapter = new FirebaseRecyclerAdapter<UserData, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull UserData model) {
                final String postKey = getRef(position).getKey();//posts er j id te data pathabe seta

                holder.postDesc.setText(model.getName());
                //calculate time ago from date time

                holder.username.setText(model.getEmail());




            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistpagination,parent,false);
                return new MyViewHolder(view);
            }
        };

        adapter.startListening();

        recyclerView.setAdapter(adapter);

    }
}