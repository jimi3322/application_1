package com.app.common.view.areaPick;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.common.domain.AddressBean;
import com.app.common.domain.Constant;
import com.app.common.utils.UIUtil;
import com.app.wayee.common.R;

import java.util.ArrayList;
import java.util.List;

public class AreaPickerView extends Dialog {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView ivBtn;
    private AreaPickerViewCallback areaPickerViewCallback;
    /**
     * View的集合
     */
    private List<View> views;
    /**
     * tab的集合
     */
    private List<String> mTabName = new ArrayList<>();
    /**
     * 省
     */
    private List<AddressBean> addressBeans;
    /**
     * 市
     */
    private List<AddressBean> cityBeans;
    /**
     * 区
     */
    private List<AddressBean> areaBeans;
    private Context context;

    private ViewPagerAdapter viewPagerAdapter;
    private AddressAdapter provinceAdapter;
    private AddressAdapter cityAdapter;
    private AddressAdapter areaAdapter;

    /**
     * 选中的区域下标 默认-1
     */
    private int provinceSelected = -1;
    private int citySelected = -1;
    private int areaSelected = -1;

    /**
     * 历史选中的区域下标 默认-1
     */
    private int oldProvinceSelected = -1;
    private int oldCitySelected = -1;
    private int oldAreaSelected = -1;

    private RecyclerView areaRecyclerView;
    private RecyclerView cityRecyclerView;

    private boolean isCreate;

    public AreaPickerView(@NonNull Context context, int themeResId, List<AddressBean> addressBeans) {
        super(context, themeResId);
        this.addressBeans = addressBeans;
        this.context = context;
    }
    public AreaPickerView(@NonNull Context context) {
        super(context);
    }

