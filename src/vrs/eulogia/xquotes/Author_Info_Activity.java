package vrs.eulogia.xquotes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Author_Info_Activity  extends Activity{
	
   	private Cursor cursor;
	    private static final String TAG = "Author_Info_Activity";
	    DBOpener dbo =new DBOpener(this);
	    String author;
	    Author author_obj;  

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
	        setContentView(R.layout.author_info);
	
	        author = getIntent().getExtras().getString("author");
	        Log.i(TAG,author);
	        
	        cursor = getAuthorInfo(author);
	        
	        	        
	        if(cursor.moveToFirst()){
	        	author_obj = new Author(cursor.getInt(0));
	        }
	        
	        populateAuthorInfo(author_obj);
	      	
		}
	    

		public Cursor getAuthorInfo(String author){
			Cursor result=null;
			String sql="SELECT _ID FROM AUTHORS WHERE IMG='"+author+"'";
			try{
		    result = dbo.getWritableDatabase().rawQuery(sql, null);
			}catch(SQLException e){
				Log.i(TAG,e.toString());
			}
			
			return result;
		}
		
		public void populateAuthorInfo(Author author_obj){
			
			TextView name=(TextView) findViewById(R.id.name);
	        TextView born=(TextView) findViewById(R.id.born);
	        TextView profession=(TextView) findViewById(R.id.profession);
	        TextView about=(TextView) findViewById(R.id.about);
	        ImageView img=(ImageView) findViewById(R.id.img_author);
	        
	        Button browse=(Button) findViewById(R.id.browse_quotes);
	        TextView more=(TextView) findViewById(R.id.more_info);
	        
	        name.setText(author_obj.getFull_name());
	        born.setText("("+author_obj.getBirth_date()+" - "+author_obj.getDeath_date()+")");
	        profession.setText(author_obj.getProfession());
	        about.setText(author_obj.getAbout());
	        
	        browse.setText("Quotes By "+author_obj.getFull_name());
	        String text="<a href=\""+author_obj.getWiki_link()+"\">Read More About "+author_obj.getFull_name()+"</a>";
	        more.setMovementMethod(LinkMovementMethod.getInstance());
	        more.setText(Html.fromHtml(text));

	        
	        int resID=getResources().getIdentifier(author_obj.getImg(),"drawable",getPackageName());
	        img.setImageResource(resID);
	        
		}
		
		public void getAuthorQuotes(View view){
			
			Intent i=new Intent(this,Quotes_Activity.class);
			i.putExtra("author", author);
			startActivity(i);
		}
	}
