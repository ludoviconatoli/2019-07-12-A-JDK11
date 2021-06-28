package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void listAllFoods(Map<Integer, Food> mappa, int np){
		String sql = "SELECT f1.food_code, f1.display_name "
				+ "FROM portions p1, food f1 "
				+ "WHERE f1.food_code = p1.food_code "
				+ "GROUP BY f1.food_code, f1.display_name "
				+ "HAVING COUNT(distinct p1.portion_display_name) < ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, np);
						
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(!mappa.containsKey(res.getInt("f1.food_code"))) {
					Food f = new Food(res.getInt("f1.food_code"), res.getString("f1.display_name"));
					mappa.put(f.getFood_code(), f);
				}
				
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Adiacenza> getArchi(Map<Integer, Food> mappa){
		
		String sql = "SELECT f1.food_code, f2.food_code, SUM(c.condiment_calories)/COUNT(c.condiment_calories) AS peso "
				+ "FROM food_condiment f1, food_condiment f2, condiment c "
				+ "WHERE f1.food_code > f2.food_code AND c.condiment_code = f1.condiment_code AND f1.condiment_code = f2.condiment_code "
				+ "GROUP BY f1.food_code, f2.food_code "
				+ "HAVING COUNT(c.condiment_calories) > 0";
		
		List<Adiacenza> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(mappa.containsKey(res.getInt("f1.food_code")) && mappa.containsKey(res.getInt("f2.food_code"))) {
					
					Adiacenza a = new Adiacenza(mappa.get(res.getInt("f1.food_code")), mappa.get(res.getInt("f2.food_code")), res.getDouble("peso"));
					result.add(a);
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}	

	}
}
