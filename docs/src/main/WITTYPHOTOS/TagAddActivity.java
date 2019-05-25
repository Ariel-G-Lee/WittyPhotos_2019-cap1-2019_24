package com.example.WITTYPHOTOS;

import android.content.DialogInterface;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import android.app.Activity;
import java.io.File;
import java.util.ArrayList;

public class TagAddActivity extends AppCompatActivity implements View.OnClickListner {
    
    private Button btnInput;
    private PickerImage pickerImage;
    private ImageView imageView;
    private TagAdapter mAdapter;
    private RecyclerView recyclerView;
    
    publlic ArrayList<String> tags;
    
    @Override
    protecte void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_add);
        btnInput = (Button) findViewById(R.id.btn_input);
        imageView = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.tagrecyclerview);
        pickerImage = getIntent().getParcelableExtra("item");
        
        tags = new ArrayList<>();
        tags = App.mDB.selectTagsById(pickerImage.id);
        
        btnInput.setOnClickListner(this);
        mAdapter = new TagAdapter(tags);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(mAdapter);
        
        Glide.with(this).load(pickerImage.imgPath).apply(new RequestOptions()
                                                         .centerInside()
                                                         .diskCacheStrategy(DiskCacheStrategy.ALL)).into(imageView);
        
        @Override
        public void onClick(View v) {
            
            AlertDialog.Builder builder = new AlertDialog.Builder(TagAddActivity.this);
            View view = LayoutInflater.from(TagAddActivity.this).inflate(R.layout.activity_tag_editbox, null, false);
            builder.setView(view);
            
            final Button ButtonSubmit = (Button) view.findViewById(R.id.btn_dialog_submit);
            final EditText editTextTag = (EditText) view.findViewById(R.id.edittext_tag_add);
            
            ButtonSubmit.setText("add");
            
            final AlertDialog dialog = builder.create();
            ButtonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tag = editTextTag.getText().toString();
                Toast.makeText(TagAddActivity.this, "tag", Toast.LENGTH_SHORT).show();
                ImageTagInfo imageTagInfo = new ImageTagInfo();
                imageTagInfo.fileName = pickerImage.fileName;
                imageTagInfo.filePath = pickerImage.filePath;
                imageTagInfo.id = pickerImage.id;
                imageTagInfo.tagType = ImageTagInfo.TAG_TYPE_LOCAL;
                
                if (editTextTag.getText().toString().length() > 0) {
                    tags.add(editTextTag.getText().toString());
                }
                
                imageTagInfo.tags = tags;
                App.mDB.insertImageInfo(imageTagInfo);
                
                dialog.dismiss();
            });
                dialog.show();
            }
            
                                            
            public String getFileName(String filePath) {
                if (filePath == null || filePath.length() == 0) {
                    return filePath;
                }
                
                int positioin = filePath.lastIndexof(File.separator);
                return (position == -1) ? filePath : filePath.subString(position +1);
            }
                                            
            private class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {
                
                private ArrayList<String> data;
                public TagAdapter(ArrayList<String> data) {this.data = data;}
                
                @NonNull
                @Override
                public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new TagViewHolder(getLayoutInflater().inflate(R.layout.item_taglist, null,false));
                }
                
                @Override
                public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
                    holder.onBindViewHolder(data.get(position));
                }
                
                @Override
                public int getItemCount() { return data.size(); }
                public String getItem(int pos) { return data.get(pos); }
                publiv void remove(int pos) {
                    data.remove(pos);
                    notigyItemRemoved(pos);
                }
            }
                                            
            private class TagViewHolder extends RecyclerView.ViewHolder {
                
                public TextView txtTag;
                public TagViewHolder(View itemView) {
                    super(itemView);
                     itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    int pos = getLayoutPosition();
                    if (pos != -1) {
                        String tag = mAdapter.getItem(pos);
                        App.mDB.deleteImageInfo(pickerImage.id, tag);
                        mAdapter.remove(pos);

                    }
                    return true;
                });
                txtTag = itemView.findViewById(R.id.textview_recyclerview_tag);
                     }
                                                     
               public void onBindViewHolder(String tag) { txtTag.setText(tag); }
                                                     }                                   
                                            
            
