package com.csc301.profilemicroservice;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

@Repository
public class PlaylistDriverImpl implements PlaylistDriver {

	Driver driver = ProfileMicroserviceApplication.driver;

	public static void InitPlaylistDb() {
//		String queryStr;
//
//		try (Session session = ProfileMicroserviceApplication.driver.session()) {
//			try (Transaction trans = session.beginTransaction()) {
//				queryStr = "CREATE CONSTRAINT ON (nPlaylist:playlist) ASSERT exists(nPlaylist.plName)";
//				trans.run(queryStr);
//				trans.success();
//			}
//			session.close();
//		}
	}

	@Override
	public DbQueryStatus likeSong(String userName, String songId) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;
		Boolean songExists = false;

		if (userName == null || songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (likedSong:song {id:{songId}}) " +
							   "RETURN likedSong";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "songId", songId));
	
					trans.success();
				}
				session.close();
			}
			if (queryResult.hasNext()) {
				songExists = true;
			}
			if (songExists) {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "MATCH (user:profile {userName:{userName}})-[:created]->(:playlist)-[r:includes]->(:song {id:{songId}}) " +
								   "RETURN r";
						queryResult = trans.run(queryStr, Values.parameters("userName", userName, "songId", songId));
		
						trans.success();
					}
					session.close();
				}
				if (queryResult.hasNext()) {
					response.setMessage("ALREADY LIKED");
					response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
				} else {
					try (Session session = ProfileMicroserviceApplication.driver.session()) {
						try (Transaction trans = session.beginTransaction()) {
							queryStr = "MATCH (user:profile {userName:{userName}})-[:created]->(likedSongs:playlist), (songLiked:song {id:{songId}}) " +
									   "CREATE (likedSongs)-[r:includes]->(songLiked) " +
									   "RETURN r";
							queryResult = trans.run(queryStr, Values.parameters("userName", userName, "songId", songId));
			
							trans.success();
						}
						session.close();
					}
					if (!queryResult.hasNext()) {
						response.setMessage("UNSUCCESSFUL");
						response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
					}
				}
			} else {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "MATCH (user:profile {userName:{userName}})-[:created]->(likedSongs:playlist) " +
								   "CREATE (likedSongs)-[r:includes]->(songLiked:song {id:{songId}}) " +
								   "RETURN r";
						queryResult = trans.run(queryStr, Values.parameters("userName", userName, "songId", songId));

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
	public DbQueryStatus unlikeSong(String userName, String songId) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (userName == null || songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}})-[:created]->(:playlist)-[r:includes]->(:song {id:{songId}}) " +
							   "RETURN r";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "songId", songId));
	
					trans.success();
				}
				session.close();
			}
			if (!queryResult.hasNext()) {
				response.setMessage("ALREADY UNLIKED");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
			} else {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "MATCH (user:profile {userName:{userName}})-[:created]->(:playlist)-[r:includes]->(:song {id:{songId}}) " +
								   "DELETE r";
						queryResult = trans.run(queryStr, Values.parameters("userName", userName, "songId", songId));
	
						trans.success();
					}
					session.close();
				}
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus deleteSongFromDb(String songId) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (:playlist)-[r:includes]->(songLiked:song {id:{songId}}) " +
							   "DELETE r, songLiked";
					queryResult = trans.run(queryStr, Values.parameters("songId", songId));
	
					trans.success();
				}
				session.close();
			}
		}
		return response;
	}
}