    public AreaPickerView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AreaPickerView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_area_pickerview);
        Window window = this.getWindow();
        isCreate = true;
        /**
         * 位于底部
         */
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = UIUtil.INSTANCE.DipToPixels(Constant.MAX_BOTTOM_DIALOG_HEIGH);
        window.setAttributes(params);
        /**
         * 设置弹出动画
         */
        window.setWindowAnimations(R.style.PickerAnim);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        ivBtn = findViewById(R.id.iv_btn);
        ivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        View provinceView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recyclerview, null, false);
        View cityView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recyclerview, null, false);
        View areaView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recyclerview, null, false);

        final RecyclerView provinceRecyclerView = provinceView.findViewById(R.id.recyclerview);
        cityRecyclerView = cityView.findViewById(R.id.recyclerview);
        areaRecyclerView = areaView.findViewById(R.id.recyclerview);

        views = new ArrayList<>();
        views.add(provinceView);
        views.add(cityView);
        views.add(areaView);

        /**
         * 配置adapter
         */
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        /**
         * 这句话设置了过后，假如又3个tab 删除第三个 刷新过后 第二个划第三个会有弹性
         * viewPager.setOffscreenPageLimit(2);
         */

        provinceAdapter = new AddressAdapter( addressBeans);
        provinceRecyclerView.setAdapter(provinceAdapter);
        LinearLayoutManager provinceManager = new LinearLayoutManager(context);
        provinceRecyclerView.setLayoutManager(provinceManager);
        provinceAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void setOnclickItem( View view, int position) {
                cityBeans.clear();
                areaBeans.clear();
                addressBeans.get(position).setSelected(true);
                provinceSelected = position;
                if (oldProvinceSelected != -1 && oldProvinceSelected != provinceSelected) {
                    addressBeans.get(oldProvinceSelected).setSelected(false);
                }
                if ( position != oldProvinceSelected) {
                    if (oldCitySelected != -1) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).setSelected(false);
                    }
                    if (oldAreaSelected != -1) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).setSelected(false);
                    }
                    oldCitySelected = -1;
                    oldAreaSelected = -1;
                }
                cityBeans.addAll(addressBeans.get(position).getChildren());
                provinceAdapter.notifyDataSetChanged();
                cityAdapter.notifyDataSetChanged();
                areaAdapter.notifyDataSetChanged();
                mTabName.set(0, addressBeans.get(position).getTitle());
                if (mTabName.size() == 1) {
                    mTabName.add("请选择");
                } else if (mTabName.size() > 1) {
                    if (position != oldProvinceSelected) {
                        mTabName.set(1, "请选择");
                        if (mTabName.size() == 3) {
                            mTabName.remove(2);
                        }
                    }
                }
                tabLayout.setupWithViewPager(viewPager);
                viewPagerAdapter.notifyDataSetChanged();
                tabLayout.getTabAt(1).select();
                oldProvinceSelected = provinceSelected;
            }
        });

        cityBeans = new ArrayList<>();
        cityAdapter = new AddressAdapter( cityBeans);
        LinearLayoutManager cityListManager = new LinearLayoutManager(context);
        cityRecyclerView.setLayoutManager(cityListManager);
        cityRecyclerView.setAdapter(cityAdapter);
        cityAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void setOnclickItem( View view, int position) {
                areaBeans.clear();
                cityBeans.get(position).setSelected(true);
                citySelected = position;
                if (oldCitySelected != -1 && oldCitySelected != citySelected) {
                    addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).setSelected(false);
                }
                if (position != oldCitySelected) {
                    if (oldAreaSelected != -1 && cityBeans.get(position).getChildren() != null) {
                        addressBeans.get(oldProvinceSelected).getChildren().get(oldCitySelected).getChildren().get(oldAreaSelected).setSelected(false);
                    }
                    oldAreaSelected = -1;
                }
                oldCitySelected = citySelected;
                if (cityBeans.get(position).getChildren() != null) {
                    areaBeans.addAll(cityBeans.get(position).getChildren());
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    mTabName.set(1, cityBeans.get(position).getTitle());
                    if (mTabName.size() == 2) {
                        mTabName.add("请选择");
                    } else if (mTabName.size() == 3) {
                        mTabName.set(2, "请选择");
                    }
                    tabLayout.setupWithViewPager(viewPager);
                    viewPagerAdapter.notifyDataSetChanged();
                    tabLayout.getTabAt(2).select();
                } else {
                    oldAreaSelected = -1;
                    cityAdapter.notifyDataSetChanged();
                    areaAdapter.notifyDataSetChanged();
                    mTabName.set(1, cityBeans.get(position).getTitle());
                    tabLayout.setupWithViewPager(viewPager);
                    viewPagerAdapter.notifyDataSetChanged();
                    dismiss();
                    areaPickerViewCallback.callback(provinceSelected, citySelected);
                }
            }
        });

        areaBeans = new ArrayList<>();
        areaAdapter = new AddressAdapter( areaBeans);
        LinearLayoutManager areaListManager = new LinearLayoutManager(context);
        areaRecyclerView.setLayoutManager(areaListManager);
        areaRecyclerView.setAdapter(areaAdapter);
        areaAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void setOnclickItem( View view, int position) {
                mTabName.set(2, areaBeans.get(position).getTitle());
                tabLayout.setupWithViewPager(viewPager);
                viewPagerAdapter.notifyDataSetChanged();
                areaBeans.get(position).setSelected(true);
                areaSelected = position;
                if (oldAreaSelected != -1 && oldAreaSelected != position) {
                    areaBeans.get(oldAreaSelected).setSelected(false);
                }
                oldAreaSelected = areaSelected;
                areaAdapter.notifyDataSetChanged();
                dismiss();
                areaPickerViewCallback.callback(provinceSelected, citySelected, areaSelected);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        provinceRecyclerView.scrollToPosition(oldProvinceSelected == -1 ? 0 : oldProvinceSelected);
                        break;
                    case 1:
                        cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : oldCitySelected);
                        break;
                    case 2:
                        areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public interface AreaPickerViewCallback {
        void callback(int... value);
    }

    public void setAreaPickerViewCallback(AreaPickerViewCallback areaPickerViewCallback) {
        this.areaPickerViewCallback = areaPickerViewCallback;
    }
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTabName.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabName.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            Log.e("AreaPickerView", "------------instantiateItem");
            return views.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
            Log.e("AreaPickerView", "------------destroyItem");
        }

    }

    public void setSelect(int... value) {
        mTabName.clear();
        if (value == null || value.length ==0) {
            mTabName.add("请选择");
            if (isCreate) {
                tabLayout.setupWithViewPager(viewPager);
                viewPagerAdapter.notifyDataSetChanged();
                tabLayout.getTabAt(0).select();
                if (provinceSelected != -1)
                    addressBeans.get(provinceSelected).setSelected(false);
                if (citySelected != -1)
                    addressBeans.get(provinceSelected).getChildren().get(citySelected).setSelected(false);
                cityBeans.clear();
                areaBeans.clear();
                provinceAdapter.notifyDataSetChanged();
                cityAdapter.notifyDataSetChanged();
                areaAdapter.notifyDataSetChanged();
            }
            return;
        }
        AddressBean bean = addressBeans.get(value[0]);
        bean.setSelected(true);
        mTabName.add(bean.getTitle());
        for(int i = 1 ;i<value.length;i++){
            bean = bean.getChildren().get(value[i]);
            mTabName.add(bean.getTitle());
            bean.setSelected(true);
        }
        if (value.length == 3) {
            tabLayout.setupWithViewPager(viewPager);
            viewPagerAdapter.notifyDataSetChanged();
            tabLayout.getTabAt(value.length - 1).select();
            if (provinceSelected != -1)
                addressBeans.get(provinceSelected).setSelected(false);
            if (provinceSelected != -1&& citySelected != -1)
                addressBeans.get(provinceSelected).getChildren().get(citySelected).setSelected(false);

            if (provinceSelected != -1&& citySelected != -1 &&areaSelected != -1)
                addressBeans.get(provinceSelected).getChildren().get(citySelected).getChildren().get(areaSelected).setSelected(false);
            cityBeans.clear();
            cityBeans.addAll(addressBeans.get(value[0]).getChildren());
            areaBeans.clear();
            areaBeans.addAll(addressBeans.get(value[0]).getChildren().get(value[1]).getChildren());

            if (value[0] < addressBeans.size()) {
                addressBeans.get( value[0]).setSelected(true);
            }
            if (value[1] < cityBeans.size()) {
                cityBeans.get( value[1]).setSelected(true);
            }
            if (value[2] < areaBeans.size()) {
                areaBeans.get( value[2]).setSelected(true);
            }

            provinceAdapter.notifyDataSetChanged();
            cityAdapter.notifyDataSetChanged();
            areaAdapter.notifyDataSetChanged();
            oldProvinceSelected = value[0];
            oldCitySelected = value[1];
            oldAreaSelected = value[2];
            provinceSelected = value[0];
            citySelected = value[1];
            areaSelected = value[2];
            areaRecyclerView.scrollToPosition(oldAreaSelected == -1 ? 0 : oldAreaSelected);
        }

        if (value.length == 2) {
            tabLayout.setupWithViewPager(viewPager);
            viewPagerAdapter.notifyDataSetChanged();
            tabLayout.getTabAt(value.length - 1).select();
            if (provinceSelected != -1) {
                addressBeans.get(provinceSelected).setSelected(false);
            }
            if (citySelected != -1) {
                addressBeans.get(provinceSelected).getChildren().get(citySelected).setSelected(false);
            }
            cityBeans.clear();
            cityBeans.addAll(addressBeans.get(value[0]).getChildren());
            provinceAdapter.notifyDataSetChanged();
            cityAdapter.notifyDataSetChanged();
            oldProvinceSelected = value[0];
            oldCitySelected = value[1];
            oldAreaSelected = -1;
            cityRecyclerView.scrollToPosition(oldCitySelected == -1 ? 0 : oldCitySelected);
        }

    }
}
