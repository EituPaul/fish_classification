package com.example.fishclassification.MyadapterForPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishclassification.ModelForPage.UserData;
import com.example.fishclassification.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<UserData> userList;
    Context context;
    public  UserAdapter(Context context, List<UserData> userList){
        this.userList = new ArrayList<>();
        this.context= context;
    }
//    public void addAll (List<UserData> newUserData)
//    {
//        int initsize = userList.size();//initialsize
//        userList.addAll(newUserData);
//       notifyItemRangeChanged(initsize, newUserData.size());
//    }
//
//    public  void removeLastItem(){
//        userList.remove(userList.size()-1);
//    }
//
//    public String getLastItemId()
//    {
//        return userList.get(userList.size()-1).getName();
//
//    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.userlistpagination,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_name.setText(userList.get(position).getName());

        holder.txt_email.setText(userList.get(position).getEmail());


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_email,txt_name;
        public  MyViewHolder(View itemview){
            super(itemview);

            txt_name = (TextView)itemview.findViewById(R.id.namepage);
            txt_email = (TextView)itemview.findViewById(R.id.emailpage);

        }
    }
}

//public class UserAdapter extends FirebaseRecyclerAdapter<UserData, UserAdapter.UserViewHolder> {
//
//    private int limit;
//
//    public UserAdapter(Query query, int limit) {
//        super(new FirebaseRecyclerOptions.Builder<UserData>()
//                .setQuery(query, UserData.class)
//                .build());
//        this.limit = limit;
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserData model) {
//        holder.bind(model);
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistpagination, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//        return super.getItemCount() > limit ? limit : super.getItemCount();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//        private TextView nameTextView;
//        private TextView emailTextView;
//
//        public UserViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.namepage);
//            emailTextView = itemView.findViewById(R.id.emailpage);
//        }
//
//        public void bind(UserData data) {
//            nameTextView.setText(data.getName());
//            emailTextView.setText(data.getEmail());
//        }
//    }
//}

