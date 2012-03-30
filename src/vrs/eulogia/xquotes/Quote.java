package vrs.eulogia.xquotes;

import android.database.Cursor;
import android.util.Log;

public class Quote {
	
	private int _id;
	private int author_id;
	private int cat_id;
	private String quote;
	private int fav;
	private Author author;
	
	private String TAG = "Quote";
	
	DBOpener dbo = new DBOpener(xQuotes.getAppContext());
	
	public Quote(int _id){
		
		try{
		Cursor cursor=dbo.getWritableDatabase().rawQuery("select * from quotes q where _id="+_id, null);
			if(cursor.moveToFirst()){
			
				this._id = _id;
				this.author_id = cursor.getInt(1);
				this.cat_id = cursor.getInt(2);
				this.quote = cursor.getString(3);
				this.fav = cursor.getInt(4);
				this.author = new Author (cursor.getInt(1));
			}
		}catch(Exception e){
			Log.i(TAG,e.toString());
		}
		
	}
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getQuote() {
		return quote;
	}
	public void setQuote(String quote) {
		this.quote = quote;
	}
	public int getFav() {
		return fav;
	}
	public void setFav(int fav) {
		this.fav = fav;
	}
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
}
