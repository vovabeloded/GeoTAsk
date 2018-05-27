package ua.beloded.geotask.Adapter;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ua.beloded.geotask.R;

public class CityListAdapteer extends BaseAdapter {

    private List<Address> addresses;
    private Context context;

    public CityListAdapteer(List<Address> addresses, Context context) {
        this.addresses = addresses;
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int i) {
        return addresses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_layout, null, false);
            newView = view;
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.cityName = newView.findViewById(R.id.cityName);
            viewHolder.cityDescription = newView.findViewById(R.id.cityDescription);
            view.setTag(viewHolder);
        }


        viewHolder.cityName.setText(addresses.get(i).getFeatureName());
        viewHolder.cityDescription.setText(addresses.get(i).getCountryName() + ", " + addresses.get(i).getAdminArea());

        return view;
    }


    class ViewHolder {
        TextView cityName;
        TextView cityDescription;
    }
}
