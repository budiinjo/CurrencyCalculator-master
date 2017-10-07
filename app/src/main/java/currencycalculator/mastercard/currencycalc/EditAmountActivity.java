package currencycalculator.mastercard.currencycalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by budi on 6/10/17.
 */
public class EditAmountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_amount_layout);

        TextView desc = (TextView) findViewById(R.id.desc);

        int card_type = getIntent().getIntExtra("card_type",0);

        if (card_type == MainActivity.CARD1)
            desc.setText(getString(R.string.i_have_amount_str));
        else if (card_type == MainActivity.CARD2)
            desc.setText(getString(R.string.i_want_to_change_for_str));
        else{
            Log.e("Message", "Something is wrong in passing card_type");
        }

        final EditText inputAmount = (EditText) findViewById(R.id.inputAmount);
        inputAmount.setText(getIntent().getStringExtra("amount"));
        if (inputAmount.requestFocus())
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Button submitButton = (Button) findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    double amt = Double.parseDouble(inputAmount.getText().toString());

                    if (amt <= 0)
                        throw new  Exception();

                    if (inputAmount.getText().toString().trim().isEmpty())
                        throw new Exception();



                    Intent submitIntent = new Intent();
                    submitIntent.putExtra("amount",inputAmount.getText().toString());
                    submitIntent.putExtra("result","ok");
                    setResult(Activity.RESULT_OK, submitIntent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        Button cancelButton = (Button)findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIntent = new Intent();
                submitIntent.putExtra("result","cancel");
                setResult(Activity.RESULT_CANCELED, submitIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {

            case KeyEvent.KEYCODE_ENTER:
                TextView desc = (TextView) findViewById(R.id.desc);
                desc.requestFocus();

                /*
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }*/

                return true;

            default:
                return super.onKeyUp(keyCode, event);
        }
    }

}
