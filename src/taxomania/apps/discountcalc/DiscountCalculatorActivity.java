package taxomania.apps.discountcalc;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public final class DiscountCalculatorActivity extends Activity {
    private EditText price, disc;
    private TextView savings, total;
    private boolean[] decComp, decimal;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        price = (EditText) findViewById(R.id.price);
        disc = (EditText) findViewById(R.id.disc);
        savings = (TextView) findViewById(R.id.savings);
        total = (TextView) findViewById(R.id.total);
        decComp = new boolean[] { false, false };
        decimal = new boolean[] { false, false };

        price.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (v.getText().toString().equals("")) {
                        v.setText("0.00");
                    } else if (!decimal[0]) {
                        v.setText(v.getText().toString() + ".00");
                    } else if (!decComp[0]) {
                        v.setText(v.getText().toString() + "0");
                    } // else
                    disc.requestFocus();
                } // if
                return true;
            } // onEditorAction(TextView, int, KeyEvent)
        });

        disc.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (v.getText().toString().equals("")) {
                        v.setText("0");
                    } // if
                    calc(v);
                } // if
                return true;
            } // onEditorAction(TextView, int, KeyEvent)
        });

        price.addTextChangedListener(new DecimalWatcher(5, 0));
        disc.addTextChangedListener(new DecimalWatcher(3, 1));
    } // onCreate(Bundle)

    public void reset(final View view) {
        price.setText("");
        disc.setText("");
        savings.setText("");
        total.setText("");
    } // reset(View)

    public void calc(final View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        final String priceText = price.getText().toString();
        final String discText = disc.getText().toString();
        if ("".equals(priceText)) { return; }
        if ("".equals(discText)) { return; }
        final double priceVal = Double.parseDouble(priceText);
        final double discVal = Double.parseDouble(discText);
        final double discount = priceVal / 100.0 * discVal;
        final int di = (int) (discount * 100);
        final double totalVal = priceVal - (di / 100.0);
        final NumberFormat nf = NumberFormat.getCurrencyInstance();
        savings.setText(nf.format(discount));
        total.setText(nf.format(totalVal));
    } // calc(View)

    private final class DecimalWatcher implements TextWatcher {
        final int len, index;

        DecimalWatcher(final int len, final int index) {
            this.len = len;
            this.index = index;
        } // DecimalWatcher(int, int)

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before,
                final int count) {
        } // onTextChanged(CharSequence, int, int, int)

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count,
                final int after) {
        } // beforeTextChanged(CharSequence, int, int, int)

        @Override
        public void afterTextChanged(final Editable s) {
            String str = s.toString();
            if (str.contains(".")) {
                decimal[index] = true;
            } else {
                decimal[index] = false;
            } // else
            if (!decimal[index]) {
                if (len == str.length()) {
                    if (str.charAt(str.length() - 1) == '.') { return; }
                    final String st = str.substring(0, str.length() - 1);
                    s.clear();
                    s.append(st);
                } // if
            } else {
                String a = str.substring(str.indexOf(".") + 1);
                if (a.contains(".")) {
                    a.replace(".", "");
                    str = str.substring(0, str.length() - 1);
                    str.concat(a);
                    s.clear();
                    s.append(str);
                } // if
                if (2 != a.length()) {
                    decComp[index] = false;
                } // if
                if (3 == a.length()) {
                    a = a.substring(0, 2);
                    str = str.substring(0, str.length() - 1);
                    str.concat(a);
                    s.clear();
                    s.append(str);
                    decComp[index] = true;
                } // if
                if ((len + 3) == str.length()) {
                    final String st = str.substring(0, str.length() - 1);
                    s.clear();
                    s.append(st);
                } // if
            } // else
        } // afterTextChanged(Editable)
    } // class DecimalWatcher
} // class DiscountCalculatorActivity