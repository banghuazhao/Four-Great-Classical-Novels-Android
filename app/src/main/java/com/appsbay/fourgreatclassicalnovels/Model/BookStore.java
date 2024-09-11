package com.appsbay.fourgreatclassicalnovels.Model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class BookStore {
    public static BookStore shared = new BookStore();

    public ArrayList<Book> getBooks(Context context) {
        ArrayList<Book> books;

        SharedPreferences preferences = context.getSharedPreferences("Language Preference", Context.MODE_PRIVATE);
        int language = preferences.getInt("language", 0);

        if (language == 0) {
            books = new ArrayList<Book>() {{
                add(new Book("水浒传", "水浒传", "施耐庵", "水浒传_json", BookType.siDaMingZhu));
                add(new Book("三国演义", "三国演义", "罗贯中", "三国演义_json", BookType.siDaMingZhu));
                add(new Book("西游记", "西游记", "吴承恩", "西游记_json", BookType.siDaMingZhu));
                add(new Book("红楼梦", "红楼梦", "曹雪芹", "红楼梦_json", BookType.siDaMingZhu));
            }};
        } else {
            books = new ArrayList<Book>() {{
                add(new Book("水滸傳", "水滸傳", "施耐庵", "水滸傳_json", BookType.siDaMingZhu_Fan));
                add(new Book("三國演義", "三國演義", "羅貫中", "三國演義_json", BookType.siDaMingZhu_Fan));
                add(new Book("西遊記", "西遊記", "吳承恩", "西遊記_json", BookType.siDaMingZhu_Fan));
                add(new Book("紅樓夢", "紅樓夢", "曹雪芹", "紅樓夢_json", BookType.siDaMingZhu_Fan));
            }};
        }
        return books;
    }

}
