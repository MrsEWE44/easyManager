package com.easymanager.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easymanager.R;

public class HelpFragmentLayout extends Fragment {

    private Boolean isRoot,isADB;

    private ExpandableListView hflelv;

    public HelpFragmentLayout(){}

    public HelpFragmentLayout(Boolean isRoot, Boolean isADB) {
        this.isRoot = isRoot;
        this.isADB = isADB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.help_fragment_layout, container, false);
        hflelv = inflate.findViewById(R.id.hflelv);
        hflelv.setAdapter(adapter);


        return inflate;
    }



    //创建仓库，做最后的测试

    // 创建一个可展开的列表组件ExpandableListAdapter对象
    ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
        private String[] parn = new String[] { "easyManager是什么?", "它的优势是什么?","工作原理是什么?","它与shizuku的区别是什么?" };
        // 每个列表下面的子列表字符数组
        private String[] child = new String[]{
                "easyManager是一个轻量化、核心化、简单易用的安卓系统管理应用,它非常适合在原生系统上使用并接替系统权限管控,它还支持安卓4.4至安卓14的系统,而且还会继续更新下去,未来会添加更多的功能." ,
                "easyManager所提供的功能是支持安卓4.4至安卓14的版本,并且还支持开发接口共享,这意味着,你可以在基于该软件sdk开发工具包并且得到软件授权的情况下,直接调用并使用该应用的api接口,做一个或者多个,你认为更加理想的程序!并且它们是跨安卓版本的!最低支持安卓4.4,最高支持安卓14!可以省去多个安卓版本适配的问题!",
                "easyManager的工作原理参考shizhuku、黑域、appopsx,通过建立一个socket服务端,用来执行请求过来的操作,然后基于app_process命令在adb shell或者root shell服务中启动该socket服务端,用来过系统api接口身份验证,开发者就只需要创建一个相同的socket客户端,用来提交对应的请求操作,即可达到可以直接调用系统api接口的功能.服务端是必须要运行在adb shell服务以上才行.",
                "easyManager是参考shizuku源码实现的,在shizuku基础上,增加了对低版本安卓的支持,耦合了常用功能的代码,让开发者可以更加方便、快捷的调用它们,而shizuku考虑到效率跟维护周期问题,并没有继续维护安卓7以下的版本,开发文档也对新手并不友好,而easyManager会继续往更低版本的安卓进行探索,尽量让旧设备也能使用新功能或者让开发者更加方便的使用更多系统api接口."
        };

        @Override
        public boolean isChildSelectable(int groupPosition,
                                         int childPosition) {

            return true;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        // 构建每个父列表组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            View inflate = View.inflate(HelpFragmentLayout.this.getActivity(), R.layout.help_ex_item_parent_layout, null);
            TextView exparent = inflate.findViewById(R.id.exparent);
            exparent.setText(parn[groupPosition]);
            return inflate;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {

            return parn.length;
        }

        @Override
        public Object getGroup(int groupPosition) {

            return parn[groupPosition];
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        // 构建子列表的数据
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View inflate = null;
            if(convertView == null){
                inflate = View.inflate(HelpFragmentLayout.this.getActivity(), R.layout.help_ex_item_child_layout, null);
            }else {
                inflate = convertView;
            }
            Log.d("gccccc",groupPosition + " --- " + childPosition);
            TextView exparent = inflate.findViewById(R.id.exchild);
            exparent.setText(child[groupPosition]);
            return inflate;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child[childPosition];
        }
    };


}


