package com.csc301.profilemicroservice;

import java.util.ArrayList;
import java.util.List;

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

//	public static void InitProfileDb() {
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
//	}
	
//	@Override
//	public DbQueryStatus createUserProfile(String userName, String fullName, String password) {
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
//		return null;
//	}

	@Override
	public DbQueryStatus followFriend(String userName, String frndUserName) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (userName == null || frndUserName == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}}), (friend:profile {userName:{friendName}})" +
							   "CREATE (user)-[r:follows]->(friend)" +
							   "RETURN r";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "friendName", frndUserName));
	
					trans.success();
				}
				session.close();
			}
	
			if (!queryResult.hasNext()) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus unfollowFriend(String userName, String frndUserName) {
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);
		String queryStr;
		StatementResult queryResult;

		if (userName == null || frndUserName == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}})-[r:follows]->(friend:profile {userName:{friendName}})" +
							   "RETURN r";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName, "friendName", frndUserName));
	
					trans.success();
				}
				session.close();
			}
	
			if (!queryResult.hasNext()) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			} else {
				try (Session session = ProfileMicroserviceApplication.driver.session()) {
					try (Transaction trans = session.beginTransaction()) {
						queryStr = "MATCH (user:profile {userName:{userName}})-[r:follows]->(friend:profile {userName:{friendName}})" +
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
		String queryStr;
		StatementResult queryResult;

		if (userName == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			try (Session session = ProfileMicroserviceApplication.driver.session()) {
				try (Transaction trans = session.beginTransaction()) {
					queryStr = "MATCH (user:profile {userName:{userName}})-[:follows]->(:profile)-[:created]->(:playlist)-[:includes]->(friendSong:song)" +
							   "RETURN friendSong";
					queryResult = trans.run(queryStr, Values.parameters("userName", userName));
	
					trans.success();
				}
				session.close();
			}

			if (!queryResult.hasNext()) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			} else {
				List<Object> queryItemList = new ArrayList<Object>();
				while (queryResult.hasNext()) {
					queryItemList.add(queryResult.next().asMap());
				}
				response.setData(queryItemList);
			}
		}
		return response;
	}
}
