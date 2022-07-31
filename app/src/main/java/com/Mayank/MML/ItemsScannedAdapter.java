package com.Mayank.MML;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemsScannedAdapter extends BaseAdapter {
    private final Context context;
    private final List<ItemsScanned> itemsScannedList;
    private LayoutInflater inflater;

    public ItemsScannedAdapter(Context context, List<ItemsScanned> itemsScannedList) {
        this.context = context;
        this.itemsScannedList = itemsScannedList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemsScannedList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsScannedList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeData(int index){
        itemsScannedList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_scanned_details, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ItemsScanned itemsScanned = itemsScannedList.get(i);


        viewHolder.bindData(itemsScanned);

        return view;
    }


    private static class ViewHolder {
        private TextView itemName,ItemBags,ItemPou;

        public ViewHolder(View view) {
            itemName = view.findViewById(R.id.it_name);
            ItemBags = view.findViewById(R.id.it_bags);
            ItemPou = view.findViewById(R.id.it_pous);
        }

        void bindData(ItemsScanned itemsScanned) {
            itemName.setText(itemsScanned.getPr_code());
            ItemBags.setText(itemsScanned.getPr_bags());
            ItemPou.setText(itemsScanned.getPr_pouch());
        }
    }

}
