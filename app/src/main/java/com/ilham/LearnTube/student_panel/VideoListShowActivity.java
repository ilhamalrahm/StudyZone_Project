package com.ilham.LearnTube.student_panel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ilham.LearnTube.R;
import com.ilham.LearnTube.adapter.VideoListShowAdapter;
import com.ilham.LearnTube.models.VideoModel;

import java.util.ArrayList;

public class VideoListShowActivity extends AppCompatActivity {

    private ListView lvVideos;
    private ArrayList<VideoModel> videos;
    private VideoListShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        lvVideos = findViewById(R.id.videosListId);

        loadVideos();
    }

    private void loadVideos() {
        videos = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("YoutubeLinks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videos.clear();
                System.out.println("DataSnapshot Len: " + dataSnapshot);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VideoModel vModel = snapshot.getValue(VideoModel.class);
                    videos.add(vModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        adapter = new VideoListShowAdapter(VideoListShowActivity.this, videos);
        lvVideos.setAdapter(adapter);

        lvVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                System.out.println(position);

                Intent i = new Intent(VideoListShowActivity.this, YoutubeVideoShowActivity.class);
                i.putExtra("title", videos.get(position).getTitle());
                i.putExtra("link", videos.get(position).getLink());
                startActivity(i);
            }
        });

//        lvVideos.setOnItemLongClickListener((parent, view, position, id) -> {
//            String message = "Do you want to delete video named \"" + videos.get(position).getTitle() + "\" ?";
//            // show dialog
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(message)
//                    .setNegativeButton("No", null)
//                    .setPositiveButton("Yes", (dialogInterface, i) -> {
//                        // delete the note
//                        FirebaseDatabase.getInstance().getReference("YoutubeLinks").child(videos.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(VideoListShowActivity.this, "Video deleted", Toast.LENGTH_SHORT).show();
//                                loadVideos();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(VideoListShowActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }).show();
//            return true;
//        });

    }
}