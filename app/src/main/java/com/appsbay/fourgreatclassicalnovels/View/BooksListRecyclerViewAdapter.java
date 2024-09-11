package com.appsbay.fourgreatclassicalnovels.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.List;
import java.util.Locale;

public class BooksListRecyclerViewAdapter extends RecyclerView.Adapter<BooksListRecyclerViewAdapter.BooksListRecyclerViewAdapterViewHolder> implements Filterable {

    Context context;
    ArrayList<Book> books;
    private List<Book> filteredBooks;
    String searchString;

    public BooksListRecyclerViewAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
        filteredBooks = new ArrayList<>(books);
    }

    @NonNull
    @Override
    public BooksListRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book, parent, false);
        return new BooksListRecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksListRecyclerViewAdapterViewHolder holder, int position) {
        Book book = filteredBooks.get(position);

        String bookNameFull = book.getName();
        String bookAuthorFull = book.getAuthor();
        holder.bookName.setTextColor(MyColor.getTitleTextColor(context));
        holder.bookAuthor.setTextColor(MyColor.getDetailTextColor(context));
        holder.separator.setBackgroundColor(MyColor.getSeparatorColor(context));

        // highlight search text
        if (searchString != null && !searchString.isEmpty()) {
            int startPos = bookNameFull.toLowerCase(Locale.US).indexOf(searchString.toLowerCase(Locale.US));
            int endPos = startPos + searchString.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(bookNameFull);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.rgb(252,147,0)});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.bookName.setText(spannable);
            } else {
                holder.bookName.setText(bookNameFull);
            }
        } else {
            holder.bookName.setText(bookNameFull);
        }

        // highlight search text
        if (searchString != null && !searchString.isEmpty()) {
            int startPos = bookAuthorFull.toLowerCase(Locale.US).indexOf(searchString.toLowerCase(Locale.US));
            int endPos = startPos + searchString.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(bookAuthorFull);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.rgb(252,147,0)});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.bookAuthor.setText(spannable);
            } else {
                holder.bookAuthor.setText(bookAuthorFull);
            }
        } else {
            holder.bookAuthor.setText(bookAuthorFull);
        }

        try {
            // get input stream
            InputStream ims = context.getAssets().open("covers/" +book.getImageName() + ".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            holder.bookImage.setImageDrawable(d);
            ims.close();
        } catch (IOException ex) {
            return;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return filteredBooks.size();
    }

    @Override
    public Filter getFilter() {
        return booksFilter;
    }

    private Filter booksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(books);
                searchString = "";
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                searchString = filterPattern;

                for (Book book : books) {
                    if (book.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                        continue;
                    }
                    if (book.getAuthor().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredBooks.clear();
            filteredBooks.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class BooksListRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView bookAuthor;
        ImageView bookImage;
        View separator;

        public BooksListRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.row_book_name);
            bookAuthor = itemView.findViewById(R.id.row_book_author);
            bookImage = itemView.findViewById(R.id.row_book_image_view);
            separator = itemView.findViewById(R.id.books_list_separator);
        }
    }

}