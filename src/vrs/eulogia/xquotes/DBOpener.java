package vrs.eulogia.xquotes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpener extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "XQUOTES_DB.sqlite";
	//private static final String TAG = "DBOpener";
			
    private static final String TABLE_CREATE_QUOTES =
                "CREATE TABLE QUOTES (_ID INTEGER PRIMARY KEY, AUTHOR_ID INTEGER, CAT_ID INTEGER, " +
                "QUOTE TEXT, FAV INTEGER);";
    
    private static final String TABLE_CREATE_AUTHORS =
            "CREATE TABLE AUTHORS (_ID INTEGER PRIMARY KEY, FULL_NAME TEXT, REVERSE_NAME TEXT, " +
            "AUTHOR_ALLOWED INTEGER, WIKI_LINK TEXT, BIRTH_DATE TEXT, DEATH_DATE TEXT, PROFESSION TEXT, " +
            "ABOUT TEXT, IMG TEXT);";
    
    private static final String TABLE_CREATE_CATEGORIES =
            "CREATE TABLE CATEGORIES (_ID INTEGER PRIMARY KEY, CATEGORY TEXT, CAT_ALLOWED INTEGER);";
    
    DBOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
    
    

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_QUOTES);
        db.execSQL(TABLE_CREATE_AUTHORS);
        db.execSQL(TABLE_CREATE_CATEGORIES);
        try {
			populateDatabase(db);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		               
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void populateDatabase(SQLiteDatabase db)
	            throws IOException {
		    String assetFilename="insert.pp";
		    Context context =xQuotes.getAppContext();
	        byte[] bytes = readAsset(context, assetFilename);
	        String sql = new String(bytes, "UTF-8");
	        String[] lines = sql.split(";(\\s)*[\n\r]");
	        for (String line : lines) {
	            line = line.trim();
	            if (line.length() > 0) {
	                db.execSQL(line);
	            	}	    
	        	}
	}
	 public byte[] readAsset(Context context, String filename) throws IOException {
		 		InputStream in = context.getResources().getAssets().open(filename);
		 		try{
		 			return readAllBytes(in);
		 		}
	            finally {
	                in.close();
	            }
	}
	 public int copyAllBytes(InputStream in, OutputStream out) throws IOException {
	        int byteCount = 0;
	        byte[] buffer = new byte[4096];
	        while (true) {
	            int read = in.read(buffer);
	            if (read == -1) {
	                break;
	            }
	            out.write(buffer, 0, read);
	            byteCount += read;
	        }
	        return byteCount;
	    }

	    public byte[] readAllBytes(InputStream in) throws IOException {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        copyAllBytes(in, out);
	        return out.toByteArray();
	    }
}
