package com.example.android.justjava2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */

public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
     * @param addChocolate    is whether of not the user wants chocolate topping
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // Base price of the coffee
        int pricePerCup = 5;
        // Price of the topping: Whipped Cream
        int priceWhippedCream = 1;
        // Price of the topping: Chocolate
        int priceChocolate = 2;
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
     * @param addWhippedCream is whether is has or not whipped cream
     * @param addChocolate    is whether is has or not chocolate
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name) {
        String priceMessage = getString(R.string.user_name, name);
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
        // Figure out if the user wants Whipped Cream
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        // Figure out if the user wants Chocolate
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        // Find the user's name
        EditText customerName = findViewById(R.id.name);
        String name = customerName.getText().toString();
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);
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