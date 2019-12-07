package com.csc301.profilemicroservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import org.springframework.stereotype.Repository;

//import com.csc301.songmicroservice.DbQueryExecResult;
//import com.csc301.songmicroservice.DbQueryStatus;

import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

@Repository
public class ProfileDriverImpl implements ProfileDriver {

	Driver driver = ProfileMicroserviceApplication.driver;

	public static void InitProfileDb() {
//		String queryStr;
//
//		try (Session session = ProfileMicroserviceApplication.driver.session()) {
//			try (Transaction trans = session.beginTransaction()) {
//				queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT exists(nProfile.userName)";
//				trans.run(queryStr);
//
//				queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT exists(nProfile.password)";
//				trans.run(queryStr);
//
//				queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT nProfile.userName IS UNIQUE";
//				trans.run(queryStr);
//
//				trans.success();
//			}
//			session.close();
//		}
	}
	
	@Override
	public DbQueryStatus createUserProfile(String userName, String fullName, String password) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (userName == null || fullName == null || password == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (newUser:profile {userName:{userName}}) " +
							   "RETURN newUser";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "fullName", fullName, "password", password));
	
					trans.success();
				}
				session.close();
			}
			if (queryResult.hasNext()) {
				response.setMessage("PROFILE ALREADY EXISTS");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
			} else {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "CREATE (newUser:profile {userName:{userName}, fullName:{fullName}, password:{password}})-[:created]->(:playlist) " +
								   "RETURN newUser";
						queryResult = trans.run(queryStr, Values.parameters("userName", userName, "fullName", fullName, "password", password));
		
						trans.success();
					}
					session.close();
				}
				if (!queryResult.hasNext()) {
					response.setMessage("UNSUCCESSFUL");
					response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
				}
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus followFriend(String userName, String frndUserName) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (userName == null || frndUserName == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else if (userName.equals(frndUserName)) {
			response.setMessage("CANNOT FOLLOW YOURSELF");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}})-[r:follows]->(friend:profile {userName:{friendName}}) " +
							   "RETURN r";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "friendName", frndUserName));
	
					trans.success();
				}
				session.close();
			}
			if (queryResult.hasNext()) {
				response.setMessage("ALREADY FOLLOWING");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
			} else {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "MATCH (user:profile {userName:{userName}}), (friend:profile {userName:{friendName}}) " +
								   "CREATE (user)-[r:follows]->(friend) " +
								   "RETURN r";
						queryResult = trans.run(queryStr, Values.parameters("userName", userName, "friendName", frndUserName));
		
						trans.success();
					}
					session.close();
				}
				if (!queryResult.hasNext()) {
					response.setMessage("UNSUCCESSFUL");
					response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
				}
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus unfollowFriend(String userName, String frndUserName) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (userName == null || frndUserName == null || userName.equals(frndUserName)) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}})-[r:follows]->(friend:profile {userName:{friendName}}) " +
							   "RETURN r";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "friendName", frndUserName));
	
					trans.success();
				}
				session.close();
			}
			if (!queryResult.hasNext()) {
				response.setMessage("ALREADY UNFOLLOWED");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
			} else {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "MATCH (user:profile {userName:{userName}})-[r:follows]->(friend:profile {userName:{friendName}}) " +
								   "DELETE r";
						queryResult = trans.run(queryStr, Values.parameters("userName", userName, "friendName", frndUserName));
	
						trans.success();
					}
					session.close();
				}
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus getAllSongFriendsLike(String userName) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		Map<String, Object> data = new HashMap<String, Object>();
		String queryStr;
		StatementResult friendQueryResult;
		StatementResult songQueryResult;

		if (userName == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			// getting the list of friends's names first
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}})-[:follows]->(friends:profile) " +
							   "RETURN friends";
					friendQueryResult = trans.run(queryStr, Values.parameters("userName", userName));
	
					trans.success();
				}
				session.close();
			}
			if (!friendQueryResult.hasNext()) {
				response.setMessage("USER HAS NO FRIENDS");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
			} else {
				while (friendQueryResult.hasNext()) {
					List<Object> songList = new ArrayList<Object>();
					Record friends = friendQueryResult.next();
					String friendName = (String) friends.fields().get(0).value().asMap().get("userName");

					songList.add(friendName);

					try (Session session = ProfileMicroserviceApplication.driver.session()) {
						try (Transaction trans = session.beginTransaction()) {
							queryStr = "MATCH (user:profile {userName:{userName}})-[:follows]->(:profile)-[:created]->(:playlist)-[:includes]->(friendSong:song) " +
									   "RETURN friendSong";
							songQueryResult = trans.run(queryStr, Values.parameters("userName", userName));
			
							trans.success();
						}
						session.close();
					}
					while (songQueryResult.hasNext()) {
						Record songs = songQueryResult.next();
						String songName = (String) songs.fields().get(0).value().asMap().get("songId");
						
						songList.add(songName);
					}
					String friendFullName = (String) friends.fields().get(0).value().asMap().get("fullName");
					data.put(friendFullName, songList);
				}
				response.setData(data);
			}
		}
		return response;
	}

//	public DbQueryStatus getAllSongFriendsLike(String userName) {
//	    DbQueryStatus songList = null;
//	    Map<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
//
//	    if (userName == null) {
//	      songList = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
//	      songList.setData(data);
//	      return songList;
//	    }
//
//	    try (Session session = driver.session()) {
//	      StatementResult result = session.run(
//	          "MATCH (me:profile{userName:{user}})-[:follows]->(friends:profile) RETURN friends",
//	          Values.parameters("user", userName));
//	      while (result.hasNext()) {
//	        Record friends = result.next();
//	        ArrayList<String> songNames = new ArrayList<String>();
//	        String friendUser = (String) friends.fields().get(0).value().asMap().get("userName");
//	        
//	        StatementResult result2 = session.run(
//	            "MATCH (me:profile{userName:{friend}})-[:created]->(:playlist)-[:includes]->(songs:song) RETURN songs",
//	            Values.parameters("friend", friendUser));
//	        
//	        while(result2.hasNext()) {
//	          Record songs = result2.next();
//	          String temp = (String) songs.fields().get(0).value().asMap().get("songId");
//	          songNames.add(temp);
//	          //songNames.add((String) songs.fields().get(0).value().asMap().get("songId"));
//	        }
//	        data.put((String) friends.fields().get(0).value().asMap().get("fullName"), songNames);
//	      }
//	      songList = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
//	      songList.setData(data);
//	      return songList;
//	    }
//	  }
}
