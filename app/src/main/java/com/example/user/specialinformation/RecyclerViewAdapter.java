package com.example.user.specialinformation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.CostomViewHolder>
{

    AlertDialog.Builder builder;

    private Context mcontext;
    private List<UserModel> myList;


    public RecyclerViewAdapter(Context mcontext, List<UserModel> myList)
    {
        this.mcontext = mcontext;
        this.myList = myList;
    }




    @NonNull
    @Override
    public CostomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.user_items, null);
        CostomViewHolder holder = new CostomViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CostomViewHolder holder, int position)
    {
        final UserModel userModel = myList.get(position);


        holder.textView_name.setText(userModel.getName());
        holder.textView_phone.setText(userModel.getPhoneNumber());
        holder.textView_adress.setText(userModel.getAdress());
        holder.imageView_profileList.setImageResource(R.mipmap.profile);

        byte[] userImage = userModel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(userImage,0,userImage.length);

        holder.imageView_profileList.setImageBitmap(bitmap);


        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(mcontext);
                builder.setMessage(" silmək istədiyinizdən əminsiniz ?");
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.diqqet_ico);
                builder.setTitle("Diqqət");

                builder.setPositiveButton(
                        "Bəli", new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                String id = String.valueOf(userModel.getId());
                                deleteUser(id);
                            }
                        });

                builder.setNegativeButton(
                        "Xeyir",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });

                builder.show();

            }
        });




        holder.button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = userModel.getPhoneNumber();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mcontext.checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions((Activity) mcontext,new String[]{Manifest.permission.CALL_PHONE},2);
                        return;
                    }
                }

                Intent makeCall = new Intent(Intent.ACTION_CALL);
                makeCall.setData(Uri.parse("tel:" + number));
                mcontext.startActivity(makeCall);
            }
        });



    }



    private void deleteUser(String id)
    {
        MainActivity.databaseHelper.deleteRow(id);
        Toast.makeText(mcontext,"Silindi \n \n Yenilemek üçün ekranı aşağı sürüşdürün",Toast.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount()
    {
        return myList.size();
    }


    /********************************************* CostomViewHolder CLASS************************************************************************/

    class CostomViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView_name, textView_phone, textView_adress;
        ImageView imageView_profileList;
        Button button_delete, button_call;


        public CostomViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textView_name = itemView.findViewById(R.id.textView_name);
            textView_phone = itemView.findViewById(R.id.textView_phone);
            textView_adress = itemView.findViewById(R.id.textView_adress);
            imageView_profileList =itemView.findViewById(R.id.imageView_proflielist);
            button_delete = itemView.findViewById(R.id.button_delete);
            button_call = itemView.findViewById(R.id.button_call);

        }

    }

}
