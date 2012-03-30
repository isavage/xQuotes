package vrs.eulogia.xquotes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;


public class Fav_Activity extends Activity {

	/**
	 * Listview selected row
	 */
	
	@SuppressWarnings("unused")
	private int mSelectedRow = 0;
	
	/**
	 * Right arrow icon on each listview row
	 */
	//private ImageView mMoreIv = null;
	
	private static final int ID_UNFAV = 1;
	private static final int ID_BAN = 2;
	//private static final int ID_UNFAV = 3;
	
	private static final int QUOTE_FAV = 1;
	private static final int QUOTE_ALLOWED = 0;
	private static final int QUOTE_BAN = 2;
	
   	private Cursor cursor;
    private static final String TAG = "Fav_Activity";
    DBOpener dbo =new DBOpener(this);
    
    ListView lview;  
    ListViewAdapter adapter;  
    String author="";
    
    ActionItem unmarkFavItem;
    ActionItem banItem;
  //  ActionItem unmarkFavItem;
    
    int status;
    
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
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        setContentView(R.layout.favorite);
        
        author = getIntent().getExtras().getString("author");
        
        
        cursor = getFavQuotesList();
        //startManagingCursor(cursor);
        //ArrayList<Quote> quote_arr=new  ArrayList<Quote>();
        
        ArrayList<Quote_OBJ> values = new ArrayList<Quote_OBJ>();
        
        if(cursor.moveToFirst()){
        	values.add(new Quote_OBJ(cursor.getString(0),cursor.getString(1)));
        	Log.i(TAG,Integer.toString(cursor.getInt(2)));
        	       	
        	while(cursor.moveToNext()){
        		values.add(new Quote_OBJ(cursor.getString(0),cursor.getString(1)));
            }
        }
                
        lview = (ListView) findViewById(R.id.list);  
        adapter = new ListViewAdapter(this, values,null);  
        lview.setAdapter(adapter);
        
        unmarkFavItem 		= new ActionItem(ID_UNFAV, "Unmark Favorite", getResources().getDrawable(R.drawable.ic_fav));
		banItem 	= new ActionItem(ID_BAN, "Ban Quote",getResources().getDrawable(R.drawable.ic_ban));
        //unmarkFavItem 	= new ActionItem(ID_UNFAV, "Unmark Favorite", getResources().getDrawable(R.drawable.ic_fav));
		
		final QuickAction mQuickAction 	= new QuickAction(this);
		

		mQuickAction.addActionItem(unmarkFavItem);
		mQuickAction.addActionItem(banItem);
		//mQuickAction.addActionItem(unmarkFavItem);
		
		//mQuickAction.addActionItem(uploadItem);
	
		//setup the action item click listener
				mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction quickAction, int pos, int actionId) {
						//ActionItem actionItem = quickAction.getActionItem(pos);
						/*status=0;
						if(cursor.moveToPosition(mSelectedRow)){
							status = getQuoteStatus(cursor.getInt(2));
							//Log.i(TAG,Integer.toString(status));
						}*/
						if (actionId == ID_UNFAV) { //Add item selected
									if(cursor.moveToPosition(mSelectedRow))
									unmarkFavorite(cursor.getInt(2));
									Toast.makeText(getApplicationContext(), "The quote has been removed from " +
											"favorites.", Toast.LENGTH_SHORT).show();
									Intent i = new Intent(xQuotes.getAppContext(),Fav_Activity.class);
									i.putExtra("author", "fav");
									startActivity(i);
						}else if(actionId == ID_BAN) {
									if(cursor.moveToPosition(mSelectedRow))
									setBanned(cursor.getInt(2));
									Toast.makeText(getApplicationContext(), "You will no longer " +
											"find this quote in Quote Of The Day.", Toast.LENGTH_SHORT).show();
									Intent i = new Intent(xQuotes.getAppContext(),Fav_Activity.class);
									i.putExtra("author", "fav");
									startActivity(i);
						}	
					}
				});
				
				//setup on dismiss listener, set the icon back to normal
				mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {			
					@Override
					public void onDismiss() {
						//mMoreIv.setImageResource(R.drawable.ic_list_more);
					}
				});
				
				lview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						mSelectedRow = position; //set the selected row
												
						
						mQuickAction.show(view);
						
						//change the right arrow icon to selected state 
						/*mMoreIv = (ImageView) view.findViewById(R.id.i_more);
						mMoreIv.setImageResource(R.drawable.ic_list_more_selected);*/
					}
				});
	}
    
	public Cursor getFavQuotesList(){
		Cursor result=null;
		String sql;
		
			sql="SELECT QUOTES.QUOTE,AUTHORS.FULL_NAME,QUOTES._ID FROM QUOTES,AUTHORS " +
					"WHERE QUOTES.AUTHOR_ID=AUTHORS._ID  " +
					"AND QUOTES.FAV="+QUOTE_FAV;
		
		try{
	    result = dbo.getWritableDatabase().rawQuery(sql, null);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
		return result;
	}
	
	public void unmarkFavorite(int id){
		Log.i(TAG,Integer.toString(id));
		String sql="UPDATE QUOTES SET FAV="+QUOTE_ALLOWED+" WHERE _ID="+id;
		try{
	    dbo.getWritableDatabase().execSQL(sql);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
	}
	public void setBanned(int id){
		
		String sql="UPDATE QUOTES SET FAV="+QUOTE_BAN+" WHERE _ID="+id;
		try{
	    dbo.getWritableDatabase().execSQL(sql);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
	}
	/*	public  int getQuoteStatus(int id){
		Cursor status=null;
		String sql;
		sql="SELECT FAV FROM QUOTES WHERE _ID="+id;	
		
		try{
		
	    status = dbo.getWritableDatabase().rawQuery(sql, null);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
		if(status.moveToFirst()){
			switch( status.getInt(0)){
			case QUOTE_FAV:
				//status.close();
				//dbo.close();
				return xQuotes.FAV_SELECTED;
			case QUOTE_BAN:
				//status.close();
				//dbo.close();
				return xQuotes.BAN_SELECTED;
			default:
				//status.close();
				//dbo.close();
				return xQuotes.NONE_SELECTED;
					
			}	
		}
		status.close();
		return xQuotes.NONE_SELECTED;
		
	}*/
	
}