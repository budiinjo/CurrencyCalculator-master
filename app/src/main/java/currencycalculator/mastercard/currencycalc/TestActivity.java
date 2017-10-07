package currencycalculator.mastercard.currencycalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import currencycalculator.mastercard.currencycalc.pojo.Currency;
import currencycalculator.mastercard.currencycalc.pojo.ExchangeRate;
import currencycalculator.mastercard.currencycalc.retrofit.MasterCardCurrencyInterface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by budi on 6/10/17.
 */
public class TestActivity  extends AppCompatActivity {
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        httpClient.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        httpClient.addInterceptor(logging);  // <-- this is the important line!



        Button btnExchangeRate = (Button) findViewById(R.id.btnGetExchangeRate);
        btnExchangeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        //.baseUrl("http://localhost:8080/")
                        .baseUrl("http://10.0.2.2:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();


                MasterCardCurrencyInterface masterCardCurrencyInterface =
                        retrofit.create(MasterCardCurrencyInterface.class);


                Call<ExchangeRate> getExchangeRate =
                        masterCardCurrencyInterface.getExchangeRate("JPY","INR");


                getExchangeRate.enqueue(new Callback<ExchangeRate>() {
                    @Override
                    public void onResponse(Call<ExchangeRate> call, Response<ExchangeRate> response) {

                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(Calendar.getInstance().getTime());

                        System.out.println("end retrofit-serializer:" + formattedDate);

                        if(response.isSuccessful()){
                            ExchangeRate exchangeRate = response.body();
                            System.out.println("Done Retrofit - successful");
                            //Toast.makeText(getApplicationContext(), "Done Retrofit - successful", Toast.LENGTH_LONG).show();
                        }else{
                            System.out.println("Done Retrofit - failed");
                            //Toast.makeText(getApplicationContext(), "Done Retrofit - not successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ExchangeRate> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(), "Failed Retrofit", Toast.LENGTH_LONG).show();
                        System.out.println("Failed Retrofit");
                    }
                });


            }
        });

        Button btnRun = (Button) findViewById(R.id.btnRun);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        //.baseUrl("http://localhost:8080/")
                        .baseUrl("http://10.0.2.2:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();


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
        });
    }
}
