package currencycalculator.mastercard.currencycalc;

import android.support.v7.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import currencycalculator.mastercard.currencycalc.helper.ConfigHelper;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by budi on 7/10/17.
 */
public class ApiCallTest {
    Retrofit retrofit;
    MasterCardCurrencyInterface masterCardCurrencyInterface;
    OkHttpClient.Builder httpClient ;

    @Before
    public void beforeTest(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        httpClient =  new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        masterCardCurrencyInterface =
                retrofit.create(MasterCardCurrencyInterface.class);

    }


    @Test
    public void getAllCurrencyTest(){

        Call<List<Currency>> getAllCurrency = masterCardCurrencyInterface.getAllCurrency();
        try{
            Response<List<Currency>> response = getAllCurrency.execute();
            List<Currency> currencies = response.body();
            assertNotEquals(0, currencies.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getExchangeRateTest(){

        MasterCardCurrencyInterface masterCardCurrencyInterface =
                retrofit.create(MasterCardCurrencyInterface.class);

        Call<ExchangeRate> getExchangeRate = masterCardCurrencyInterface.getExchangeRate("JPY","INR");
        try{
            Response<ExchangeRate> response = getExchangeRate.execute();
            ExchangeRate exchangeRate = response.body();
            assertNotEquals(null,exchangeRate);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
