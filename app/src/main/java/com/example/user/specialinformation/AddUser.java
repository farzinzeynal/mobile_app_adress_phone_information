package com.example.user.specialinformation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddUser extends AppCompatActivity {

    EditText editText_name,editText_phone,editText_adress;
    Button button_add;
    ImageView imageView_profile;
    Uri uri;
    LinearLayout linearLayout_photo,linearLayout_galery;
    public static DatabaseHelper databaseHelper;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final static  int REQUEST_CODE_GALLERY = 999;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editText_name = findViewById(R.id.editText_name);
        editText_phone = findViewById(R.id.editText_number);
        editText_adress = findViewById(R.id.editText_adress);
        button_add = findViewById(R.id.button_add);
        imageView_profile = findViewById(R.id.imageView_profile);


        databaseHelper = new DatabaseHelper(this);


        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 getDialog();
            }
        });


        //insert data
        button_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(editText_name.getText().toString().length()==0)
                { editText_name.setError("Boş buraxıla bilməz"); return;}

                if(editText_phone.getText().toString().length()==0)
                { editText_phone.setError("Boş buraxıla bilməz"); return;}

                if(editText_adress.getText().toString().length()==0)
                { editText_adress.setError("Boş buraxıla bilməz"); return;}


                try
                {
                        databaseHelper.insertData
                            (
                                    editText_name.getText().toString().trim(),
                                    editText_phone.getText().toString().trim(),
                                    editText_adress.getText().toString().trim(),
                                    imageViewToByte(imageView_profile)
                            );

                    Toast.makeText(getApplicationContext(),"Əlavə olundu \n Yenilemek üçün ekranı aşağı sürüşdürün",Toast.LENGTH_LONG).show();
                    editText_name.setText("");
                    editText_phone.setText("");
                    editText_adress.setText("");
                    imageView_profile.setImageResource(R.mipmap.profile);
                    finish();
                }
                catch (Exception e)
                { e.printStackTrace(); }

            }
        });
    }


    // dialog for choosing capture & gallery
    private void getDialog()
    {
        final AlertDialog.Builder alert;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            alert = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            alert = new AlertDialog.Builder(this);
        }


        LayoutInflater layoutInflater = getLayoutInflater();

        View view1 = layoutInflater.inflate(R.layout.dialog_layout,null);

        linearLayout_photo = view1.findViewById(R.id.linear_takePhoto);
        linearLayout_galery = view1.findViewById(R.id.linear_takeGalery);


        alert.setView(view1);

        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setCancelable(true);


        linearLayout_photo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                getFromCamera();
                dialog.cancel();
            }
        });

        linearLayout_galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromGalery();
                dialog.cancel();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getFromCamera()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    private void getFromGalery()
    {
        ActivityCompat.requestPermissions(AddUser.this,
                                           new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_CODE_GALLERY);
    }


    //convert Image to byte[]
    private byte[] imageViewToByte(ImageView imageView_profile)
    {
        Bitmap bitmap = ((BitmapDrawable)imageView_profile.getDrawable()).getBitmap();
        ByteArrayOutputStream stream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    //request permisson
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        //request for galery
        if (requestCode == REQUEST_CODE_GALLERY)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Galareya'ya girməyə icazəniz yoxdur",Toast.LENGTH_LONG).show();
            }
            return;
        }

        //request for camera
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Kamera icazəsi verildi", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "Kameranın açılmasına icazə verilmədi", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



    }


    //result and open activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        //result for galery
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
           uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView_profile.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //result for camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView_profile.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);



    }



    //start Crop (now unused)
    private void cropRequest(Uri uri)
    {
        CropImage.activity(uri)
                 .setGuidelines(CropImageView.Guidelines.ON)
                 .setMultiTouchEnabled(true)
                 .start(this);

    }


}
