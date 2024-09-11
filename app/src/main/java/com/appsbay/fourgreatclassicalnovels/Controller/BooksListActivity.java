package com.appsbay.fourgreatclassicalnovels.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.fourgreatclassicalnovels.Model.Book;
import com.appsbay.fourgreatclassicalnovels.R;
import com.appsbay.fourgreatclassicalnovels.Tools.MyColor;
import com.appsbay.fourgreatclassicalnovels.Tools.MyImage;
import com.appsbay.fourgreatclassicalnovels.View.BooksListRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class BooksListActivity extends AppCompatActivity {

    ArrayList<Book> books;
    RecyclerView booksListRecyclerView;
    BooksListRecyclerViewAdapter booksListRecyclerViewAdapter;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mAdView = findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        books = intent.getParcelableArrayListExtra("books");

        booksListRecyclerView = findViewById(R.id.book_list_activity);

        booksListRecyclerViewAdapter = new BooksListRecyclerViewAdapter(this, books);
        booksListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        booksListRecyclerView.setAdapter(booksListRecyclerViewAdapter);

        configColor();
    }

    private void configColor() {
        SharedPreferences preferences = this.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");

        booksListRecyclerView.setBackgroundColor(MyColor.getBackgroundColor(this));
        MyImage.setBackgroundImage(this, booksListRecyclerView);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MyColor.getActionBarColor(this));

        View decorView = window.getDecorView();
        if (backgroundColorName.equals("dark")) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(MyColor.getActionBarColor(this)));

        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(MyColor.getTitleTextColor(this)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.home_menu_action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.book_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.home_menu_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        int searchHintBtnId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = searchView.findViewById(searchHintBtnId);
        searchIcon.setImageDrawable(MyImage.changeDrawableColor(this, R.drawable.nav_search, MyColor.getButtonTintColor(this)));

        int searchTextID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(searchTextID);
        textView.setTextColor(MyColor.getTitleTextColor(this));

        int searchCloseID = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchClose = searchView.findViewById(searchCloseID);
        searchClose.setImageDrawable(MyImage.changeDrawableColor(this, R.drawable.nav_close, MyColor.getButtonTintColor(this)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                booksListRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                booksListRecyclerViewAdapter.getFilter().filter("");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}