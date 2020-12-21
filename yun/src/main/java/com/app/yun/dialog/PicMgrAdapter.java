package com.app.yun.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.yun.R;
import com.wayeal.thirdpartyoperation.view.Pic;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class PicMgrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String uesrName=null;
    private ArrayList<Pic> mList;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int MAX_COUNT = 3;
    private int maxCount = MAX_COUNT;
    private PicClickListener mPicClickListener;

    public static final int TYPE_PIC = 1001;
    public static final int TYPE_PIC_ADD = 1002;

    private int mItemHeight;
    private int mItemWidth;
    private float mProportion = 1.0f;

    private ValueAnimator emptyAnimator;

    private EmptyAnimatorListener mEmptyAnimatorListener;
    private boolean mLimit = false;                             //是否限制模式  比如浏览模式，不显示x号 和+号

    public PicMgrAdapter(@NonNull Context context, int itemHeight) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemHeight = itemHeight;
        mItemWidth = (int) (itemHeight * mProportion);
        emptyAnimator = new ValueAnimator();
    }

    /**
     * 设置item比例
     *
     * @param proportion 高度为1，宽度为高度的proportion倍
     */
    public void setProportion(float proportion) {
        mProportion = proportion;
        mItemWidth = (int) (mItemHeight * mProportion);
        notifyDataSetChanged();
    }

    public void setMaxCount(@IntRange(from = 1, to = Integer.MAX_VALUE) int maxCount) {
        this.maxCount = maxCount;
    }

    public void setList(ArrayList<Pic> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public ArrayList<Pic> getList() {
        return mList;
    }

    public void removeItem(int pos) {
        if (mList == null) {
            return;
        }
        if (pos < 0 || pos > mList.size()) {
            return;
        }
        mList.remove(pos);
        notifyItemRemoved(pos);
        mPicClickListener.onDeletePic(pos);
    }

    public void removeItemFromDrag(int pos) {
        if (mList == null) {
            return;
        }
        if (pos < 0 || pos > mList.size()) {
            return;
        }
        mList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount() - pos, "payload");
    }

    public void addItem(Pic pic) {
        if (mList == null) {
            return;
        }
        mList.add(pic);
        //notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null) {
            return TYPE_PIC_ADD;
        }
        int count = mList.size();
        if (count >= maxCount) {
            return TYPE_PIC;
        } else {
            if (position == count) {//-1+1
                return TYPE_PIC_ADD;
            } else {
                return TYPE_PIC;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 1;
        }
        int count = mList.size();
        if (count >= maxCount) {
            count = maxCount;
        } else {
            count = count + 1;
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PIC) {
            View view = mInflater.inflate(R.layout.item_pic, parent, false);
            return new PicViewHolder(view);
        } else if (viewType == TYPE_PIC_ADD) {
            View view = mInflater.inflate(R.layout.item_pic_add, parent, false);
            return new PicAddViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            int viewType = holder.getItemViewType();
            if (viewType == TYPE_PIC) {
                PicViewHolder picHolder = (PicViewHolder) holder;
                picHolder.itemView.setVisibility(View.VISIBLE);
            } else if (viewType == TYPE_PIC_ADD) {
                onBindViewHolder(holder, position);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        int viewType = holder.getItemViewType();
        if (viewType == TYPE_PIC) {
            Pic pic = mList.get(position);
            PicViewHolder picHolder = (PicViewHolder) holder;
            picHolder.itemView.getLayoutParams().height = mItemHeight;
            picHolder.itemView.getLayoutParams().width = mItemWidth;
            picHolder.itemView.setVisibility(View.VISIBLE);
            if (!mLimit){
                picHolder.edit.setVisibility(View.VISIBLE);
            }
            //TODO 这里测试直接使用bitmap
            picHolder.pic.setImageBitmap(pic.getBitmap());

        } else if (viewType == TYPE_PIC_ADD) {
            if (mLimit){
                PicAddViewHolder picAddHolder = (PicAddViewHolder) holder;
                picAddHolder.itemView.getLayoutParams().height = 0;
                picAddHolder.itemView.getLayoutParams().width = 0;
            }else {
                PicAddViewHolder picAddHolder = (PicAddViewHolder) holder;
                picAddHolder.itemView.getLayoutParams().height = mItemHeight;
                picAddHolder.itemView.getLayoutParams().width = mItemWidth;
            }

//            if(getItemCount() >= maxCount + 1){
//                picAddHolder.itemView.setVisibility(View.GONE);
//            }else {
//                picAddHolder.itemView.setVisibility(View.VISIBLE);
//            }
        }

    }

    public class PicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView edit;
        private ImageView pic;

        public PicViewHolder(View itemView) {
            super(itemView);
            edit = (ImageView) itemView.findViewById(R.id.img_edit);
            pic = (ImageView) itemView.findViewById(R.id.img_pic);
            itemView.setOnClickListener(this);

            if (mLimit){
                edit.setVisibility(View.GONE);
            }
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (v == itemView) {
                if (mPicClickListener != null) {
                    mPicClickListener.onPicClick(v, pos);
                }
            } else if (v == edit) {
                if (!mLimit){
                    removeItem(pos);
                }

            }
        }

    }

    public class PicAddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt;

        public PicAddViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mPicClickListener != null) {
                if (!mLimit){
                    mPicClickListener.onAddClick(v);
                }
            }
        }
    }

    public interface PicClickListener {
        void onPicClick(View view, int pos);

        void onAddClick(View view);

        void onDeletePic(int pos);
    }

    public void setPicClickListener(PicClickListener listener) {
        mPicClickListener = listener;
    }

    private PicAddViewHolder getPicAddViewHolder(@NonNull RecyclerView recyclerView) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(getItemCount() - 1);
        if (viewHolder == null) {
            return null;
        }
        if (viewHolder instanceof PicAddViewHolder) {
            return (PicAddViewHolder) viewHolder;
        } else {
            return null;
        }
    }

    public void startEmptyAnimator(@NonNull RecyclerView recyclerView, long duration, float... values) {
        if (getItemCount() != 1) {
            return;
        }
        if (emptyAnimator.isRunning()) {
            return;
        }
        PicAddViewHolder picAddViewHolder = getPicAddViewHolder(recyclerView);
        if (picAddViewHolder == null) {
            return;
        }
        emptyAnimator.removeAllUpdateListeners();
        emptyAnimator.setFloatValues(values);
        emptyAnimator.setDuration(duration);

        emptyAnimator.addUpdateListener(new EmptyAnimatorUpdateListener(picAddViewHolder));
        emptyAnimator.start();
    }


    class EmptyAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private WeakReference<PicAddViewHolder> reference;

        public EmptyAnimatorUpdateListener(PicAddViewHolder picAddViewHolder) {
            reference = new WeakReference<>(picAddViewHolder);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (mEmptyAnimatorListener != null) {
                PicAddViewHolder picAddViewHolder = reference.get();
                if (picAddViewHolder != null) {
                    mEmptyAnimatorListener.onAnimationUpdate(animation, picAddViewHolder);
                }
            }
        }
    }

    public void setEmptyAnimatorListener(EmptyAnimatorListener listener) {
        mEmptyAnimatorListener = listener;
    }

    public void setLimitMode(boolean isLimit){
        mLimit = isLimit;
    }

    public interface EmptyAnimatorListener {
        void onAnimationUpdate(ValueAnimator animation, PicAddViewHolder holder);
    }

}
