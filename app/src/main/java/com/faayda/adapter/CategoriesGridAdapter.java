package com.faayda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faayda.BaseActivity;
import com.faayda.R;
import com.faayda.database.DBConstants;
import com.faayda.imageloader.Loader;
import com.faayda.models.CategoryGridModel;
import com.faayda.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by vinove on 8/6/2015.
 */
public final class CategoriesGridAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    public ArrayList<CategoryGridModel> categoryModelArrayList;
    Loader lodimg;

    public CategoriesGridAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        categoryModelArrayList = new ArrayList<>();
        lodimg = new Loader(mContext);
    }

    public void add(CategoryGridModel gridModel) {
        categoryModelArrayList.add(gridModel);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.catagory_row, parent, false);
        viewHolder.categoryImages = (ImageView) convertView.findViewById(R.id.iv_catagory);
        viewHolder.category_name = (TextView) convertView.findViewById(R.id.tv_catagory);
        viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.categoryRowLinear);
        CategoryGridModel model = categoryModelArrayList.get(position);

        if (CommonUtils.getCategoryIcon(model.getCategoryTitle()) == -1) {
            //   lodimg.displayImage(model.getCategoryImageString(), viewHolder.categoryImages);

            try {
                lodimg.displayImage(((BaseActivity) mContext).dbHelper.getValue(DBConstants.CATEGORIES, DBConstants.CATEGORY_IMAGE,
                        DBConstants.CATEGORY_TITLE + " = '" + model.getCategoryTitle() + "'"), viewHolder.categoryImages,R.drawable.medical);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            viewHolder.categoryImages.setImageResource(CommonUtils.getCategoryIcon(model.getCategoryTitle()));
        }


        if (categoryModelArrayList.get(position).getIsSelected())
            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        else
            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        viewHolder.category_name.setText(model.getCategoryTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView categoryImages;
        TextView category_name;
        LinearLayout linearLayout;
    }
}
