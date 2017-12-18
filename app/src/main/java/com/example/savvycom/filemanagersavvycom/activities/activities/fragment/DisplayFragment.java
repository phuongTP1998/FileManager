package com.example.savvycom.filemanagersavvycom.activities.activities.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.savvycom.filemanagersavvycom.R;
import com.example.savvycom.filemanagersavvycom.activities.activities.adapter.DisplayFragmentAdapter;
import com.example.savvycom.filemanagersavvycom.activities.activities.manager.EventManager;
import com.example.savvycom.filemanagersavvycom.activities.activities.manager.FileManager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by savvycom on 12/15/2017.
 */

public class DisplayFragment extends Fragment {
    private RecyclerView recyclerView;
    private File path;
    private ArrayList<File> filesAndFolders = new ArrayList<>();
    private Toolbar toolbar;
    private DisplayFragmentAdapter adapter;
    private FloatingActionButton fbNewNote;
    private SubActionButton btCreateFile;
    private SubActionButton btCreateFolder;
    private FloatingActionMenu floatingActionMenu;
    private File root;
    private FileManager fileManager = new FileManager();
    Button btnHome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        fetchFileandFolder();

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("List Folders and Files");


        adapter = new DisplayFragmentAdapter(filesAndFolders, onItemClickListenerCallback, getActivity());
        EventManager.getInstance().init(getActivity(), this, adapter, filesAndFolders);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        setupUI();
        addListeners();

        return view;
    }

    private void addListeners() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchFileandFolder();
                adapter.notifyDataSetChanged();
            }
        });

        btCreateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFolder();

                floatingActionMenu.close(false);
            }
        });
    }

    private void createNewFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText editText = new EditText(getActivity());

        builder.setMessage("Create a new folder")
                .setView(editText)
                .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileManager.newFolder(root, editText.getText().toString());
                        listDirectory(root);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("CANCEL",null)
                .create()
                .show();
    }

    private void setupUI() {
        btnHome = getActivity().findViewById(R.id.home_button);

        fbNewNote = getActivity().findViewById(R.id.fb_new_note);

        SubActionButton.Builder sabBuilder = new SubActionButton.Builder(getActivity());
        btCreateFile = sabBuilder.setBackgroundDrawable
                (getResources().getDrawable(R.drawable.create_file))
                .build();
        btCreateFolder = sabBuilder.setBackgroundDrawable
                (getResources().getDrawable(R.drawable.create_folder))
                .build();

        floatingActionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(btCreateFile)
                .addSubActionView(btCreateFolder)
                .attachTo(fbNewNote)
                .build();
    }

    private void fetchFileandFolder() {
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        listDirectory(root);
    }

    private void listDirectory(File f) {
        File[] files = f.listFiles();
        filesAndFolders.clear();
        for (File file : files) {
            filesAndFolders.add(file);
        }
    }

    private DisplayFragmentAdapter.OnItemClickListener onItemClickListenerCallback = new DisplayFragmentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            root = filesAndFolders.get(position);
            EventManager.getInstance().open(root);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            dialogRequest(position);
        }

        @Override
        public void onIconClick(View view, int position) {

        }
    };

    private void dialogRequest(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("DELETE");
        alertDialog.setMessage("Delete this files?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                File longClickItem = filesAndFolders.get(position);
                EventManager.getInstance().delete(longClickItem);
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
