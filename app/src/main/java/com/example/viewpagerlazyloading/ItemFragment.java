package com.example.viewpagerlazyloading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ItemFragment extends Fragment
{
    private static final String FRAGMENT_POSITION = "position";

    private int[] mDrawableList = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};

    private ImageView imageView;
    //判断fragment是否可见，是否需要加载
    private boolean isViewVisiable;
    //判断数据是否已经初始化
    private boolean isDataInitialed;
    //判断视图是否已经初始化
    private boolean isViewInitialed;

    public static ItemFragment newInstance(int position)
    {
        ItemFragment fragment = new ItemFragment();
        Bundle arg = new Bundle();
        arg.putInt(FRAGMENT_POSITION,position);

        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.item_fragment_layout,container,false);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        if(isDataInitialed)
        {
            int position = getArguments().getInt(FRAGMENT_POSITION);
            imageView.setImageResource(mDrawableList[position]);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        /*一定要有此标志位，如果没有这个标志位，在setUserVisiableHint中进行的加载会报错，
        *因为第一次加载执行setUserVisiableHint函数时，fragment中的组件并未全部初始化
        *会报空指针
        */
        isViewInitialed = true;
        addImage(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        isViewVisiable = isVisibleToUser;
        //如果在此处不进行网络请求操作，则fragment中的内容无法被正常加载
        addImage(false);
    }

    private void addImage(boolean forceUpdate)
    {
        if(isViewVisiable && isViewInitialed && (!isDataInitialed||forceUpdate))
        {
            //此Handler在于模拟网络请求和图片加载的耗时过程
            Handler loadImage = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    super.handleMessage(msg);
                    int what = msg.what;
                    switch (what)
                    {
                        case 0:
                            int position = getArguments().getInt(FRAGMENT_POSITION);
                            imageView.setImageResource(mDrawableList[position]);
                            isDataInitialed = true;
                            break;
                    }
                }
            };
            loadImage.sendEmptyMessageDelayed(0,2000);
        }
    }
}
