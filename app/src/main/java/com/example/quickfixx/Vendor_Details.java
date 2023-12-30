package com.example.quickfixx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class Vendor_Details extends AppCompatActivity {
ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);

        imageButton = findViewById(R.id.vendor_menu);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(Vendor_Details.this, imageButton);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if (id == R.id.action_logout) {
                            new AlertDialog.Builder(Vendor_Details.this)
                                    .setTitle("Logout")
                                    .setMessage("Are you sure you want to logout??")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                            myEdit.remove("emailId");
                                            myEdit.remove("password");
                                            myEdit.commit();
                                            Toast.makeText(Vendor_Details.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Vendor_Details.this, Login_Page.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        } else if (id == R.id.action_rate_us) {
                        } else if (id == R.id.action_share_app) {
                        } else if (id == R.id.action_about_us) {
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }
}