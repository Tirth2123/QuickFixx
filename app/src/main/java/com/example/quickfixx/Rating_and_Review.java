package com.example.quickfixx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rating_and_Review extends AppCompatActivity {

    ImageButton imageButton;
    RatingBar ratingBar;
    SimpleDraweeView simpleDraweeView;
    TextView terrible, bad, average, good, excellent;
    TextView comp_name,comp_address;
    TextInputEditText textView_review;
    TextView submit_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_and_review);

        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        submit_review = findViewById(R.id.review_page_submit_review);
        imageButton = findViewById(R.id.review_page_menu);
        terrible = findViewById(R.id.Terrible);
        bad = findViewById(R.id.Bad);
        average = findViewById(R.id.Average);
        good = findViewById(R.id.Good);
        excellent = findViewById(R.id.Excellent);
        ratingBar = findViewById(R.id.review_page_vendor_star);
        textView_review =findViewById(R.id.review_page_review_textview);
        comp_name = findViewById(R.id.review_page_wroker_Name);
        comp_address = findViewById(R.id.review_page_wroker_Address);

        comp_name.setText(profile.getCompanyName());
        comp_address.setText(profile.getAddress());
        if (!profile.getImages().isEmpty()) {
            String serverUrl = "http://192.168.184.42:3000";
            String imageUrl = serverUrl + "/" + profile.getImages().get(0);
            Uri uri = Uri.parse(imageUrl);
            simpleDraweeView = findViewById(R.id.review_page_Picture);
            simpleDraweeView.setImageURI(uri);
        }

        Intent intent = getIntent();
        float ratingFloat = intent.getFloatExtra("Rating", 0);
        int rating = Math.round(ratingFloat);
        ratingBar.setRating(rating);

        if(rating == 1){
            terrible.setVisibility(View.VISIBLE);
        } else if(rating == 2){
            bad.setVisibility(View.VISIBLE);
        } else if(rating == 3){
            average.setVisibility(View.VISIBLE);
        } else if(rating == 4){
            good.setVisibility(View.VISIBLE);
        } else if(rating == 5){
            excellent.setVisibility(View.VISIBLE);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                terrible.setVisibility(View.GONE);
                bad.setVisibility(View.GONE);
                average.setVisibility(View.GONE);
                good.setVisibility(View.GONE);
                excellent.setVisibility(View.GONE);

                if(rating == 1){
                    terrible.setVisibility(View.VISIBLE);
                } else if(rating == 2){
                    bad.setVisibility(View.VISIBLE);
                } else if(rating == 3){
                    average.setVisibility(View.VISIBLE);
                } else if(rating == 4){
                    good.setVisibility(View.VISIBLE);
                } else if(rating == 5){
                    excellent.setVisibility(View.VISIBLE);
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(Rating_and_Review.this, imageButton);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if (id == R.id.action_logout) {
                            new AlertDialog.Builder(Rating_and_Review.this)
                                    .setTitle("Logout")
                                    .setMessage("Are you sure you want to logout??")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                            myEdit.remove("emailId");
                                            myEdit.remove("password");
                                            myEdit.commit();
                                            Toast.makeText(Rating_and_Review.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Rating_and_Review.this, Login_Page.class);
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

        submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewText = textView_review.getText().toString();
                float ratingFloat = ratingBar.getRating();
                int rating = Math.round(ratingFloat);

                if (rating == 0) {
                    Toast.makeText(getApplicationContext(), "Please give a rating", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPref = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String userEmail = sharedPref.getString("emailId", null);

                String serviceProviderContact = profile.getPhoneNo();
                Review review = new Review(userEmail, serviceProviderContact, rating, reviewText);

                Api api = RetrofitClient.getClient();

                Call<ResponseBody> call = api.submitReview(review);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Review submitted successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Vendor_Details.class);
                            intent.putExtra("profile", profile);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to submit review", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}