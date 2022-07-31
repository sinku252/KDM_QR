package com.Mayank.MML;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EmployeeListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Employee> employeeList;
    private LayoutInflater inflater;

    public EmployeeListAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int i) {
        return employeeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeData(int index){
        employeeList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_details, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Employee employee = employeeList.get(i);


        viewHolder.bindData(employee);

        return view;
    }


    private static class ViewHolder {
        private TextView itemName,ItemBags,ItemPou;

        public ViewHolder(View view) {
            itemName = view.findViewById(R.id.it_name);
            ItemBags = view.findViewById(R.id.it_bags);
            ItemPou = view.findViewById(R.id.it_pous);
        }

        void bindData(Employee employee) {
            itemName.setText(employee.getPr_code());
            ItemBags.setText(employee.getPr_bags());
            ItemPou.setText(employee.getPr_pouch());
        }
    }

}
