package com.example.chatup.ui.dashboard;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.databinding.FragmentDashboardBinding;
import com.example.chatup.groupMessages.GroupList;
import com.example.chatup.groupMessages.GroupsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    public String myGroupName;
    public String mobile;
    public String myname;
    public final List<GroupList> groupLists = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scambuster-4568a-default-rtdb.firebaseio.com/");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        Intent intent = requireActivity().getIntent();
        mobile = intent.getStringExtra("mobile");
        myname = intent.getStringExtra("name");

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView grouprecyclerView = binding.groupsRecyclerview;


        grouprecyclerView.setHasFixedSize(true);
        grouprecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        final GroupsAdapter groupsAdapter= new GroupsAdapter(root.getContext(),groupLists);
        grouprecyclerView.setAdapter(groupsAdapter);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupLists.clear();
                for (DataSnapshot dataSnapshotg : snapshot.child("Groups").getChildren()) {
                    final String groupusername = dataSnapshotg.getKey();
                    if (dataSnapshotg.child("members").hasChild(mobile)) {
                        String groupprofilepic = null;
                        if (groupusername != null) {
                            groupprofilepic = dataSnapshotg.child(groupusername).child("profile_pic").getValue(String.class);
                        }
                        GroupList groupList;
                        groupList = new GroupList("new messages", groupusername, groupprofilepic, 0, mobile, myname);
                        groupLists.add(groupList);
                        groupsAdapter.updateGroupData(groupLists);
                    }


                }
                grouprecyclerView.setAdapter(groupsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder makegroup = new AlertDialog.Builder(root.getContext());
                makegroup.setTitle("Enter group name");
                final EditText groupname = new EditText(root.getContext());
                groupname.setInputType(InputType.TYPE_CLASS_TEXT);
                makegroup.setView(groupname);
                makegroup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myGroupName = groupname.getText().toString();
                        if (!myGroupName.equals("")){

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("Groups").child(myGroupName).child("members").child(mobile).setValue("");
                                databaseReference.child("Groups").child(myGroupName).child("profile_pic").setValue("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpCKq1XnPYYDaUIlwlsvmLPZ-9-rdK28RToA&usqp=CAU");

                                Toast.makeText(root.getContext(), "group " +myGroupName +" created", Toast.LENGTH_SHORT).show();



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                            Toast.makeText(root.getContext(), "please enter the name of group", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                makegroup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.cancel();
                    }
                });
                makegroup.show();

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}