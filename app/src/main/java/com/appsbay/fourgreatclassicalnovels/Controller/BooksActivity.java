package com.appsbay.fourgreatclassicalnovels.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.fourgreatclassicalnovels.Controller.Menu.MenuActivity;
import com.appsbay.fourgreatclassicalnovels.Model.BookCategoryStore;
import com.appsbay.fourgreatclassicalnovels.Model.BookStore;
import com.appsbay.fourgreatclassicalnovels.R;
import com.appsbay.fourgreatclassicalnovels.Tools.HelperFunctions;
import com.appsbay.fourgreatclassicalnovels.Tools.MyColor;
import com.appsbay.fourgreatclassicalnovels.Tools.MyImage;
import com.appsbay.fourgreatclassicalnovels.Tools.RateItDialogFragment;
import com.appsbay.fourgreatclassicalnovels.View.BooksListRecyclerViewAdapter;
import com.appsbay.fourgreatclassicalnovels.View.BooksVerticalRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

public class BooksActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;
    RecyclerView booksVerticalRecyclerView;
    RecyclerView booksListRecyclerView;
    BooksVerticalRecyclerViewAdapter booksVerticalRecyclerViewAdapter;
    BooksListRecyclerViewAdapter booksListRecyclerViewAdapter;
    Integer viewFlipperChild;
    private Menu menu;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        RateItDialogFragment.show(this, getSupportFragmentManager());

        mAdView = findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setHomeAsUpIndicator(changeDrawableColor(this, R.drawable.nav_more, MyColor.getButtonTintColor(this)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (HelperFunctions.isFirstRun(this)) {
            SharedPreferences preferences = this.getSharedPreferences("Language Preference", Context.MODE_PRIVATE);
            String caiDan = getResources().getString(R.string.Menu);
            if (caiDan.equals("菜單")) {
                preferences.edit().putInt("language", 1).commit();
            } else {
                preferences.edit().putInt("language", 0).commit();
            }
        }

        viewFlipper = findViewById(R.id.main_view_flipper);

        booksVerticalRecyclerView = findViewById(R.id.main_category_recycler_view);
        booksListRecyclerView = findViewById(R.id.main_list_recycler_view);

        booksVerticalRecyclerViewAdapter = new BooksVerticalRecyclerViewAdapter(this, BookCategoryStore.shared.getCategories(this));

        booksVerticalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        booksVerticalRecyclerView.setAdapter(booksVerticalRecyclerViewAdapter);

        booksListRecyclerViewAdapter = new BooksListRecyclerViewAdapter(this, BookStore.shared.getBooks(this));
        booksListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        booksListRecyclerView.setAdapter(booksListRecyclerViewAdapter);

        viewFlipperChild = 1;
        viewFlipper.setDisplayedChild(viewFlipperChild);

        configColor();
    }

    private void configColor() {
        SharedPreferences preferences = this.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");

        viewFlipper.setBackgroundColor(MyColor.getBackgroundColor(this));
        MyImage.setBackgroundImage(this, viewFlipper);

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


        if (menu != null) {
            menu.getItem(0).setIcon(changeDrawableColor(this, R.drawable.nav_language, MyColor.getButtonTintColor(this)));
            menu.getItem(1).setIcon(changeDrawableColor(this, R.drawable.nav_sun, MyColor.getButtonTintColor(this)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, MenuActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.home_menu_action_language:
                SharedPreferences preferences = this.getSharedPreferences("Language Preference", Context.MODE_PRIVATE);
                int language = preferences.getInt("language", 0);

                new XPopup.Builder(this)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asCenterList(getResources().getString(R.string.choose_language), new String[]{"简体中文", "繁體中文"},
                                null, language,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putInt("language", position);
                                        editor.apply();
                                        booksVerticalRecyclerViewAdapter = new BooksVerticalRecyclerViewAdapter(mContext, BookCategoryStore.shared.getCategories(mContext));
                                        booksListRecyclerViewAdapter = new BooksListRecyclerViewAdapter(mContext, BookStore.shared.getBooks(mContext));
                                        booksVerticalRecyclerView.setAdapter(booksVerticalRecyclerViewAdapter);
                                        booksListRecyclerView.setAdapter(booksListRecyclerViewAdapter);
                                        booksListRecyclerViewAdapter.notifyDataSetChanged();
                                        booksVerticalRecyclerViewAdapter.notifyDataSetChanged();
//                                        booksVerticalRecyclerView.invalidate();
//                                        booksListRecyclerView.invalidate();
//                                        finish();
//                                        overridePendingTransition( 0, 0);
//                                        startActivity(getIntent());
//                                        overridePendingTransition( 0, 0);
                                    }
                                })
                        .show();
                return true;
            case R.id.home_menu_action_image:
                Intent i = new Intent(this, ImagesActivity.class);
                startActivityForResult(i, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                configColor();

                getSupportActionBar().setHomeAsUpIndicator(changeDrawableColor(this, R.drawable.nav_more, MyColor.getButtonTintColor(this)));

                booksVerticalRecyclerViewAdapter.notifyDataSetChanged();
                booksListRecyclerViewAdapter.notifyDataSetChanged();

                AdRequest adRequestInterstitialAd = new AdRequest.Builder().build();

                InterstitialAd.load(this, getResources().getString(R.string.adInterstitialID), adRequestInterstitialAd, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Admob", "onAdLoaded");
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(BooksActivity.this);
                        } else {
                            Log.d("Admob", "The interstitial ad wasn't ready yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("Admob", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
            }
        }
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.home_menu, menu);

        menu.getItem(0).setIcon(changeDrawableColor(this, R.drawable.nav_language, MyColor.getButtonTintColor(this)));
        menu.getItem(1).setIcon(changeDrawableColor(this, R.drawable.nav_sun, MyColor.getButtonTintColor(this)));

        return super.onCreateOptionsMenu(menu);
    }
}