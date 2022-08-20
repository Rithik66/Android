package com.example.collegeapp.ui.faculty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewAdapter> {
    private List<FacultyData> facultyDataList;
    private Context context;

    public FacultyAdapter(List<FacultyData> facultyDataList, Context context) {
        this.facultyDataList = facultyDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public FacultyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new FacultyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewAdapter holder, int position) {
        FacultyData item = facultyDataList.get(position);
        holder.name.setText(item.getName().toUpperCase());
        holder.post.setText(item.getPost().toUpperCase());
        holder.email.setText(item.getEmail());
        try {
            Picasso.get().load(item.getImage()).into(holder.avatarProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return facultyDataList.size();
    }

    public static void confirmDelete(){

    }

    class FacultyViewAdapter extends RecyclerView.ViewHolder {

        private TextView name,post,email;
        private Button editButton,deleteButton;
        private CircleImageView avatarProfile;

        public FacultyViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            post = itemView.findViewById(R.id.post);
            email = itemView.findViewById(R.id.email);

            avatarProfile = itemView.findViewById(R.id.avatarProfile);
        }
    }
}
