package com.jkr.albert.model;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jkr.albert.R;
import android.view.View;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    List<YoutubeVideos> youtubeVideos;
    public VideoAdapter(){
    }
    public VideoAdapter(List<YoutubeVideos> youtubeVideos){
        this.youtubeVideos = youtubeVideos;
    }
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new VideoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewHolder holder, int position) {
        holder.videoWeb.loadData(youtubeVideos.get(position).getUrl(),"text/html","utf-8");
    }
    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }
    public class VideoViewHolder extends RecyclerView.ViewHolder{
        WebView videoWeb;
        public VideoViewHolder(View itemView){
            super(itemView);
            videoWeb = itemView.findViewById(R.id.videoview);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient(){
            });

        }

    }

}
