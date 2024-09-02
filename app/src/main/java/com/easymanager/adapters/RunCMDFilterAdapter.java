package com.easymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class RunCMDFilterAdapter extends BaseAdapter implements Filterable {

    public RunCMDFilterAdapter(ArrayList<String> list, Context context) {
        this.list = (ArrayList<String>) list.clone();
        this.context = context;
        notifyDataSetChanged();
    }

    private ArrayList<String> list;
    private Context context;
    private Filter filter;


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int size = list.size();
        view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null);
        TextView text = view.findViewById(android.R.id.text1);
        if(i < size){
            text.setText(list.get(i));
        }
        
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            private ArrayList<String> filterList ;

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(filterList == null || filterList.size() < 1){
                    filterList = new ArrayList<>(list);
                }
                if(charSequence == null || charSequence.length() < 1){
                    results.values = filterList;
                    results.count = filterList.size();
                }else{
                    ArrayList<String> retList = new ArrayList<>();
                    String filterStr = charSequence.toString().toLowerCase(Locale.ROOT);
                    for (String s : filterList) {
                        if(s.indexOf(filterStr) != -1){
                            retList.add(s);
                        }
                    }
                    results.values = retList;
                    results.count = retList.size();
                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<String>) filterResults.values;
                if(list.size() > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
