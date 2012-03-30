package vrs.eulogia.xquotes;

import android.database.Cursor;

public class Author {

	private int _id;
	private String full_name;
	private String reverse_name;
	private int author_allowed;
	private String wiki_link;
	private String birth_date;
	private String death_date;
	private String profession;
	private String about;
	private String img;
	
	DBOpener dbo = new DBOpener(xQuotes.getAppContext());
	
	public Author(int _id){
		
		Cursor cursor=dbo.getWritableDatabase().rawQuery("select * from authors a where _id="+_id, null);
		if(cursor.moveToFirst()){
			
			this.full_name = cursor.getString(1);
			this.reverse_name = cursor.getString(2);
			this.author_allowed = cursor.getInt(3);
			this.wiki_link = cursor.getString(4);
			this.birth_date = cursor.getString(5);
			this.death_date = cursor.getString(6);
			this.profession = cursor.getString(7);
			this.about = cursor.getString(8);
			this.img = cursor.getString(9);
			
			if(dbo != null)
				dbo.close();
			
		}
		
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getReverse_name() {
		return reverse_name;
	}
	public void setReverse_name(String reverse_name) {
		this.reverse_name = reverse_name;
	}
	public int getAuthor_allowed() {
		return author_allowed;
	}
	public void setAuthor_allowed(int author_allowed) {
		this.author_allowed = author_allowed;
	}
	public String getWiki_link() {
		return wiki_link;
	}
	public void setWiki_link(String wiki_link) {
		this.wiki_link = wiki_link;
	}
	public String getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}
	public String getDeath_date() {
		return death_date;
	}
	public void setDeath_date(String death_date) {
		this.death_date = death_date;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	
}
