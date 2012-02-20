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

public class DiscountCalculatorActivity extends Activity {
	private EditText price, disc;
	private TextView savings, total;
	private static boolean[] decComp, decimal;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		price = (EditText) findViewById(R.id.price);
		disc = (EditText) findViewById(R.id.disc);
		savings = (TextView) findViewById(R.id.savings);
		total = (TextView) findViewById(R.id.total);
		decComp = new boolean[2];
		decimal = new boolean[2];
		setListeners(price, true, 5, 0);
		setListeners(disc, false, 3, 1);
	}

	public void reset(View view) {
		price.setText("");
		disc.setText("");
		savings.setText("");
		total.setText("");
	}

	public void calc(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		if (price.getText().toString().equals(""))
			return;
		if (disc.getText().toString().equals(""))
			return;
		double priceVal = Double.parseDouble(price.getText().toString());
		double discVal = Double.parseDouble(disc.getText().toString());
		double discount = priceVal / 100.0 * discVal;
		int di = (int) (discount * 100);
		double totalVal = priceVal - (di / 100.0);
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		savings.setText(nf.format(discount));
		total.setText(nf.format(totalVal));
	}

	private void setListeners(EditText et, final boolean doesEdit,
			final int len, final int index) {
		TextView.OnEditorActionListener editListener = new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_NULL) {
					if (doesEdit) {
						if (v.getText().toString().equals(""))
							v.setText("0.00");
						else if (!decimal[index])
							v.setText(v.getText().toString() + ".00");
						else if (!decComp[index])
							v.setText(v.getText().toString() + "0");
						disc.requestFocus();
					} else {
						if (v.getText().toString().equals(""))
							v.setText("0");
						calc(v);
					}
				}
				return true;
			}
		};
		et.setOnEditorActionListener(editListener);
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.contains("."))
					decimal[index] = true;
				else
					decimal[index] = false;
				if (!decimal[index]) {
					if (str.length() == len) {
						if (str.charAt(str.length() - 1) == '.')
							return;
						String st = str.substring(0, str.length() - 1);
						s.clear();
						s.append(st);
					}
				} else {
					String a = str.substring(str.indexOf(".") + 1);
					if (a.contains(".")) {
						a.replace(".", "");
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
					}
					if (a.length() != 2)
						decComp[index] = false;
					if (a.length() == 3) {
						a = a.substring(0, 2);
						str = str.substring(0, str.length() - 1);
						str.concat(a);
						s.clear();
						s.append(str);
						decComp[index] = true;
					}
					if (str.length() == (len + 3)) {
						String st = str.substring(0, str.length() - 1);
						s.clear();
						s.append(st);
					}
				}
			}
		};
		et.addTextChangedListener(watcher);
	}

}