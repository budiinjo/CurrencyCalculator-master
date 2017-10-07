package currencycalculator.mastercard.currencycalc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import currencycalculator.mastercard.currencycalc.helper.ConfigHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by budi on 6/10/17.
 */
public class BaseAppCompatActivity extends AppCompatActivity {
    Retrofit retrofit;
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                //.baseUrl("http://localhost:8080/")
                .baseUrl(ConfigHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        httpClient.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        httpClient.addInterceptor(logging);  // <-- this is the important line!


        if (!isNetworkConnected())
            Toast.makeText(getApplicationContext(), "Your device is not connected to internet!", Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
