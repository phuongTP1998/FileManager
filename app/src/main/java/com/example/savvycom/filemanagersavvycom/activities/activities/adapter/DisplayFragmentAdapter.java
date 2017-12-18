package com.example.savvycom.filemanagersavvycom.activities.activities.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.savvycom.filemanagersavvycom.R;
import com.example.savvycom.filemanagersavvycom.activities.activities.manager.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by savvycom on 12/15/2017.
 */

public class DisplayFragmentAdapter extends RecyclerView.Adapter<DisplayFragmentAdapter.DisplayFragmentViewHolder> {
    private ArrayList<File> fileList;
    private Context context;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private OnItemClickListener onItemClickListener;
    private FileManager fileManager;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

        void onIconClick(View view, int position);
    }

    public DisplayFragmentAdapter(ArrayList<File> file, OnItemClickListener onItemClickListener, Context context) {
        this.fileList = file;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        fileManager = new FileManager();
    }

    @Override
    public DisplayFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_fragment_display, parent, false);

        return new DisplayFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DisplayFragmentViewHolder holder, final int position) {
        final File singleItem = fileList.get(position);

        holder.title.setText(singleItem.getName());
        holder.lastModified.setText(new Date(singleItem.lastModified()).toString());
        setIcon(singleItem, holder);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(v, position);
                return true;
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onIconClick(holder.cardView, position);
            }
        });

        holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    public void setIcon(File file, DisplayFragmentViewHolder holder) {

        String extension;
        Drawable drawable = null;

        try {

            extension = fileManager.getExtension(file.getAbsolutePath());

            if (file.isFile()) {

                switch (extension) {

                    case ".c":
                    case ".cpp":
                    case ".doc":
                    case ".docx":
                    case ".exe":
                    case ".h":
                    case ".html":
                    case ".java":
                    case ".log":
                    case ".txt":
                    case ".pdf":
                    case ".ppt":
                    case ".xls":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_file);
                        break;

                    case ".3ga":
                    case ".aac":
                    case ".mp3":
                    case ".m4a":
                    case ".ogg":
                    case ".wav":
                    case ".wma":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_audio);
                        break;

                    case ".3gp":
                    case ".avi":
                    case ".mpg":
                    case ".mpeg":
                    case ".mp4":
                    case ".mkv":
                    case ".webm":
                    case ".wmv":
                    case ".vob":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_video);
                        break;

                    case ".ai":
                    case ".bmp":
                    case ".exif":
                    case ".gif":
                    case ".jpg":
                    case ".jpeg":
                    case ".png":
                    case ".svg":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_image);
                        break;

                    case ".rar":
                    case ".zip":
                    case ".ZIP":
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_compressed);
                        break;

                    default:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);
                        break;
                }

            } else if (file.isDirectory()) {
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_folder);
            } else drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);

        } catch (Exception e) {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);
        }

        drawable = DrawableCompat.wrap(drawable);
        holder.icon.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class DisplayFragmentViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView lastModified;
        ImageView icon;
        LinearLayout linearLayout;

        public DisplayFragmentViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView) {
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            lastModified = (TextView) itemView.findViewById(R.id.lastModified);
        }
    }
}