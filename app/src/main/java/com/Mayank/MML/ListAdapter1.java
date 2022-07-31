package com.Mayank.MML;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter1 extends BaseAdapter {
    private final Context context;
    private final List<User> userList;
    private LayoutInflater inflater;
    MyDatabase db;

    public ListAdapter1(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void removeData(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final User user = userList.get(i);


        viewHolder.bindData(user);

        viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ItemDetails.class);
                intent.putExtra("name",user.getName());
                context.startActivity(intent);

            }
        });

        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData deleteData= new DeleteData(user.getName());
                deleteData.execute();
            }
        });


        return view;
    }

    /**
     * return the list of checked users
     * @return
     */
    public List<User> getSelectedUsers() {
        List<User> userList = new ArrayList<>();
        for (User user  :this.userList){
            if (user.isSelected){
                userList.add(user);
            }
        }
        return userList;
    }

    private static class ViewHolder {
        private TextView label;
        private Button del;

        public ViewHolder(View view) {
            label = view.findViewById(R.id.item_data);
            del= view.findViewById(R.id.bt_del);
        }

        void bindData(User user) {
            label.setText(user.getName());
        }
    }

    class DeleteData extends AsyncTask<String, String, String> {
        String name;
        public DeleteData(String name) {
            this.name = name;

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                db = MyApplication.getMyDatabase();
                db.userDao().deleteUser(name);
                db.employeeDao().deleteEmployee(name);
                db.itemsScannedDao().deleteItemsScanned(name);
            }
            catch (Exception e) {

            }

            return null;
        }

        protected void onPostExecute(String file_url){
            removeData();
        }


    }

}
