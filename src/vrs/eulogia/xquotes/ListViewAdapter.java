package vrs.eulogia.xquotes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
  
public class ListViewAdapter extends BaseAdapter  
{  
	public ArrayList<Quote_OBJ> values;
	public Activity context;  
    public LayoutInflater inflater; 
    
    
    public ArrayList<Author_OBJ> authors;
      
    public ListViewAdapter(Activity context,ArrayList<Quote_OBJ> values, ArrayList<Author_OBJ> authors) {  
        super();  
        
        this.context = context;  
        this.values = values; 
        this.authors = authors;
         
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
    
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        if(values!=null)
        	return values.size(); 
        else if(authors!=null)
        	return authors.size();
        return 0;
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
  
    public static class ViewHolder  
    {  
        
        TextView item_quote;  
        TextView item_author;
        TextView item_author_name;
        ImageView item_author_img;
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
		  if(values!=null){
		        ViewHolder holder;  
		        if(convertView==null)  
		        {  
		            holder = new ViewHolder();  
		            convertView = inflater.inflate(R.layout.listitem_row, null);  
		  
		            holder.item_quote = (TextView) convertView.findViewById(R.id.item_quote);  
		            holder.item_author = (TextView) convertView.findViewById(R.id.item_author);  
		  
		            convertView.setTag(holder);  
		        }  
		        else  
		            holder=(ViewHolder)convertView.getTag();  
		  
		        holder.item_quote.setText(values.get(position).getQuote());  
		        holder.item_author.setText(values.get(position).getAuthor());  
		  }
		  else if(authors!=null){
			  ViewHolder holder;  
		        if(convertView==null)  
		        {  
		            holder = new ViewHolder();  
		            convertView = inflater.inflate(R.layout.list_authors, null);  
		  
		            holder.item_author_name = (TextView) convertView.findViewById(R.id.item_author_name);  
		            holder.item_author_img = (ImageView) convertView.findViewById(R.id.item_author_img);  
		  
		            convertView.setTag(holder);  
		        }  
		        else  
		            holder=(ViewHolder)convertView.getTag();  
		  
		        
		        holder.item_author_name.setText(authors.get(position).getAuthor());  
		        int resID=context.getResources().getIdentifier(authors.get(position).getImg(),"drawable",context.getPackageName());
		        holder.item_author_img.setImageResource(resID);  
		  }
		  
		  return convertView;  
    }   
  
} 