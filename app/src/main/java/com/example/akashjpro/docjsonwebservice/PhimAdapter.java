package com.example.akashjpro.docjsonwebservice;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Akashjpro on 10/6/2016.
 */

public class PhimAdapter extends BaseAdapter {

    Activity context;
    int layout;
    ArrayList<Phim> arrayPhim;

    public PhimAdapter(Activity context, int layout, ArrayList<Phim> arrayPhim) {
        this.context = context;
        this.layout = layout;
        this.arrayPhim = arrayPhim;
    }

    @Override
    public int getCount() {
        return arrayPhim.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayPhim.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHoler{
        TextView tenPhim;
        TextView theLoai;
        TextView quocGia;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView = convertView;

        ViewHoler viewHoler = new ViewHoler();
        if(rowView==null){
            rowView = inflater.inflate(this.layout, null);
            viewHoler.tenPhim = (TextView) rowView.findViewById(R.id.txtTenPhim);
            viewHoler.theLoai = (TextView) rowView.findViewById(R.id.txtTheLoai);
            viewHoler.quocGia = (TextView) rowView.findViewById(R.id.txtQuocGia);
            rowView.setTag(viewHoler);
        }else {
            viewHoler = (ViewHoler) rowView.getTag();
        }

        viewHoler.tenPhim.setText(arrayPhim.get(position).getTenPhim());
        viewHoler.theLoai.setText(arrayPhim.get(position).getTheLoai());
        viewHoler.quocGia.setText(arrayPhim.get(position).getQuocGia());

        return rowView;
    }
}
