package com.easymanager.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.easymanager.R;
import com.easymanager.utils.TextUtils;

public class HelpFragmentLayout extends Fragment {

    private Boolean isRoot, isADB;

    private Context context;
    private ExpandableListView hflelv;
    private TextUtils tvvv = new TextUtils();

    public HelpFragmentLayout() {
    }

    public HelpFragmentLayout(Boolean isRoot, Boolean isADB) {
        this.isRoot = isRoot;
        this.isADB = isADB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View inflate = inflater.inflate(R.layout.help_fragment_layout, container, false);
        hflelv = inflate.findViewById(R.id.hflelv);
        ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
            private String[] parn = new String[]{getLanStr(R.string.help_question_1), getLanStr(R.string.help_question_2), getLanStr(R.string.help_question_3), getLanStr(R.string.help_question_4)};
            // 每个列表下面的子列表字符数组
            private String[] child = new String[]{getLanStr(R.string.help_question_1_reply), getLanStr(R.string.help_question_2_reply), getLanStr(R.string.help_question_3_reply), getLanStr(R.string.help_question_4_reply)};

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            // 构建每个父列表组件
            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
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
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                View inflate = null;
                if (convertView == null) {
                    inflate = View.inflate(HelpFragmentLayout.this.getActivity(), R.layout.help_ex_item_child_layout, null);
                } else {
                    inflate = convertView;
                }
                Log.d("gccccc", groupPosition + " --- " + childPosition);
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

        hflelv.setAdapter(adapter);
        return inflate;
    }

    private String getLanStr(int id) {
        return tvvv.getLanguageString(context, id);
    }


}


