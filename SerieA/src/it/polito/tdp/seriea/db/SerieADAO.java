package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasonInt;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons(Map<Integer, Season> seasonsIdMap) {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if (!seasonsIdMap.containsKey(res.getInt("season"))) {
					seasonsIdMap.put(res.getInt("season"), new Season(res.getInt("season"), res.getString("description")));
				}
				result.add(seasonsIdMap.get(res.getInt("season")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<SeasonInt> getWinPerYear(Team team, Map<Integer, Season> seasonsIdMap) {
		List<SeasonInt> result = new LinkedList<>();
		String sql = "SELECT matches.Season AS seas, COUNT(*) AS win FROM matches " + 
					"WHERE (matches.HomeTeam = ? AND matches.FTR= 'H') OR " + 
					"(matches.AwayTeam = ? AND matches.FTR= 'A') " + 
					"GROUP BY seas ORDER BY seas;";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = seasonsIdMap.get(res.getInt("seas"));
				if (s != null)
					result.add(new SeasonInt(s, res.getInt("win")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<SeasonInt> getDrawPerYear(Team team, Map<Integer, Season> seasonsIdMap) {
		List<SeasonInt> result = new LinkedList<>();
		String sql = "SELECT matches.Season AS seas, COUNT(*) AS draw FROM matches " + 
					"WHERE (matches.HomeTeam = ? AND matches.FTR= 'D') OR " + 
					"(matches.AwayTeam = ? AND matches.FTR= 'D') " + 
					"GROUP BY seas ORDER BY seas;";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = seasonsIdMap.get(res.getInt("seas"));
				if (s != null)
					result.add(new SeasonInt(s, res.getInt("draw")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
