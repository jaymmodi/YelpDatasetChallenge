import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Category {
	private String name;
	private ArrayList<Category> subcategory;
	
	public Category(String name)
	{
		this.name = name;
		subcategory = new ArrayList<Category>();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public ArrayList<Category> getSubcateories()
	{
		return this.subcategory;
	}
	public void addSubcategories(Category category)
	{
		this.subcategory.add(category);
	}

public static void main(String []args) throws FileNotFoundException, IOException
{
	JSONParser parser = new JSONParser();
	 try {
		Object obj =  parser.parse(new FileReader("/home//rk//Documents//Z534-Search//categories.json"));
		JSONArray jsonObjs = (JSONArray) obj;
		Set categories = new HashSet();
		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		for (Object jsonObj : jsonObjs) {
			JSONObject jb = (JSONObject)jsonObj;
			JSONArray parents = (JSONArray) jb.get("parents");
			String subcategory = jb.get("alias").toString();
			for (Object object : parents) {
				if(map.containsKey(object.toString()))
					map.get(object.toString()).add(subcategory);
				else
				{
					map.put(object.toString(),new ArrayList<String>());
					map.get(object.toString()).add(subcategory);
				}
			}
//			if(parents.toArray().length >0)
//				categories.addAll(parents);
				
		}
		for (String key : map.keySet()) {
			System.out.println("----------"+key+"-------------");
			for(String v : map.get(key))
			{
				System.out.println(v);
			}
			System.out.println("----------END-------------");
			
		}
		
		
		
		
//		Iterator<JSONObject> jsonObjs = (Iterator<JSONObject>)obj;
//		while (jsonObjs.hasNext()) {
//			JSONObject jsonObj = jsonObjs.next();
////			JSONArray parents = (JSONArray) jsonObj.get("parents");
////			Iterator<JSONObject> iterator = parents.iterator();
////			while (iterator.hasNext()) {
////				
////		        // System.out.println(iterator.next());
////		        }
//			
//		
//		}
		
		
		
//		JSONArray parents = (JSONArray) jsonObj.get("parents");
//        Iterator<String> iterator = parents.iterator();
//        while (iterator.hasNext()) {
//         System.out.println(iterator.next());
//        }
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
//	Category c = new Category("Root");
//	Category a = new Category("a");
//	Category b = new Category("b");
//	c.addSubcategories(a);
//	c.addSubcategories(b);
//	for (Category node : c.getSubcateories()) {
//		System.out.println(node.getName());
//	}
}
}


