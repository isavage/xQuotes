package vrs.eulogia.xquotes;

import java.util.Random;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XQuotesActivity extends Activity {
    /** Called when the activity is first created. */
      
	private GestureDetector myGestDetector;
    private LinearLayout qotd_layout;
	
    private Cursor cursor;
    private Cursor result;
    private static final String TAG = "XQuotesActivity";
    DBOpener dbo =new DBOpener(this);
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(dbo != null)
			dbo.close();
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        setInitialScreen();
        setGestureDetector();
        
        
    }
	
	public void setInitialScreen(){
		
		
        cursor = getRandomQuote();
        
        //Layout for animation
        LinearLayout anim_layout=(LinearLayout)findViewById(R.id.anim_layout);
        //Animation
        Animation anim = AnimationUtils.loadAnimation(xQuotes.getAppContext(), R.anim.grow_from_top);
        anim_layout.startAnimation(anim);
                
        //Loading Views
        AutoResizeTextView qotd = (AutoResizeTextView)findViewById(R.id.qotd);
        TextView qotd_author = (TextView)findViewById(R.id.qotd_author);
        ImageView qotd_img = (ImageView)findViewById(R.id.qotd_img);
        if (cursor.moveToFirst()){
        	qotd.setText(cursor.getString(0));
        	Cursor cursor_auth=getQuoteAuthor(cursor.getInt(1));
        	Log.i(TAG,Integer.toString(cursor.getInt(1)));
        	if(cursor_auth.moveToFirst()){
        		qotd_author.setText(cursor_auth.getString(0));
        		int resID = getResources().getIdentifier(cursor_auth.getString(1),
        			    "drawable", getPackageName());
        		Log.i(TAG,cursor_auth.getString(1)+"--"+Integer.toString(resID));
        		qotd_img.setImageResource(resID);
        		qotd_img.invalidate();
        	}
        	else{
        		Log.e(TAG,"Cursor failed to get Author.");
        		setInitialScreen();
        	}
        }
        else{
        	Log.e(TAG,"Cursor failed to get Quote.");
        	setInitialScreen();
        }
        
	}
	
   
    
    public Cursor getRandomQuote(){
    	 
    	int random_q=1;
    	
    	long time_seed=System.currentTimeMillis(); // Generating random seed
    	Log.i(TAG,Long.toString(time_seed)); 
    	
    	Random random=new Random(time_seed);
    	int random_int=Math.abs(random.nextInt());
    	Log.i(TAG,Integer.toString(random_int));
    	
    	String sql="SELECT COUNT(*) FROM QUOTES";
    	try{
        result = dbo.getWritableDatabase().rawQuery(sql, null);
    	}catch(SQLException e){
    		Log.i(TAG,e.toString());
    	}
    	if(result.moveToFirst()){
    		random_q=(random_int%result.getInt(0))+1;
    	}
    	
    	sql="SELECT QUOTE, AUTHOR_ID FROM QUOTES " +
        		"WHERE _ID = "+Integer.toString(random_q);
    	try{
        result = dbo.getWritableDatabase().rawQuery(sql, null);
        
    	}catch(SQLException e){
    		Log.i(TAG,e.toString());
    	}
    	
    	return result;
    }
    public Cursor getQuoteAuthor(int id){
    	
    	
    	String sql="SELECT FULL_NAME,IMG FROM AUTHORS " +
        		"WHERE _ID ="+id;
    	try{
        result = dbo.getWritableDatabase().rawQuery(sql, null);
        
    	}catch(SQLException e){
    		Log.i(TAG,e.toString());
    	}
    	
    	return result;
    }
    
    public void qotdRefresh(View view){
    	
    	Animation rotate = AnimationUtils.loadAnimation(xQuotes.getAppContext(), R.anim.rotate);
    	
    	view.startAnimation(rotate);
    	setInitialScreen();
    	
    }
	public void label_clicked(View view){
			
			Intent i=null;
					
	    	TextView label=(TextView)findViewById(view.getId());
	    	
	    	switch(view.getId()){
	    	case R.id.quotes_label: 
	    		i=new Intent(this,Quotes_Activity.class); 
	    		i.putExtra("author", "all");
	    		break;
	    	case R.id.author_label: 
	    		i=new Intent(this,Author_Activity.class);	
	    		break;
	    	case R.id.fav_label: 
	    		i=new Intent(this,Fav_Activity.class); 
	    		i.putExtra("author", "fav");
	    		break;
	    	case R.id.categories_label: 
	    		i=new Intent(this,Quotes_Activity.class); break;
	    	case R.id.settings_label: 
	    		i=new Intent(this,Quotes_Activity.class); break;
	    	case R.id.feedback_label: 
	    		i=new Intent(this,Quotes_Activity.class); break;
	    	}
	    	
	    	label.startAnimation(AnimationUtils.loadAnimation(xQuotes.getAppContext(), R.anim.image_click));
	    	startActivity(i);
	    	
	    }
	    
	
	public void donate_clicked(View view){
		
			Intent i=new Intent(this,Donate_Activity.class);
			
			ImageView label=(ImageView)findViewById(view.getId());
			label.startAnimation(AnimationUtils.loadAnimation(xQuotes.getAppContext(), R.anim.image_click));
			startActivity(i);
		
	}
	
	public void setGestureDetector(){
		myGestDetector = new GestureDetector(this, new SimpleOnGestureListener()
        {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                //qotd_layout.setText("just scroll'n");
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e1)
            {
                //mainTextView.setText("on down");
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
            	setInitialScreen(); 
            	//mainTextView.setText("on fling");
                return false;
            }
        });

		qotd_layout = (LinearLayout)findViewById(R.id.anim_layout);
        //qotd_layout.setText("Starting app...");
        qotd_layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                myGestDetector.onTouchEvent(event);
                return true;
            }
        });
    }

}

