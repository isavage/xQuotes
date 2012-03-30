package vrs.eulogia.xquotes;


import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

public class Donate_Activity extends Activity implements OnClickListener {
	
	// The PayPal server to be used - can also be ENV_NONE and ENV_LIVE
	private static final int server = PayPal.ENV_SANDBOX;
	// The ID of your application that you received from PayPal
	private static final String appID = "APP-80W284485P519543T";
	// This is passed in for the startActivityForResult() android function, the value used is up to you
	private static final int request = 1;
	
	protected static final int INITIALIZE_SUCCESS = 0;
	protected static final int INITIALIZE_FAILURE = 1;

	private String TAG="Donate_Activity";
	TextView labelSimplePayment;
	TextView exitApp;
	TextView title;
	TextView info;
	TextView extra;
	TextView appVersion;
	EditText donate_amount;
	LinearLayout layoutSimplePayment ;
		
	// You will need at least one CheckoutButton, this application has four for examples
	CheckoutButton launchSimplePayment;
	
	
	// These are used to display the results of the transaction
	public static String resultTitle;
	public static String resultInfo;
	public static String resultExtra;
	
	// This handler will allow us to properly update the UI. You cannot touch Views from a non-UI thread.
	Handler hRefresh = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
		    	case INITIALIZE_SUCCESS:
		    		setupButtons();
		            break;
		    	case INITIALIZE_FAILURE:
		    		showFailure();
		    		break;
			}
		}
	};

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize the library. We'll do it in a separate thread because it requires communication with the server
		// which may take some time depending on the connection strength/speed.
		Thread libraryInitializationThread = new Thread() {
			public void run() {
				initLibrary();
				
				// The library is initialized so let's create our CheckoutButton and update the UI.
				if (PayPal.getInstance().isLibraryInitialized()) {
					hRefresh.sendEmptyMessage(INITIALIZE_SUCCESS);
				}
				else {
					hRefresh.sendEmptyMessage(INITIALIZE_FAILURE);
				}
			}
		};
		libraryInitializationThread.start();
		setContentView(R.layout.donate);
			
		exitApp = (TextView) findViewById(R.id.exit);
		layoutSimplePayment = (LinearLayout) findViewById(R.id.donate_simple);
		info = (TextView) findViewById(R.id.donate_info);
		title = (TextView) findViewById(R.id.donate_title);
		extra = (TextView) findViewById(R.id.donate_extra);
		
	}
	
	/**
	 * Create our CheckoutButton and update the UI.
	 */
	public void setupButtons() {
		
		PayPal pp = PayPal.getInstance();
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_152x33, CheckoutButton.TEXT_DONATE);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchSimplePayment.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layoutSimplePayment.addView(launchSimplePayment);
		
		donate_amount = (EditText) findViewById(R.id.donate_amount);
		TextView dollars = (TextView) findViewById(R.id.dollars);
		dollars.setVisibility(0);
		donate_amount.setVisibility(0);
		
		info.setText("");
		info.setVisibility(View.GONE);
	}
	
	/**
	 * Show a failure message because initialization failed.
	 */
	public void showFailure() {
		title.setText("FAILURE");
		info.setText("Could not initialize the PayPal library.");
		title.setVisibility(View.VISIBLE);
		info.setVisibility(View.VISIBLE);
	}
	
	/**
	 * The initLibrary function takes care of all the basic Library initialization.
	 * 
	 * @return The return will be true if the initialization was successful and false if 
	 */
	private void initLibrary() {
		PayPal pp = PayPal.getInstance();
		// If the library is already initialized, then we don't need to initialize it again.
		if(pp == null) {
			// This is the main initialization call that takes in your Context, the Application ID, and the server you would like to connect to.
			pp = PayPal.initWithAppID(this, appID, server);
   			
			// -- These are required settings.
        	pp.setLanguage("en_US"); // Sets the language for the library.
        	// --
        	
        	// -- These are a few of the optional settings.
        	// Sets the fees payer. If there are fees for the transaction, this person will pay for them. Possible values are FEEPAYER_SENDER,
        	// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY.
        	pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER); 
        	// Set to true if the transaction will require shipping.
        	pp.setShippingEnabled(true);
        	// Dynamic Amount Calculation allows you to set tax and shipping amounts based on the user's shipping address. Shipping must be
        	// enabled for Dynamic Amount Calculation. This also requires you to create a class that implements PaymentAdjuster and Serializable.
        	pp.setDynamicAmountCalculationEnabled(false);
        	// --
		}
	}
	
	/**
	 * Create a PayPalPayment which is used for simple payments.
	 * 
	 * @return Returns a PayPalPayment. 
	 */
	
	private PayPalPayment exampleSimplePayment() {
		
		String dnt_amt = donate_amount.getText().toString();
		
		// Create a basic PayPalPayment.
		PayPalPayment payment = new PayPalPayment();
		// Sets the currency type for this payment.
    	payment.setCurrencyType("USD");
    	// Sets the recipient for the payment. This can also be a phone number.
    	payment.setRecipient("seller_1331451528_biz@yahoo.co.in");
    	// Sets the amount of the payment, not including tax and shipping amounts.
    	payment.setSubtotal(new BigDecimal(dnt_amt));
    	// Sets the payment type. This can be PAYMENT_TYPE_GOODS, PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE.
    	payment.setPaymentType(PayPal.PAYMENT_TYPE_PERSONAL);
    	
    	// PayPalInvoiceData can contain tax and shipping amounts. It also contains an ArrayList of PayPalInvoiceItem which can
    	// be filled out. These are not required for any transaction.
    	PayPalInvoiceData invoice = new PayPalInvoiceData();
    	// Sets the tax amount.
    	invoice.setTax(new BigDecimal("0.00"));
    	// Sets the shipping amount.
    	invoice.setShipping(new BigDecimal("0.00"));
    	
    	// PayPalInvoiceItem has several parameters available to it. None of these parameters is required.
    	PayPalInvoiceItem item1 = new PayPalInvoiceItem();
    	// Sets the name of the item.
    	item1.setName("Donation to xQuotes");
    	// Sets the ID. This is any ID that you would like to have associated with the item.
    	item1.setID("xQ005");
    	// Sets the total price which should be (quantity * unit price). The total prices of all PayPalInvoiceItem should add up
    	// to less than or equal the subtotal of the payment.
    	item1.setTotalPrice(new BigDecimal(dnt_amt));
    	// Sets the unit price.
    	item1.setUnitPrice(new BigDecimal(dnt_amt));
    	// Sets the quantity.
    	item1.setQuantity(1);
    	// Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively, you can create an ArrayList<PayPalInvoiceItem>
    	// and pass it to the PayPalInvoiceData function setInvoiceItems().
    	invoice.getInvoiceItems().add(item1);
    	
    	
    	return payment;
	}
	


	public void onClick(View v) {
		
		/**
		 * For each call to checkout() and preapprove(), we pass in a ResultDelegate. If you want your application
		 * to be notified as soon as a payment is completed, then you need to create a delegate for your application.
		 * The delegate will need to implement PayPalResultDelegate and Serializable. See our ResultDelegate for
		 * more details.
		 */		
			
		    // Use our helper function to create the simple payment.
			PayPalPayment payment = exampleSimplePayment();	
			// Use checkout to create our Intent.
			Intent checkoutIntent = PayPal.getInstance().checkout(payment, this, new ResultDelegate());
			// Use the android's startActivityForResult() and pass in our Intent. This will start the library.
	    	startActivityForResult(checkoutIntent, request);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode != request)
    		return;
    	
    	/**
    	 * If you choose not to implement the PayPalResultDelegate, then you will receive the transaction results here.
    	 * Below is a section of code that is commented out. This is an example of how to get result information for
    	 * the transaction. The resultCode will tell you how the transaction ended and other information can be pulled
    	 * from the Intent using getStringExtra.
    	 */
    	/*switch(resultCode) {
		case Activity.RESULT_OK:
			resultTitle = "SUCCESS";
			resultInfo = "You have successfully completed this " + (isPreapproval ? "preapproval." : "payment.");
			//resultExtra = "Transaction ID: " + data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
			break;
		case Activity.RESULT_CANCELED:
			resultTitle = "CANCELED";
			resultInfo = "The transaction has been cancelled.";
			resultExtra = "";
			break;
		case PayPalActivity.RESULT_FAILURE:
			resultTitle = "FAILURE";
			resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
			resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
		}*/
    	 
    	
    	launchSimplePayment.updateButton();
    	
    	
    	title.setText(resultTitle);
    	title.setVisibility(View.VISIBLE);
    	info.setText(resultInfo);
    	info.setVisibility(View.VISIBLE);
    	extra.setText(resultExtra);
    	extra.setVisibility(View.VISIBLE);
    }
	public void exitApp(View view){
		
			exitApp.startAnimation(AnimationUtils.loadAnimation(xQuotes.getAppContext(), R.anim.image_click));
			finish();
			Log.i(TAG,"finished");
		}
}