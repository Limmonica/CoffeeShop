package com.example.android.justjava2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */

public class MainActivity extends AppCompatActivity {

    // Saves the quantity in case of changing activity
    static final String QUANTITY_SELECTED = "quantity";
    // Initialize the coffee quantity
    int quantity = 1;

    /**
     * This method hides the keyboard when clicking outside the EditText area
     * Needs the parent view to be made clickable and focusable
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current quantity state
        savedInstanceState.putInt(QUANTITY_SELECTED, quantity);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the quantity from the saved instance
        quantity = savedInstanceState.getInt(QUANTITY_SELECTED);
        displayQuantity(quantity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Hide the keyboard when clicking outside the EditText area
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null
                && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText
                && !v.getClass().getName().startsWith("android.webkit.")) {
            int screenCoordinates[] = new int[2];
            v.getLocationOnScreen(screenCoordinates);
            float x = ev.getRawX() + v.getLeft() - screenCoordinates[0];
            float y = ev.getRawY() + v.getTop() - screenCoordinates[1];
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        // When the quantity arrives at 100, exit the method and show a Toast error message
        if (quantity == 100) {
            // Show a Toast error message
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.error_toast_100);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            // Exit this method early because there's nothing left to do
            return;
        }
        // When + button is pressed, increment the quantity of coffees
        quantity++;
        // Update the quantity based on button being pressed
        displayQuantity(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        // When the quantity arrives to 1, exit the method and show a Toast error message
        if (quantity == 1) {
            // Show a Toast error message
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.error_toast_1);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            // Exit this method early because there's nothing left to do
            return;
        }
        // When - button is pressed, decrement the quantity of coffees
        quantity--;
        // Update the quantity based on button being pressed
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfCoffees));
    }

    /**
     * Calculates the price of the order.
     *
     * @param addCaramel      is whether or not the user wants caramel topping
     * @param addCinnamon     is whether or not the user wants cinnamon topping
     * @param addChocolate    is whether or not the user wants chocolate topping
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @return total price
     */
    private int calculatePrice(boolean addCaramel, boolean addCinnamon, boolean addWhippedCream, boolean addChocolate) {
        // Base price of the coffee
        int pricePerCup = 5;
        // Price of the topping: Caramel
        int priceCaramel = 1;
        // Price of the topping: Cinnamon
        int priceCinnamon = 1;
        // Price of the topping: Whipped Cream
        int priceWhippedCream = 1;
        // Price of the topping: Chocolate
        int priceChocolate = 2;
        // Add 1$ if the user wants Caramel
        if (addCaramel) {
            pricePerCup = pricePerCup + priceCaramel;
        }
        // Add 1$ if the user wants Cinnamon
        if (addCinnamon) {
            pricePerCup = pricePerCup + priceCinnamon;
        }
        // Add 1$ if the user wants Whipped Cream
        if (addWhippedCream) {
            pricePerCup = pricePerCup + priceWhippedCream;
        }
        // Add 2$ if the user wants Chocolate
        if (addChocolate) {
            pricePerCup = pricePerCup + priceChocolate;
        }
        // Return total order price by multiplying by quantity
        return quantity * pricePerCup;
    }

    /**
     * Create a summary of the order.
     *
     * @param price           of the order
     * @param addSugar        is whether it has or not sugar
     * @param addMilk         is whether it has or not milk
     * @param addCaramel      is whether it has or not caramel
     * @param addCinnamon     is whether it has or not cinnamon
     * @param addWhippedCream is whether it has or not whipped cream
     * @param addChocolate    is whether it has or not chocolate
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addSugar, boolean addMilk, boolean addCaramel, boolean addCinnamon, boolean addWhippedCream, boolean addChocolate, String name) {
        String priceMessage = getString(R.string.user_name, name);
        priceMessage += "\n" + getString(R.string.opt_sugar, addSugar);
        priceMessage += "\n" + getString(R.string.opt_milk, addMilk);
        priceMessage += "\n" + getString(R.string.top_caramel, addCaramel);
        priceMessage += "\n" + getString(R.string.top_cinnamon, addCinnamon);
        priceMessage += "\n" + getString(R.string.top_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.top_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.quantity, quantity);
        priceMessage += "\n" + getString(R.string.total_charged, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Figure out if the user wants Sugar
        CheckBox sugarCheckBox = findViewById(R.id.sugar_checkbox);
        boolean hasSugar = sugarCheckBox.isChecked();
        // Figure out if the user wants Milk
        CheckBox milkCheckBox = findViewById(R.id.milk_checkbox);
        boolean hasMilk = milkCheckBox.isChecked();
        // Figure out if the user wants Caramel
        CheckBox caramelCheckBox = findViewById(R.id.caramel_checkbox);
        boolean hasCaramel = caramelCheckBox.isChecked();
        // Figure out if the user wants Cinnamon
        CheckBox cinnamonCheckBox = findViewById(R.id.cinnamon_checkbox);
        boolean hasCinnamon = cinnamonCheckBox.isChecked();
        // Figure out if the user wants Whipped Cream
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        // Figure out if the user wants Chocolate
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        // Find the user's name
        EditText customerName = findViewById(R.id.name);
        String name = customerName.getText().toString();
        int price = calculatePrice(hasCaramel, hasCinnamon, hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasSugar, hasMilk, hasCaramel, hasCinnamon, hasWhippedCream, hasChocolate, name);
        //Send order summary to an email
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: orders@coffeeshop.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}