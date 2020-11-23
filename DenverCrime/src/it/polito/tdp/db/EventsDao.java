package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.model.DistrictGeo;
import it.polito.tdp.model.Event;
import it.polito.tdp.model.Evento;


public class EventsDao {
	
	public List<Event> listAllEvents(Map<Long, Event> evIdMap){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if (!evIdMap.containsKey(res.getLong("incident_id"))) {
						Event e = new Event(res.getLong("incident_id"),
								res.getInt("offense_code"),
								res.getInt("offense_code_extension"), 
								res.getString("offense_type_id"), 
								res.getString("offense_category_id"),
								res.getTimestamp("reported_date").toLocalDateTime(),
								res.getString("incident_address"),
								res.getDouble("geo_lon"),
								res.getDouble("geo_lat"),
								res.getInt("district_id"),
								res.getInt("precinct_id"), 
								res.getString("neighborhood_id"),
								res.getInt("is_crime"),
								res.getInt("is_traffic"));
						evIdMap.put(res.getLong("incident_id"), e);
						list.add(e);
					} else {
						list.add(evIdMap.get(res.getLong("incident_id")));
					}
						
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
	
	public Map<Integer, LatLng> listDistrictsAndGeo(int year) {
		String sql = "SELECT `events`.district_id as id, avg(`events`.geo_lon) AS avglon, avg(`events`.geo_lat) AS avglat FROM `events` " + 
				"WHERE YEAR(`events`.reported_date) = ? " + 
				"GROUP BY `events`.district_id;" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			
			Map<Integer, LatLng> result = new HashMap<>();
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					result.put(res.getInt("id"), new LatLng(res.getDouble("avglat"), res.getDouble("avglon")));
						
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}		
	}

	public Integer getDistrettoSicuro(Integer year) {
		String sql = "SELECT `events`.district_id as id FROM `events` " + 
				"WHERE YEAR(`events`.reported_date) = ? " + 
				"GROUP BY `events`.district_id ORDER BY COUNT(*) ASC LIMIT 1;" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			
			Integer result = 0;
			ResultSet res = st.executeQuery() ;
			
			
			while(res.next()) {
				try {
					result = res.getInt("id");
						
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}	
	}
}
