package currencycalculator.mastercard.currencycalc;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import currencycalculator.mastercard.currencycalc.helper.CacheHelper;
import currencycalculator.mastercard.currencycalc.pojo.Currency;
import currencycalculator.mastercard.currencycalc.retrofit.MasterCardCurrencyInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by budi on 6/10/17.
 */
public class ListCurrencyActivity extends BaseAppCompatActivity {

    RecyclerView recyclerView;
    List<Currency> currencies = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_currency_activity);
        populateAllCurrency();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_currency_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.v("test-search", String.format("Search for '%s'", newText));

                List<Currency> filter_currenciesList = new ArrayList<Currency>();
                for(Currency currency : currencies){
                    if (currency.code.toLowerCase().contains(newText.toLowerCase()) ||
                            currency.name.toLowerCase().contains(newText.toLowerCase())){
                        filter_currenciesList.add(currency);
                    }
                }



                ArrayList<Currency> itemList = new ArrayList<Currency>();
                itemList.addAll(filter_currenciesList);

                CurrencyItemArrayAdapter currencyItemArrayAdapter
                        = new CurrencyItemArrayAdapter(R.layout.currency_row_item_layout, itemList,ListCurrencyActivity.this);
                recyclerView.setAdapter(currencyItemArrayAdapter);

                return false;
            }
        });


        return true;
    }


    void populateAllCurrency(){

        MasterCardCurrencyInterface masterCardCurrencyInterface =
                retrofit.create(MasterCardCurrencyInterface.class);


        Call<List<Currency>> getAllCurrency = masterCardCurrencyInterface.getAllCurrency();

        getAllCurrency.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {

                String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(Calendar.getInstance().getTime());

                System.out.println("end retrofit-serializer:" + formattedDate);

                if(response.isSuccessful()){
                     currencies = response.body();


                    // Initializing list view with the custom adapter
                    ArrayList<Currency> itemList = new ArrayList<Currency>();
                    // Populating list items
                    itemList.addAll(currencies);

                    CurrencyItemArrayAdapter currencyItemArrayAdapter =
                            new CurrencyItemArrayAdapter(R.layout.currency_row_item_layout, itemList, ListCurrencyActivity.this);
                    recyclerView = (RecyclerView) findViewById(R.id.item_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListCurrencyActivity.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(currencyItemArrayAdapter);


                    CacheHelper cacheHelper = new CacheHelper(getApplicationContext());
                    cacheHelper.cacheCurrencyList(currencies);

                    System.out.println("Done Retrofit - successful");
                    //Toast.makeText(getApplicationContext(), "Done Retrofit - successful", Toast.LENGTH_LONG).show();
                }else{
                    System.out.println("Done Retrofit - failed");
                    //Toast.makeText(getApplicationContext(), "Done Retrofit - not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Failed Retrofit", Toast.LENGTH_LONG).show();
                System.out.println("Failed Retrofit");
            }
        });
    }
}
