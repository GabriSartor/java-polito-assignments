package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<YearCount> getSightingsPerYear() {
		String sql = "SELECT YEAR(DATETIME) as y, COUNT(id) as cnt FROM sighting "+
					"WHERE country =\"us\" GROUP BY y ORDER BY y;";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<YearCount> list = new LinkedList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new YearCount(Year.of(res.getInt("y")), res.getInt("cnt")) ) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("y"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	public List<String> getStates(Year year) {
		String sql = "SELECT distinct state FROM sighting "+
				"WHERE YEAR(datetime)= ? AND country =\"us\" ;";
		try {
			Connection conn = DBConnect.getConnection() ;
	
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, year.getValue());
			
			List<String> list = new LinkedList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("state")) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getString("state"));
				}
			}
			
			conn.close();
			return list ;
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<SightingsEdge> loadEdges(Year year) {
		String sql = "SELECT COUNT(*) AS cnt, s1.state AS s1s, s2.state AS s2s FROM sighting s1, sighting s2 WHERE " + 
				"s1.country = 'us' AND s2.country = 'us' AND " + 
				"YEAR(s1.DATETIME) = ? AND " + 
				"YEAR(s2.DATETIME) = YEAR(s1.DATETIME) AND " + 
				"s2.datetime > s1.datetime " + 
				"GROUP BY s1s, s2s;";
		try {
			Connection conn = DBConnect.getConnection() ;
	
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, year.getValue());
						
			List<SightingsEdge> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new SightingsEdge(res.getInt("cnt"), res.getString("s1s"), res.getString("s2s")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getString("s1s")+" "+res.getString("s2s"));
				}
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
