package currencycalculator.mastercard.currencycalc;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import currencycalculator.mastercard.currencycalc.helper.CacheHelper;
import currencycalculator.mastercard.currencycalc.helper.ConfigHelper;
import currencycalculator.mastercard.currencycalc.helper.SharedPreferencesHelper;
import currencycalculator.mastercard.currencycalc.pojo.Currency;
import currencycalculator.mastercard.currencycalc.pojo.ExchangeRate;
import currencycalculator.mastercard.currencycalc.retrofit.MasterCardCurrencyInterface;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseAppCompatActivity {
    static int CARD1 = 1;
    static int CARD2 = 2;
    static int CURRENCY1 = 3;
    static int CURRENCY2 = 4;

    SharedPreferencesHelper sharedPreferencesHelper = null;
    CacheHelper cacheHelper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        Init();

        final LinearLayout card1_layout = (LinearLayout) findViewById(R.id.card1_layout);
        LinearLayout currencySelection1 = (LinearLayout) card1_layout.findViewById(R.id.currency_option_layout);
        currencySelection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCurrencyActivity.class);
                startActivityForResult(intent,CURRENCY1);

                //if(BuildConfig.DEBUG){
                //    Toast.makeText(MainActivity.this, "card1-currency-selection",Toast.LENGTH_LONG).show();
               // }

            }
        });

        final TextView currency_display1 = (TextView) card1_layout.findViewById(R.id.currency_amount);
        currency_display1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditAmountActivity.class);
                intent.putExtra("card_type",CARD1);
                intent.putExtra("amount",currency_display1.getText().toString());
                startActivityForResult(intent,CARD1);
            }
        });



        LinearLayout card2_layout = (LinearLayout) findViewById(R.id.card2_layout);
        LinearLayout currencySelection2 = (LinearLayout) card2_layout.findViewById(R.id.currency_option_layout);
        currencySelection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListCurrencyActivity.class);
                startActivityForResult(intent,CURRENCY2);

               // if (BuildConfig.DEBUG)
                //    Toast.makeText(MainActivity.this, "card2-currency-selection",Toast.LENGTH_LONG).show();
            }
        });

        final TextView currency_display2 = (TextView) card2_layout.findViewById(R.id.currency_amount);
        currency_display2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditAmountActivity.class);
                intent.putExtra("card_type",CARD2);
                intent.putExtra("amount",currency_display2.getText().toString());
                startActivityForResult(intent,CARD2);
            }
        });

    }

    private void Init(){
        cacheHelper = new CacheHelper(getApplicationContext());

        if (cacheHelper.loadCacheFromCurrency() == null || cacheHelper.loadCacheToCurrency() == null){

            //init currency value
            sharedPreferencesHelper.setFromCurrencyAmount(ConfigHelper.CURRENCY_VALUE);
            //currency_display1.setText(Double.toString(sharedPreferencesHelper.getFromCurrencyAmount()));


            //init -> get Exchange rate -> update display
            InitCache(
                    new GetExchangeRateRunnable(
                            new UpdateDisplayRunnable(CURRENCY1)
                    ));
        }
        else{
            //get Exchange rate -> updateDisplay
            new GetExchangeRateRunnable(new UpdateDisplayRunnable(CURRENCY1)).run();
        }

    }



    class UpdateDisplayRunnable implements Runnable {
        int type ;
        public UpdateDisplayRunnable(int type){
            this.type = type;
        }

        public void run() {
            //fromCurrency
            Currency fromCurrency = cacheHelper.loadCacheFromCurrency();
            LinearLayout card1_layout = (LinearLayout) findViewById(R.id.card1_layout);

            TextView currencyCode1 = (TextView) card1_layout.findViewById(R.id.currencyCode);
            currencyCode1.setText(fromCurrency.code);

            TextView infoText1 = (TextView) card1_layout.findViewById(R.id.info_text);
            infoText1.setText(getString(R.string.i_have_str));

            ImageView currencyFlag1 = (ImageView) card1_layout.findViewById(R.id.currencyflag);
            Glide.with(MainActivity.this).load(ConfigHelper.BASE_URL +  fromCurrency.flagPath).into(currencyFlag1);



            //ToCurrency
            Currency toCurrency = cacheHelper.loadCacheToCurrency();
            LinearLayout card2_layout = (LinearLayout) findViewById(R.id.card2_layout);

            TextView currencyCode2 = (TextView) card2_layout.findViewById(R.id.currencyCode);
            currencyCode2.setText(toCurrency.code);


            TextView infoText2 = (TextView) card2_layout.findViewById(R.id.info_text);
            infoText2.setText(getString(R.string.i_want_str));

            ImageView currencyFlag2 = (ImageView) card2_layout.findViewById(R.id.currencyflag);
            Glide.with(MainActivity.this).load(ConfigHelper.BASE_URL +  toCurrency.flagPath).into(currencyFlag2);



            //ExchangeRate

            ExchangeRate exchangeRate1 = cacheHelper.loadCacheExchangeRate1();
            Log.v("budi-log", "BaseCode:" + exchangeRate1.baseCode +
                    ", TargetCode:" + exchangeRate1.targetCode +
                    ", Rate" + exchangeRate1.rate);



            TextView currency_desc1 = (TextView) card1_layout.findViewById(R.id.currency_desc);
            currency_desc1.setText(String.format("1 %s = %s %s", fromCurrency.code, exchangeRate1.rate.toString(), toCurrency.code));




            ExchangeRate exchangeRate2 = cacheHelper.loadCacheExchangeRate2();
            Log.v("budi-log", "BaseCode:" + exchangeRate2.baseCode +
                    ", TargetCode:" + exchangeRate2.targetCode +
                    ", Rate" + exchangeRate2.rate);


            TextView currency_desc2 = (TextView) card2_layout.findViewById(R.id.currency_desc);
            currency_desc2.setText(String.format("1 %s = %s %s", toCurrency.code, exchangeRate2.rate.toString(), toCurrency.code));




            Log.v("budi-log", "Base Currency:" + this.type);

            TextView currency_display1 = (TextView) card1_layout.findViewById(R.id.currency_amount);
            currency_display1.setText(Double.toString(sharedPreferencesHelper.getFromCurrencyAmount()));


            double currencyAmount1=0, currencyAmount2=0;
            TextView currency_amount1 = (TextView) card1_layout.findViewById(R.id.currency_amount);
            TextView currency_amount2 = (TextView) card2_layout.findViewById(R.id.currency_amount);

            if (type == CURRENCY1){
                currencyAmount1 = sharedPreferencesHelper.getFromCurrencyAmount();
                currencyAmount2 = currencyAmount1 * exchangeRate1.rate;

            }else if (type == CURRENCY2){

                currencyAmount2 = sharedPreferencesHelper.getToCurrencyAmount();
                currencyAmount1 = currencyAmount2 * exchangeRate2.rate;
            }else{
                Log.e("Budi-Log", "Something wrong with passing CURRENCY TYPE!");
            }


            //currency_amount1.setText(Double.toString(currencyAmount1));
            //currency_amount2.setText(Double.toString(currencyAmount2));

            currency_amount1.setText(formatMoney(currencyAmount1));
            currency_amount2.setText(formatMoney(currencyAmount2));



            //currency_amount
            //currency_display1.setText(Double.toString(sharedPreferencesHelper.getFromCurrencyAmount()));

            //currency_amount.setText(Integer.toString(ConfigHelper.CURRENCY_VALUE));
        }
    }


    class GetExchangeRateRunnable implements Runnable{
        UpdateDisplayRunnable updateDisplayRunnable;
        GetExchangeRateRunnable(UpdateDisplayRunnable updateDisplayRunnable) { this.updateDisplayRunnable = updateDisplayRunnable; }

        @Override
        public void run() {
            String toCurrencyCode = cacheHelper.loadCacheToCurrency().code;
            String fromCurrencyCode = cacheHelper.loadCacheFromCurrency().code;

            MasterCardCurrencyInterface masterCardCurrencyInterface =
                    retrofit.create(MasterCardCurrencyInterface.class);


            Call<ExchangeRate> getExchangeRate1 =
                    masterCardCurrencyInterface.getExchangeRate(fromCurrencyCode,toCurrencyCode);


            getExchangeRate1.enqueue(new Callback<ExchangeRate>() {
                @Override
                public void onResponse(Call<ExchangeRate> call, Response<ExchangeRate> response) {

                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(Calendar.getInstance().getTime());

                    System.out.println("end retrofit-serializer:" + formattedDate);

                    if(response.isSuccessful()){
                        ExchangeRate exchangeRate = response.body();
                        cacheHelper.cacheExchangeRate1(exchangeRate);


                        MasterCardCurrencyInterface masterCardCurrencyInterface =
                                retrofit.create(MasterCardCurrencyInterface.class);


                        Call<ExchangeRate> getExchangeRate2 =
                                masterCardCurrencyInterface.getExchangeRate(toCurrencyCode,fromCurrencyCode);
                        getExchangeRate2.enqueue(new Callback<ExchangeRate>() {
                            @Override
                            public void onResponse(Call<ExchangeRate> call, Response<ExchangeRate> response) {
                                if(response.isSuccessful()){
                                    ExchangeRate exchangeRate = response.body();
                                    cacheHelper.cacheExchangeRate2(exchangeRate);
                                    updateDisplayRunnable.run();
                                    Log.v("Budi-Log","Done getExchangeRate2 - successful");
                                }else{
                                    Log.v("Budi-Log","Done getExchangeRate2 - failed");
                                }

                            }

                            @Override
                            public void onFailure(Call<ExchangeRate> call, Throwable t) {
                                Log.v("Budi-Log","Failed getExchangeRate2");
                            }
                        });






                        Log.v("Budi-Log","Done getExchangeRate1 - successful");
                        //Toast.makeText(getApplicationContext(), "Done Retrofit - successful", Toast.LENGTH_LONG).show();
                    }else{
                        Log.v("Budi-Log","Done getExchangeRate1 - failed");
                        //Toast.makeText(getApplicationContext(), "Done Retrofit - not successful", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ExchangeRate> call, Throwable t) {
                    //Toast.makeText(getApplicationContext(), "Failed Retrofit", Toast.LENGTH_LONG).show();
                    Log.v("Budi-Log","Failed getExchangeRate1");
                }
            });

        }
    }

    private void InitCache(Runnable getExchangeRateRunnable){



        //get all currency
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
                    List<Currency> currencies = response.body();

                    CacheHelper cacheHelper = new CacheHelper(getApplicationContext());
                    cacheHelper.cacheCurrencyList(currencies);

                    System.out.println("Done Retrofit - successful");

                    //Currency fromCurrency = currencies.stream().filter(c -> c.code.equals(ConfigHelper.FROM_CURRENCY_CODE)).findFirst().get();
                    //cacheHelper.cacheFromCurrency(fromCurrency);
                    //Currency toCurrency = currencies.stream().filter(c -> c.code.equals(ConfigHelper.TO_CURRENCY_CODE)).findFirst().get();
                    //cacheHelper.cacheFromCurrency(toCurrency);


                    Currency fromCurrency = null,toCurrency=null;

                    for(Currency currency :currencies){
                        if(currency.code.equals(ConfigHelper.FROM_CURRENCY_CODE)) {
                            fromCurrency = currency;
                            break;
                        }
                    }

                    for(Currency currency :currencies){
                        if(currency.code.equals(ConfigHelper.TO_CURRENCY_CODE)) {
                            toCurrency=currency;
                            break;
                        }
                    }

                    if (fromCurrency==null) {
                        Log.e("Budi-log", "Something wrong in Config Setting - FROM_CURRENCY_CODE ");
                        finish();
                    }

                    if (toCurrency==null) {
                        Log.e("Budi-log", "Something wrong in Config Setting - TO_CURRENCY_CODE ");
                    }

                    cacheHelper.cacheToCurrency(toCurrency);
                    cacheHelper.cacheFromCurrency(fromCurrency);

                    getExchangeRateRunnable.run();

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



    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == CARD1){
            if(resultCode == Activity.RESULT_OK){
                LinearLayout card1_layout = (LinearLayout) findViewById(R.id.card1_layout);
                TextView currency_display1 = (TextView) card1_layout.findViewById(R.id.currency_amount);

                String amount = data.getStringExtra("amount");
                currency_display1.setText(amount);

                //save to sharePreference
                sharedPreferencesHelper.setFromCurrencyAmount(Double.parseDouble(amount));

                new UpdateDisplayRunnable(CURRENCY1).run();

                //if (BuildConfig.DEBUG)
                //    Toast.makeText(MainActivity.this,amount , Toast.LENGTH_LONG).show();
            }else{

                //if (BuildConfig.DEBUG)
                //    Toast.makeText(MainActivity.this,data.getStringExtra("result") , Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == CARD2){
            if(resultCode == Activity.RESULT_OK){
                LinearLayout card2_layout = (LinearLayout) findViewById(R.id.card2_layout);
                TextView currency_display2 = (TextView) card2_layout.findViewById(R.id.currency_amount);

                String amount = data.getStringExtra("amount");
                currency_display2.setText(amount);


                //save to sharePreference
                sharedPreferencesHelper.setToCurrencyAmount(Double.parseDouble(amount));
                new UpdateDisplayRunnable(CURRENCY2).run();

               // if (BuildConfig.DEBUG)
                //    Toast.makeText(MainActivity.this, data.getStringExtra("amount") , Toast.LENGTH_LONG).show();
            }else{

                //if (BuildConfig.DEBUG)
                 //   Toast.makeText(MainActivity.this,data.getStringExtra("result") , Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == CURRENCY1){
            if(resultCode == Activity.RESULT_OK){
                Currency currencyFrom = Parcels.unwrap(data.getParcelableExtra("currency"));
                cacheHelper.cacheFromCurrency(currencyFrom);

                ChangeCurrency(CURRENCY1);

                //if (BuildConfig.DEBUG)
                  //  Toast.makeText(MainActivity.this, "Currency1:" + currencyFrom.name , Toast.LENGTH_LONG).show();



                //update currency display
            }
        }else if (requestCode == CURRENCY2){
            if(resultCode == Activity.RESULT_OK){
                Currency currencyTo = Parcels.unwrap(data.getParcelableExtra("currency"));
                cacheHelper.cacheToCurrency(currencyTo);

                ChangeCurrency(CURRENCY1);

                //if (BuildConfig.DEBUG)
                //    Toast.makeText(MainActivity.this, "Currency2:" + currencyTo.name , Toast.LENGTH_LONG).show();
            }
        }
    }


    private void ChangeCurrency(int currencyType){
        new GetExchangeRateRunnable(new UpdateDisplayRunnable(currencyType)).run();


        /*
        //change card1
        LinearLayout card1_layout = (LinearLayout) findViewById(R.id.card1_layout);
        LinearLayout currencySelection1 = (LinearLayout) card1_layout.findViewById(R.id.currency_option_layout);

        ImageView currencyFlag1 = (ImageView) currencySelection1.findViewById(R.id.currencyflag);
        Glide.with(MainActivity.this).load(ConfigHelper.BASE_URL +  fromCurrency.flagPath).into(currencyFlag1);

        TextView currencyCode1 = (TextView) currencySelection1.findViewById(R.id.currencyCode);
        currencyCode1.setText(fromCurrency.code);

        //change card2
        LinearLayout card2_layout = (LinearLayout) findViewById(R.id.card2_layout);
        LinearLayout currencySelection2 = (LinearLayout) card2_layout.findViewById(R.id.currency_option_layout);

        ImageView currencyFlag2 = (ImageView) currencySelection2.findViewById(R.id.currencyflag);
        Glide.with(MainActivity.this).load(ConfigHelper.BASE_URL +  toCurrency.flagPath).into(currencyFlag2);


        TextView currencyCode2 = (TextView) currencySelection2.findViewById(R.id.currencyCode);
        currencyCode2.setText(toCurrency.code);
        */

    }

    @Override
    public void onStart(){
        super.onStart();


    }

    String formatMoney(double money){

        /*
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(money);

        if (moneyString.endsWith(".00")) {
            int centsIndex = moneyString.lastIndexOf(".00");
            if (centsIndex != -1) {
                moneyString = moneyString.substring(1, centsIndex);
            }
        }

        return moneyString;
        */

        String moneyString = Double.toString(money);
        if (moneyString.endsWith(".0"))
            moneyString = moneyString.replace(".0","");

        return moneyString;
    }

}
