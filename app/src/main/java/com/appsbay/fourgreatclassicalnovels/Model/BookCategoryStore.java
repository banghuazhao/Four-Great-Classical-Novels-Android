package com.appsbay.fourgreatclassicalnovels.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class BookCategoryStore {
    public static BookCategoryStore shared = new BookCategoryStore();

    public ArrayList<BookCategory> getCategories(Context context) {

        ArrayList<BookCategory> categories = new ArrayList<>();

        ArrayList<Book> siDaMingZhu = new ArrayList<>();

        SharedPreferences preferences = context.getSharedPreferences("Language Preference", Context.MODE_PRIVATE);
        int language = preferences.getInt("language", 0);

        if (language == 0) {
            for (Book book : BookStore.shared.getBooks(context)) {
                if (book.getBookType() == BookType.siDaMingZhu) {
                    siDaMingZhu.add(book);
                }
            }
            categories.add(new BookCategory("四大名著", siDaMingZhu));
        } else {
            for (Book book : BookStore.shared.getBooks(context)) {
                if (book.getBookType() == BookType.siDaMingZhu_Fan) {
                    siDaMingZhu.add(book);
                }
            }
            categories.add(new BookCategory("四大名著", siDaMingZhu));
        }

        return categories;
    }

    public void setCategories(ArrayList<BookCategory> categories) {
        this.categories = categories;
    }

    public ArrayList<BookCategory> categories;

}
