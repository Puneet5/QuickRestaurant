package com.example.hp.quick_rest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.quick_rest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profilefragment extends Fragment {
     DatabaseReference numberd,emaild,adressd,mDatabase;
     TextView mnumber,memail,maddress,mname;
     User user;
     StorageReference mStorage;

    private CircleImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
        return inflater.inflate(R.layout.fragment_profile,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numberd = FirebaseDatabase.getInstance().getReference().child("number");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        emaild = FirebaseDatabase.getInstance().getReference().child("email");
        adressd = FirebaseDatabase.getInstance().getReference().child("address");
        mnumber = (TextView) getActivity().findViewById(R.id.mobile);
        mname = (TextView) getActivity().findViewById(R.id.usern);
        memail = (TextView) getActivity().findViewById(R.id.emailad);
        maddress = (TextView) getActivity().findViewById(R.id.adress);

        mDatabase.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);

                    String name = user.getUsername();
                    String adress = user.getAddress();
                    String number = user.getNumber();
                    String email = user.getEmail();


                    mnumber.setText(number);
                    mname.setText(name);
                    maddress.setText(adress);
                    memail.setText(email);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // [START_EXCLUDE]
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });



        imageView = (CircleImageView) getActivity().findViewById(R.id.profile_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        }





    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}

