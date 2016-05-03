package grupp2.satansdemocracy;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Tweetadapter extends RecyclerView.Adapter<Tweetadapter.MyViewHolder> {

    private List<Tweet> tweetList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userMessage;
        public ImageView userPicture;
        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.username);
            userMessage = (TextView) view.findViewById(R.id.usermessage);
//            userPicture = (ImageView) view.findViewById(R.id.userpicture);
        }
    }


    public Tweetadapter(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tweet tweet = tweetList.get(position);
        holder.userName.setText(tweet.getUserName());
        holder.userMessage.setText(tweet.getUserMessage());
//        holder.userPicture.(tweet.getUserPicture());

    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }
}