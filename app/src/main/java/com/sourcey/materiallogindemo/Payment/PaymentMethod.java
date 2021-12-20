package com.sourcey.materiallogindemo.Payment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import com.sourcey.materiallogindemo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;

public class PaymentMethod extends AppCompatActivity implements OnClickListener {

    private void setRightDrawable(int imageID){
        Drawable dr=getResources().getDrawable(imageID);
        Bitmap bitmap=((BitmapDrawable)dr).getBitmap();
        Drawable dr1=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,25,25,true));
        creaditNumberText.setCompoundDrawablesWithIntrinsicBounds(null,null,dr1,null);
    }
    TextView creaditNumberText;
    ImageView finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        final ArrayList<String> listOfPattern=new ArrayList<String>();
        finishButton=findViewById(R.id.finish);
        finishButton.setOnClickListener(this);
        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        creaditNumberText=findViewById(R.id.creaditNumberText);
        creaditNumberText.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
                String ccNum = s.toString();
                if(!ccNum.equals(""))
                    if (ccNum.startsWith("4"))
                        setRightDrawable(R.drawable.vis);
                      //  System.out.println("Visa");
                    else if (ccNum.startsWith("5"))
                        setRightDrawable(R.drawable.mastercard);
                        //System.out.println("MasterCard");
                    else
                        setRightDrawable(R.drawable.paypal);
                //System.out.println("PayPal");
                else
                    creaditNumberText.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

            }
            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }
            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                finish();
                break;
        }
    }
}
