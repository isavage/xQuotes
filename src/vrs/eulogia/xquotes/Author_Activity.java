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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;


public class Author_Activity extends Activity {
	
	/**
	 * Listview selected row
	 */
	private int mSelectedRow = 0;
	
	/**
	 * Right arrow icon on each listview row
	 */
	//private ImageView mMoreIv = null;
	
	private static final int ID_FAV = 1;
	private static final int ID_BAN = 2;
	//private static final int ID_UPLOAD = 3;
	
	private static final int AUTH_FAV = 2;
	//private static final int AUTH_ALLOWED = 1;
	private static final int AUTH_BAN = 0;
	
   	private Cursor cursor;
    private static final String TAG = "Author_Activity";
    DBOpener dbo =new DBOpener(this);
    
    int status;
    
    ListView lview;  
    ListViewAdapter adapter;
    PopupWindow pw;

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
        setContentView(R.layout.authors);
        
        
        
        
        cursor = getAuthorsList();
        //startManagingCursor(cursor);
        //ArrayList<Quote> quote_arr=new  ArrayList<Quote>();
        ArrayList<Author_OBJ> values = new ArrayList<Author_OBJ>();
        
        if(cursor.moveToFirst()){
        	values.add(new Author_OBJ(cursor.getString(0),cursor.getString(1)));
        	       	
        	while(cursor.moveToNext()){
        		values.add(new Author_OBJ(cursor.getString(0),cursor.getString(1)));
            }
        }
        
        ActionItem addItem 		= new ActionItem(ID_FAV, "Mark Favorite", getResources().getDrawable(R.drawable.ic_fav));
		ActionItem acceptItem 	= new ActionItem(ID_BAN, "Ban Author", getResources().getDrawable(R.drawable.ic_ban));
        //ActionItem uploadItem 	= new ActionItem(ID_UPLOAD, "Upload", getResources().getDrawable(R.drawable.ic_up));
		
		final QuickAction mQuickAction 	= new QuickAction(this);
		
		mQuickAction.addActionItem(addItem);
		mQuickAction.addActionItem(acceptItem);
		//mQuickAction.addActionItem(uploadItem);
	
		//setup the action item click listener
				mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction quickAction, int pos, int actionId) {
						//ActionItem actionItem = quickAction.getActionItem(pos);
						
						status=0;
						if(cursor.moveToPosition(mSelectedRow)){
							status = getAuthorStatus(cursor.getString(1));
							Log.i(TAG,Integer.toString(status));
						}
						
						if (actionId == ID_FAV) { //Add item selected
							if(status==xQuotes.FAV_SELECTED){
								
								Toast.makeText(getApplicationContext(), "Author already marked as favorite.", Toast.LENGTH_SHORT).show();
							}
							else{
							if(cursor.moveToPosition(mSelectedRow)){
								setFavorite(cursor.getString(1));
								Toast.makeText(getApplicationContext(), cursor.getString(0)+" marked " +
										"as favorite.", Toast.LENGTH_LONG).show();
							}
							}
						} else if(actionId == ID_BAN) {
							if(status==xQuotes.BAN_SELECTED){
								
								Toast.makeText(getApplicationContext(), "Author already in your Ban List.", Toast.LENGTH_SHORT).show();
							}
							else{
							if(cursor.moveToPosition(mSelectedRow)){
								setBanned(cursor.getString(1));
								Toast.makeText(getApplicationContext(), "You" +
										" will no longer find "+cursor.getString(0) + "'s quotes in " +
												"Quote Of The Day", Toast.LENGTH_LONG).show();
							}
							}
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
        
        
        
                
        lview = (ListView) findViewById(R.id.list_authors);  
        lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				mSelectedRow = position; //set the selected row
				
				mQuickAction.show(view);
				
				//change the right arrow icon to selected state 
				/*mMoreIv = (ImageView) view.findViewById(R.id.i_more);
				mMoreIv.setImageResource(R.drawable.ic_list_more_selected);*/
				//onLongPressAuthor(position);
				return false;
			}

        });
        
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
             {
            	onSelect_Author(position);
             }

        });
        
        adapter = new ListViewAdapter(this, null,values);  
        lview.setAdapter(adapter);
        
        
        
        

	}
    

	public Cursor getAuthorsList(){
		Cursor result=null;
		String sql="SELECT FULL_NAME, IMG FROM AUTHORS";
		try{
	    result = dbo.getWritableDatabase().rawQuery(sql, null);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
		return result;
	}
	public void onSelect_Author(int position){
		cursor.moveToPosition(position);
		Intent i=new Intent(this,Author_Info_Activity.class);
		Log.i(TAG,cursor.getString(1));
		i.putExtra("author", cursor.getString(1));
		startActivity(i);
	}
	
	public void setFavorite(String img){
		
		String sql="UPDATE AUTHORS SET AUTHOR_ALLOWED="+AUTH_FAV+" WHERE IMG='"+img+"'";
		try{
	    dbo.getWritableDatabase().execSQL(sql);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
	}
	public void setBanned(String img){
		
		String sql="UPDATE AUTHORS SET AUTHOR_ALLOWED="+AUTH_BAN+" WHERE IMG='"+img+"'";
		try{
	    dbo.getWritableDatabase().execSQL(sql);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
	}
	
	public  int getAuthorStatus(String img){
		Cursor status=null;
		String sql;
		sql="SELECT AUTHOR_ALLOWED FROM AUTHORS WHERE IMG='"+img+"'";	
		
		try{
		
	    status = dbo.getWritableDatabase().rawQuery(sql, null);
		}catch(SQLException e){
			Log.i(TAG,e.toString());
		}
		
		if(status.moveToFirst()){
			switch( status.getInt(0)){
			case AUTH_FAV:
				//status.close();
				//dbo.close();
				return xQuotes.FAV_SELECTED;
			case AUTH_BAN:
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
		
	}
	
	
}