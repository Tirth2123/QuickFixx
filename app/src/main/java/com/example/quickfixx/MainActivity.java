package com.example.quickfixx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ServiceProviderAdapter adapter;
    ImageButton imageButton,back;
    TextView title,availability;
    Chip chipSortBy, chipTopRated, chipRating;
    private List<Profile> profiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);

        imageButton = findViewById(R.id.menu);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back_main);
        title = findViewById(R.id.title_main);
        chipSortBy = findViewById(R.id.sort_by);
        chipTopRated = findViewById(R.id.top_rated);
        chipRating = findViewById(R.id.ratings);
        availability = findViewById(R.id.avilable);
        SharedPreferences sharedPref = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String name = sharedPref.getString("name", null);
        title.setText("Hello " + name + "!!");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Api api = RetrofitClient.getClient();

        Call<List<Profile>> call = api.getProfiles();
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profile>> call, @NonNull Response<List<Profile>> response) {
                if (!response.isSuccessful()) {
                    Log.e("API Error", "Error loading profiles: " + response.code());
                    return;
                }

                profiles = response.body();
                adapter = new ServiceProviderAdapter(profiles);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Log.e("MainActivity", "Error loading profiles", t);
                Toast.makeText(MainActivity.this, "Error loading profiles", Toast.LENGTH_SHORT).show();
            }
        });
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
                                            myEdit.remove("name");
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chipSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        chipTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Profile> sortedProfiles = sortProfilesByRating(profiles);
                updateRecyclerView(sortedProfiles, "All services");
            }
        });

        chipRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingsMenu(v);
            }
        });

    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_services, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String service = item.getTitle().toString();
                List<Profile> filteredProfiles = filterProfilesByService(service);
                updateRecyclerView(filteredProfiles, service);
                return true;
            }
        });
        popupMenu.show();
    }

    private List<Profile> filterProfilesByService(String service) {
        List<Profile> filteredProfiles = new ArrayList<>();
        for (Profile profile : profiles) {
            if (profile.getService().equals(service)) {
                filteredProfiles.add(profile);
            }
        }
        return filteredProfiles;
    }

    private void updateRecyclerView(List<Profile> profiles, String service) {
        if (profiles.isEmpty()) {
            availability.setText("At this moment, there is no " + service + " available.");
            availability.setVisibility(View.VISIBLE);
        } else {
            availability.setVisibility(View.GONE);
        }
        adapter = new ServiceProviderAdapter(profiles);
        recyclerView.setAdapter(adapter);
    }

    private List<Profile> sortProfilesByRating(List<Profile> profiles) {
        // Create a new list to avoid modifying the original list
        List<Profile> sortedProfiles = new ArrayList<>(profiles);
        Collections.sort(sortedProfiles, new Comparator<Profile>() {
            @Override
            public int compare(Profile p1, Profile p2) {
                return Double.compare(p2.getAverageRating(), p1.getAverageRating());
            }
        });
        return sortedProfiles;
    }

    private void showRatingsMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_ratings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click here
                String selectedFilter = item.getTitle().toString();
                List<Profile> filteredProfiles = filterProfilesByRating(selectedFilter);
                updateRecyclerView(filteredProfiles, selectedFilter);
                return true;
            }
        });
        popupMenu.show();
    }

    private List<Profile> filterProfilesByRating(String filter) {
        double minRating = 0;
        if (!filter.equals("Any")) {
            String ratingString = filter.replace("+", ""); // Remove the "+" sign
            minRating = Double.parseDouble(ratingString);
        }
        List<Profile> filteredProfiles = new ArrayList<>();
        for (Profile profile : profiles) {
            if (profile.getAverageRating() >= minRating) {
                filteredProfiles.add(profile);
            }
        }
        return filteredProfiles;
    }

    class ServiceProviderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewStar;
        TextView details;
        TextView textViewRating;
        TextView textViewAddress;
        TextView textViewWhatsapp;
        TextView textPhoneNo;
        TextView textViewService;
        CardView cardView;

        ServiceProviderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Picture);
            textViewName = itemView.findViewById(R.id.wroker_Name);
            textViewRating = itemView.findViewById(R.id.wroker_Rating);
            textViewStar = itemView.findViewById(R.id.wroker_Star);
            textViewAddress = itemView.findViewById(R.id.wroker_Address);
            textViewService = itemView.findViewById(R.id.who);
            textPhoneNo = itemView.findViewById(R.id.buttonCall);
            textViewWhatsapp = itemView.findViewById(R.id.buttonChat);
            details = itemView.findViewById(R.id.buttonDetails);
            cardView = itemView.findViewById(R.id.card);
        }
    }

    class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderViewHolder> {
        List<Profile> profiles;

        ServiceProviderAdapter(List<Profile> profiles) {
            this.profiles = profiles;
        }

        @Override
        public ServiceProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ServiceProviderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ServiceProviderViewHolder holder, int position) {
            Profile profile = profiles.get(position);
            holder.textViewName.setText(profile.getCompanyName());
            holder.textViewAddress.setText(profile.getAddress());
            holder.textViewService.setText(profile.getService());
            holder.textPhoneNo.setText(profile.getPhoneNo());
            holder.textViewRating.setText(String.valueOf(profile.getTotalRatings()) + " Ratings");
            holder.textViewStar.setText(String.format("%.1f", profile.getAverageRating()));
            if (!profile.getImages().isEmpty()) {
                String serverUrl = RetrofitClient.BASE_URL;
                String imageUrl = serverUrl + "/" + profile.getImages().get(0);
                Uri uri = Uri.parse(imageUrl);
                SimpleDraweeView draweeView = (SimpleDraweeView) holder.itemView.findViewById(R.id.Picture);
                draweeView.setImageURI(uri);
            }
            holder.textPhoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = holder.textPhoneNo.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                    v.getContext().startActivity(dialIntent);
                }
            });
            holder.textViewWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String WhatsappNumber = profile.getWhatsappNumber();
                    if (isValidPhoneNumber(WhatsappNumber)) {
                        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + WhatsappNumber);
                        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            v.getContext().startActivity(whatsappIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(v.getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Vendor_Details.class);
                    intent.putExtra("profile", profile);
                    startActivity(intent);
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Vendor_Details.class);
                    intent.putExtra("profile", profile);
                    startActivity(intent);
                }
            });
        }
        public boolean isValidPhoneNumber(String phoneNumber) {
            String regex = "^[1-9][0-9]{9,14}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.matches();
        }
        @Override
        public int getItemCount() {
            return profiles.size();
        }
    }
}