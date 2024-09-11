package com.appsbay.fourgreatclassicalnovels.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.fourgreatclassicalnovels.Controller.BookChapterActivity;
import com.appsbay.fourgreatclassicalnovels.Model.Book;
import com.appsbay.fourgreatclassicalnovels.R;
import com.appsbay.fourgreatclassicalnovels.Tools.MyColor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BooksHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<BooksHorizontalRecyclerViewAdapter.BooksHorizontalRecyclerViewViewHolder> {

    Context context;
    ArrayList<Book> books;

    public BooksHorizontalRecyclerViewAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BooksHorizontalRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BooksHorizontalRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksHorizontalRecyclerViewViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookName.setText(book.getName());
        holder.bookName.setTextColor(MyColor.getTitleTextColor(context));
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookAuthor.setTextColor(MyColor.getDetailTextColor(context));

        try {
            // get input stream
            InputStream ims = context.getAssets().open("covers/" + book.getImageName() + ".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            holder.bookImage.setImageDrawable(d);
            ims.close();
        } catch (IOException ex) {
            return;
        }

        holder.bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent companyInfoIntent = new Intent(context, BookChapterActivity.class);
                companyInfoIntent.putExtra("book", book);
                context.startActivity(companyInfoIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BooksHorizontalRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView bookAuthor;
        ImageView bookImage;

        public BooksHorizontalRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author_name);
            bookImage = itemView.findViewById(R.id.book_image);
        }
    }

}
