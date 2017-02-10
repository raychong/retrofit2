package com.pentagon.retrofitexample.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pentagon.retrofitexample.MainActivity;
import com.pentagon.retrofitexample.R;
import com.pentagon.retrofitexample.model.GoList;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


/**
 * Created by Upnixt User on 10/27/2015.
 * DayAdapter for UpdateGoListEvent lists
 */
public class GoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GoList> list;
    private Context mContext;
    private AdapterCallback mAdapterCallback;
    private static final int TYPE_PRELOADER = 0;
    private static final int TYPE_GOLIST  = 1;
    private static final int TYPE_DIVIDER  = 2;
    private static final int TYPE_FOOTER  = 3;
    private static final int TYPE_HEADER = 4;
    private boolean isFromProfile = false;
    private Fragment selectedFragment;
    private int imgWidth,imgHeight;
    private CircularProgressBar progressIndicator;

    public GoListAdapter(Context ctx, AdapterCallback callback, int imgWidth, int imgHeight) {
        this.mContext = ctx;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        list = new ArrayList<>();
        try {
            this.mAdapterCallback = callback;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    public GoListAdapter(Context ctx, AdapterCallback callback, boolean fromProfile, int imgWidth, int imgHeight) {
        this.mContext = ctx;
        this.isFromProfile = fromProfile;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        list = new ArrayList<>();
        try {
            this.mAdapterCallback = callback;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    public void setList( ArrayList<GoList> list) {
        try{
            this.list.clear();
            append(list);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void append(ArrayList<GoList> list) {
        try{
            int positionStart = this.list.size();
            int itemCount = list.size();
            this.list.addAll(list);
            if (positionStart > 0 && itemCount > 0) {
                notifyItemRangeInserted(positionStart, itemCount);
            } else {
                notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh = null;
        switch(viewType) {
            case TYPE_DIVIDER:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_golist_featured_row_divider, parent, false);
                vh = new VHDivider(v);
                break;
            case TYPE_GOLIST:
                View vGolist = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_golist_featured_row, parent, false);
                vh = new VHGoList(vGolist);
                break;
            case TYPE_PRELOADER:
                View vPreloader = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_golist_preloader, parent, false);
                vh = new VHPreloader(vPreloader);
                break;
            case TYPE_FOOTER:
                View vFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_gridview, parent, false);
                vh =  new VHFooter(vFooter);
                break;
            case TYPE_HEADER:
                View vHeader = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_no_go_list, parent, false);
                vh =  new VHHeader(vHeader);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

        switch (getItemViewType(position)) {
            case TYPE_GOLIST:
                final GoList goList = list.get(position);
                final VHGoList itemHolder = (VHGoList) holder;
                final ImageView imgBookmark = itemHolder.imgBookmark;
                Glide.with(mContext).load(goList.isBookmark() ? R.drawable.ic_bookmark_active_38dp : R.drawable.ic_bookmark_38dp).into(imgBookmark);
                itemHolder.goListName.setText("" + goList.getName());

                Glide.with(mContext)
                        .load(getGoListCoverPhoto(goList))
                        .placeholder(itemHolder.goListCoverPhoto.getDrawable())
                        .centerCrop()
                        .dontAnimate()
                        .into(itemHolder.goListCoverPhoto);
                itemHolder.placeNum.setText(getGoListPlaceNumLabel(goList));
                itemHolder.upvoteNum.setText(getGoListUpvotesLabel(goList));


//                if(goList.isLiked()){
//                  itemHolder.imgUpvote.setImageResource(R.drawable.v2_upvote_filled_blue);
//                }else{
//                    itemHolder.imgUpvote.setImageResource(R.drawable.v2_upvote_filled);
//                }

                itemHolder.collaboratorsViewFrame.removeAllViews();
//                if (goList.getCollaborators() != null) {
//                    itemHolder.collaboratorsViewFrame.addView(getCollaborators(goList.getCollaborators().getList(), goList.getCollaborators().getExcessCount()).getCollaboratorView());
//                }

                itemHolder.imgBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        try {
//                            if (!UpNixt.getInstance().getLoggedInId(mContext).isEmpty()) {
//                                goList.setIsBookmark(!goList.isBookmark());
//                                Glide.with(mContext).load(goList.isBookmark() ? R.drawable.ic_bookmark_active_38dp : R.drawable.ic_bookmark_38dp).into(imgBookmark);
//                                list.get(position).setIsBookmark(goList.isBookmark());
//                            }
//                            mAdapterCallback.onMethodCallback(list.get(position), position);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                });
                itemHolder.view.getLayoutParams().height = (int) ((itemHolder.imgBookmark.getLayoutParams().height) * 1.2);
                break;
            case TYPE_PRELOADER:
                VHPreloader itemPreloader = (VHPreloader) holder;
                animatePreloader(itemPreloader.title);
                animatePreloader(itemPreloader.desc);
                animatePreloader(itemPreloader.button);
                break;

            case TYPE_HEADER:
                VHHeader itemHeader = (VHHeader) holder;
                itemHeader.tvTitle.setText("Featured Go List");
                break;
            case TYPE_FOOTER:
//                VHFooter itemFooter = (VHFooter)holder;
//                itemFooter.progressIndicator.start();
//                itemFooter.progressIndicator.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void animatePreloader(View view){
        try {
            int colorStart =  ContextCompat.getColor(mContext, R.color.opaque_70_black_bg);
            int colorEnd   = ContextCompat.getColor(mContext, R.color.row_white_opaque);

            ValueAnimator colorAnim = ObjectAnimator.ofInt(
                    view, "backgroundColor", colorStart, colorEnd);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setDuration(1200);
            colorAnim.setRepeatCount(ValueAnimator.INFINITE);
            colorAnim.setRepeatMode(ValueAnimator.REVERSE);
            colorAnim.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface AdapterCallback {
        void onMethodCallback(GoList golist, int pos);
    }
    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public String getGoListCoverPhoto(GoList goList) {
        String photoUrl="";
        try {
            photoUrl = constructCoverURL(goList.getPhotoReference());
        }catch (Exception e){
            e.printStackTrace();
        }
        return photoUrl;
    }

    public String constructCoverURL(String imageURL){
        try{
            if(imageURL != null) {
                if (imageURL.contains("agoda.net")) {
                    String[] str = imageURL.split("/");
                    String fileName = str[str.length - 1];
                    if (fileName.contains("?")) {
                        String[] fileStr = fileName.split("\\?");
                        fileName = fileStr[0];
                    }
                    String defaultPrefix = "http://d3f48swccogl5m.cloudfront.net/place/";
                    String url = defaultPrefix + imgWidth + "x" + imgHeight + "/" + fileName + "?url=" + imageURL;
                    return url;
                } else {
                    if (imageURL != null) {
                        if (imgWidth > 720)
                            imgWidth = 720;
                        if (imgHeight > 500)
                            imgHeight = 500;
                        if (imageURL.startsWith("//files.upnixt.com/cover")) {
                            String[] str = imageURL.split("cover/");
                            String fileName = str[1];
                            String defaultPrefix = "http://d3f48swccogl5m.cloudfront.net/cover/";

                            String url = defaultPrefix + imgWidth + "x" + imgHeight + "/" + fileName + "?url=http:" + imageURL;
                            return url;
                        } else if (imageURL.startsWith("//files.upnixt.com/moment")) {
                            String[] str = imageURL.split("moment/");
                            String fileName = str[1];
                            String defaultPrefix = "http://d3f48swccogl5m.cloudfront.net/cover/";

                            String url = defaultPrefix + imgWidth + "x" + imgHeight + "/" + fileName + "?url=http:" + imageURL;
                            return url;
                        } else {
                            String[] str = imageURL.split("/");
                            String fileName = str[str.length - 1];
                            String defaultPrefix = "http://d3f48swccogl5m.cloudfront.net/place/";

                            String url = defaultPrefix + imgWidth + "x" + imgHeight + "/" + fileName + "?url=" + imageURL;

                            return url;
                        }
                    } else {
                        return "";
                    }
                }

            }
            return "";
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }



    public String getGoListUpvotesLabel(GoList goList) {
        String upvotesStr;
        if (goList.getVote()  < 2) {
            upvotesStr = goList.getVote() + " " + mContext.getString(R.string.upvote);
        } else {
            upvotesStr = goList.getVote() + " " + mContext.getString(R.string.upvotes);
        }
        return upvotesStr;
    }
    public String getGoListPlaceNumLabel(GoList goList) {
        String placeNumStr;
        if (goList.getPlaceCount() < 2) {
            placeNumStr = goList.getPlaceCount() + " Place" ;
        } else {
            placeNumStr = goList.getPlaceCount() + " Places";
        }
        return placeNumStr;
    }
//    public CollaboratorViewBuilder getCollaborators(List<Collaborator> collaborators, int excessCount) {
//
//        CollaboratorViewBuilder builder = new CollaboratorViewBuilder(mContext);
//        int maxSize = (collaborators.size() > 4) ? 4 : collaborators.size();
//        for (int i=0; i<maxSize; i++) {
//            builder.setCollaborator(collaborators.get(i));
//        }
//
//        if (excessCount == -1) {
//            int excessCountComputed = 0;
//            if (collaborators.size() > 4) {
//                excessCountComputed = collaborators.size() - 4;
//            }
//            builder.setExcessCount(excessCountComputed);
//        } else {
//            builder.setExcessCount(excessCount);
//        }
//       return builder;
//    }

    public ArrayList<GoList> getList() {
        return list;
    }

    public Fragment getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(Fragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }
    @Override
    public int getItemViewType(int position) {
        int defaultType =  TYPE_GOLIST ;
        if(isPositionFooter(position)){
            defaultType = TYPE_FOOTER;
        }else {
            if (list.get(position).isPreloader()) {
                defaultType = TYPE_PRELOADER;
            }else if(list.get(position).isHeader()){
                defaultType = TYPE_HEADER;
            } else {
                if (list.get(position).isDivider()) {
                    defaultType = TYPE_DIVIDER;
                } else {
                    defaultType = TYPE_GOLIST;
                }
            }
        }
        return defaultType;
    }
    private boolean isPositionFooter(int position) {
        return position == list.size();
    }
    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
    class VHDivider extends RecyclerView.ViewHolder {
        public VHDivider(View itemView) {
            super(itemView);
        }
    }
    class VHGoList extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView goListCoverPhoto;
        ImageView imgBookmark;
        ImageView imgUpvote;
        TextView goListName;
        TextView placeNum;
        TextView upvoteNum;
        FrameLayout collaboratorsViewFrame;
        View view;

        public VHGoList(View itemView) {
            super(itemView);
            goListCoverPhoto = (ImageView) itemView.findViewById(R.id.golist_cover_photo);
            goListName = (TextView) itemView.findViewById(R.id.golist_name);
            placeNum = (TextView) itemView.findViewById(R.id.golist_places_num);
            upvoteNum = (TextView) itemView.findViewById(R.id.golist_upvote_num);
            collaboratorsViewFrame = (FrameLayout) itemView.findViewById(R.id.collaborators_frameview);
            imgBookmark = (ImageView) itemView.findViewById(R.id.imageBookmark);
            imgUpvote  = (ImageView) itemView.findViewById(R.id.imgUpvote);
            view = (View) itemView.findViewById(R.id.v);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
//            try{
//                int pos = isFromProfile?getAdapterPosition()-1:getAdapterPosition();
//                GoList goList =new GoList(); //list.get(pos);//GoListAdapter
//                goList.setId(list.get(pos).getId());
//                goList.setPhotoReference(list.get(pos).getPhotoReference());
//                Intent intent = new Intent(mContext, GoListActivity.class);
//                intent.putExtra("item", goList);
//                intent.putExtra("position", getAdapterPosition());
//                intent.putExtra("fromProfile", isFromProfile);
//                intent.putExtra("showDetail", true);
//                intent.putExtra("imageWidth", imgWidth);
//                intent.putExtra("imageHeight", imgHeight);
//                mContext.startActivity(intent);
//
//                ProfileGoListFragment.prevPosition = getAdapterPosition();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateLayout(Intent intent,View view){
        try {
                String transitionName = mContext.getString(R.string.accept);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(((MainActivity) mContext), view, transitionName);
                mContext.startActivity(intent, transitionActivityOptions.toBundle());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    class VHPreloader extends RecyclerView.ViewHolder {
        View background,profile,title,desc,button;
        public VHPreloader(View itemView) {
            super(itemView);
            background = (View) itemView.findViewById(R.id.bg);
            profile = (View) itemView.findViewById(R.id.profile);
            button = (View) itemView.findViewById(R.id.button);
            title = (View) itemView.findViewById(R.id.title);
            desc = (View) itemView.findViewById(R.id.details);

        }
    }
    class VHHeader extends RecyclerView.ViewHolder{
        TextView tvTitle;
        public VHHeader(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
        }
    }
    class VHFooter extends RecyclerView.ViewHolder {
        CircularProgressBar pv;
        public VHFooter(View itemView) {
            super(itemView);
            pv = (CircularProgressBar) itemView.findViewById(R.id.pv);
            setProgressIndicator(pv);
        }
    }
    public boolean isFromProfile() {
        return isFromProfile;
    }
    public CircularProgressBar getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(CircularProgressBar progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public void setFromProfile(boolean fromProfile) {
        isFromProfile = fromProfile;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof VHGoList){
            VHGoList itemHolder = (VHGoList) holder;
            itemHolder.goListName.setText("");
            itemHolder.placeNum.setText("");
            itemHolder.upvoteNum.setText("");
            Glide.with(mContext).
                    load(android.R.color.transparent).into(itemHolder.goListCoverPhoto);
        }
    }
}
