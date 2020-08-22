package com.iconlab.turkgram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.security.Provider;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Uploadactivity extends AppCompatActivity {
    EditText editText;
    ImageView imageView;
    Bitmap selectedimage;
    private StorageReference storageReference;
    Uri imagedata;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    HashMap<String, Object> hashmap = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadactivity);
        imageView = findViewById(R.id.addimage);
        editText = findViewById(R.id.sharetext);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth =  FirebaseAuth.getInstance();


    }
    public void addonclick (View view){

        if (ContextCompat.checkSelfPermission(Uploadactivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Uploadactivity.this,new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},1);





        }else{

                ActivityCompat.requestPermissions(Uploadactivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);



        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imagedata = data.getData();
            try {
                if (Build.VERSION.SDK_INT >=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imagedata);
                    selectedimage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedimage);

                }else
                selectedimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagedata);
                imageView.setImageBitmap(selectedimage);
            } catch (IOException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadonclick(View view){
        if (imagedata != null){
            UUID uuid = UUID.randomUUID();
            final String randomname = "image" + uuid + ".jpg";


            storageReference.child(randomname).child("imagesmap").child("turkagram").putFile(imagedata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newreference = FirebaseStorage.getInstance().getReference(randomname);


                    newreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download = uri.toString();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String email = firebaseUser.getEmail();
                            String comment = editText.toString();

                            hashmap.put("usermail",email);
                            hashmap.put("comment",comment);
                            hashmap.put("imageurl",download);
                            hashmap.put("date", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("posts").add(hashmap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Uploadactivity.this, "succes", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Uploadactivity.this,feed_activity.class);


                                    startActivity(intent);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Uploadactivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                                }
                            });



                        }
                    });
                    Toast.makeText(Uploadactivity.this, "Resim Yüklendi!", Toast.LENGTH_SHORT).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Uploadactivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }else{
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this,R.style.Theme_AppCompat_DayNight);


            alertdialog.setTitle("Lütfen resim secin");
            alertdialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);


                }
            });alertdialog.show();

        }
        String text = editText.toString();



    }

}