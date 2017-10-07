package currencycalculator.mastercard.currencycalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.ArrayList;

import currencycalculator.mastercard.currencycalc.helper.ConfigHelper;
import currencycalculator.mastercard.currencycalc.pojo.Currency;

/**
 * Created by budi on 6/10/17.
 */
public class CurrencyItemArrayAdapter extends RecyclerView.Adapter<CurrencyItemArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<Currency> itemList;
    private Context context;
    // Constructor of the class
    public CurrencyItemArrayAdapter(int layoutId, ArrayList<Currency> itemList, Context context) {
        listItemLayout = layoutId;
        this.itemList = itemList;
        this.context= context;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(itemList.get(listPosition).code + " - " + itemList.get(listPosition).name);
        item.setTag(itemList.get(listPosition));
        ImageView countryFlag =  holder.countryFlag;

        Glide.with(context).load(ConfigHelper.BASE_URL +  itemList.get(listPosition).flagPath).into(countryFlag);
    }

    // Static inner class to initialize the views of rows
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public ImageView countryFlag;
        public RelativeLayout arrowRightLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item = (TextView) itemView.findViewById(R.id.row_item);
            countryFlag = (ImageView) itemView.findViewById(R.id.country_flag);
            arrowRightLayout = (RelativeLayout) itemView.findViewById(R.id.arrow_right_layout);

            arrowRightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
        @Override
        public void onClick(View view) {


            Intent submitIntent = new Intent();
            Currency currency = (Currency)item.getTag();
            submitIntent.putExtra("currency", Parcels.wrap(currency));

            ((Activity)context).setResult(Activity.RESULT_OK, submitIntent);
            ((Activity)context).finish();
            Log.v("test", ((Currency)item.getTag()).name);

            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }
    }
}