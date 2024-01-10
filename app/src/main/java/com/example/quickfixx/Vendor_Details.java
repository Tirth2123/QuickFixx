package com.example.quickfixx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vendor_Details extends AppCompatActivity {
ImageButton imageButton;

TextView vendorname, vendoraddress,vendorrole, vendorexp, vendorcall, vendorwhatsapp;
RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);

        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        ViewPager2 viewPager = findViewById(R.id.vendor_images_view_pager);
        ImageAdapter adapter = new ImageAdapter(profile.getImages());
        viewPager.setAdapter(adapter);

        imageButton = findViewById(R.id.vendor_menu);
        ratingBar = findViewById(R.id.vendor_review);
        vendorname = findViewById(R.id.vendor_name);
        vendoraddress = findViewById(R.id.vendor_Address);
        vendorrole = findViewById(R.id.vendor_who);
        vendorexp = findViewById(R.id.vendor_exp);
        vendorcall = findViewById(R.id.vendor_buttonCall);
        vendorwhatsapp = findViewById(R.id.vendor_buttonChat);

        vendorname.setText(profile.getCompanyName());
        vendoraddress.setText(profile.getAddress());
        vendorrole.setText(profile.getService());
        vendorexp.setText(profile.getExperience());
        vendorcall.setText(profile.getPhoneNo());


        vendorcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = vendorcall.getText().toString();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                v.getContext().startActivity(dialIntent);
            }
        });

        vendorwhatsapp.setOnClickListener(new View.OnClickListener() {
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

        RatingBar ratingBar = findViewById(R.id.vendor_review);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent intent = new Intent(getBaseContext(), Rating_and_Review.class);
                intent.putExtra("Rating", rating);
                intent.putExtra("profile", profile);
                startActivity(intent);
            }
        });
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
    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[1-9][0-9]{9,14}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();

    }
    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private static final String TAG = "ImageAdapter";
        private List<String> imageUrls;

        public ImageAdapter(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_holder, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            String serverUrl = "http://192.168.184.42:3000";
            String imageUrl = serverUrl + "/" + imageUrls.get(position);
            Uri uri = Uri.parse(imageUrl);
            holder.imageView.setImageURI(uri);
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.vendor_images_simple_drawee_view);
            }
        }
    }
}