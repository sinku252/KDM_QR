package com.Mayank.MML;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class QrDetailAdapterDet extends ArrayAdapter<QrBeanModelDet> {
    List<QrBeanModelDet> QrObject;
//    private ArrayList<QrBeanModel> privatearray;
    public QrDetailAdapterDet(Context context, int resource, List<QrBeanModelDet> objects) {
        super(context, resource, objects);
        this.QrObject = objects;
    }

    /*public void remove(int position) {
        QrBeanModelDet qrBeanModelDet = getItem(position);
        qrBeanModelDet.getQrText();
        QrObject.remove(position);
        //Toast.makeText(getContext(),"Clicked  " + qrBeanModel.getSpecDate(),Toast.LENGTH_LONG).show();
        MainActivity.sendUniqueKey(qrBeanModelDet.getQrText());
        notifyDataSetChanged();
    }*/

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.qr_challan_items,parent,false);
        TextView qrText = convertView.findViewById(R.id.qrText);
        TextView qrDate = convertView.findViewById(R.id.qrDate);
        QrBeanModelDet qrBeanModelDet = getItem(position);
        qrText.setText(qrBeanModelDet.getQrText());
        //qrDate.setText(qrBeanModelDet.getDate());
        return convertView;
    }

//    // Filter Class
//    public void filter(String charText) {
//
//        charText = charText.toLowerCase(Locale.getDefault());
//        QrObject.clear();
//        if(charText.length()==0){
//            privatearray.addAll(QrObject);
//        }
//        else{
//            for (QrBeanModel c : privatearray) {
//                if (c.getQrText().toLowerCase(Locale.getDefault())
//                        .contains(charText)) {
//                    QrObject.add(c);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
