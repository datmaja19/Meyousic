package adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import ModalClass.SongModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.tudiby.freemusic.MainActivity;
import com.tudiby.freemusic.MusicUtils;
import com.tudiby.freemusic.R;



public class Playlist_RecycleView_Adapter extends RecyclerView.Adapter<Playlist_RecycleView_Adapter.MyViewHolder> {

    Context context;
    private List<SongModel> playlistModalClassList = new ArrayList<>();;

    InterstitialAd mInterstitialAd;
    SweetAlertDialog pDialog;




    public Playlist_RecycleView_Adapter(Context mainActivityContacts, List<SongModel> listModalClassList) {
        this.playlistModalClassList = listModalClassList;
        this.context = mainActivityContacts;
        }

@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_song_list, parent, false);



        return new MyViewHolder(itemView);


        }

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
@Override
public void onBindViewHolder(final MyViewHolder holder, final int position){


    final SongModel modalClass = playlistModalClassList.get(position);

    MusicUtils musicUtils= new MusicUtils();
    holder.title.setText(modalClass.getTitle());
    holder.artists.setText(modalClass.getArtist());
    holder.duration.setText(musicUtils.milliSecondsToTimer(Long.parseLong(modalClass.getDuration())));
    Glide
            .with(context)
            .load(modalClass.getImageurl())
            .centerCrop()
            .placeholder(R.drawable.iconapp)
            .into(holder.image);


    holder.mainly.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

         showinter(position);


        }
    });


    holder.rmplaylists.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (context instanceof MainActivity) {
                ((MainActivity)context).removefromplaylists(modalClass);
            }




        }
    });
        }

@Override
public int getItemCount() {
        return playlistModalClassList.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {



    ImageView image,rmplaylists;
    TextView title,artists,duration;
    LinearLayout mainly;


    public MyViewHolder(View view) {
        super(view);




        image = view.findViewById(R.id.imageview);
        title = view.findViewById(R.id.txttitle);
        artists = view.findViewById(R.id.txtartists);
        duration=view.findViewById(R.id.txtduration);
        mainly=view.findViewById(R.id.mainly);
        rmplaylists=view.findViewById(R.id.removefromplaylists);



    }

}

    public  void showinter(final int position) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Showing Ads");
        pDialog.setCancelable(false);

        pDialog.show();


        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.interads));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                pDialog.cancel();
                if (context instanceof MainActivity) {
                    ((MainActivity)context).playmusic(position,playlistModalClassList);
                    ((MainActivity)context).addtorecent();
                }

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                pDialog.cancel();
                if (context instanceof MainActivity) {

                    ((MainActivity)context).playmusic(position,playlistModalClassList);
                    ((MainActivity)context).addtorecent();
                }
                // Code to be executed when the interstitial ad is closed.
            }
        });





    }
}
