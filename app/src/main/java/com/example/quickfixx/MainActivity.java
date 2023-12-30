package com.example.quickfixx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> serviceProviders = Arrays.asList("Provider 1", "Provider 2", "Provider 3", "Provider 4","Provider 5", "Provider 6", "Provider 7", "Provider 8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ServiceProviderAdapter());

    }

    class ServiceProviderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewStar;
        TextView details;
        TextView textViewRating;
        TextView textViewAddress;
        ImageButton imageButton;

        ServiceProviderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Picture);
            textViewName = itemView.findViewById(R.id.wroker_Name);
            textViewRating = itemView.findViewById(R.id.wroker_Rating);
            textViewStar = itemView.findViewById(R.id.wroker_Star);
            textViewAddress = itemView.findViewById(R.id.wroker_Address);
            details = itemView.findViewById(R.id.buttonDetails);
            imageButton = findViewById(R.id.menu);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(MainActivity.this, imageButton);
                    popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            int id = item.getItemId();
                            if (id == R.id.action_logout) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Logout")
                                        .setMessage("Are you sure you want to logout??")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                myEdit.remove("emailId");
                                                myEdit.remove("password");
                                                myEdit.commit();
                                                Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, Login_Page.class);
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

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),Vendor_Details.class);
                    startActivity(intent);
                }
            });
        }
    }

    class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderViewHolder> {
        @Override
        public ServiceProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ServiceProviderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ServiceProviderViewHolder holder, int position) {
            // Replace with your actual image loading logic
            holder.textViewName.setText(serviceProviders.get(position));
            holder.textViewRating.setText("Rating");
            holder.textViewAddress.setText("Address");
        }

        @Override
        public int getItemCount() {
            return serviceProviders.size();
        }
    }
}
