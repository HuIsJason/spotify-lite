package com.csc301.songmicroservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

//import ca.utoronto.utm.mcs.NotFoundException;

@Repository
public class SongDalImpl implements SongDal {

	private final MongoTemplate db;

	@Autowired
	public SongDalImpl(MongoTemplate mongoTemplate) {
		this.db = mongoTemplate;
	}

	@Override
	public DbQueryStatus addSong(Song songToAdd) {
		// TODO Auto-generated method stub
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);

		if (songToAdd == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			Song insertedSong = db.insert(songToAdd);
	
			if (insertedSong == null) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			} else {
				response.setData(insertedSong.getJsonRepresentation());
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus findSongById(String songId) {
		// TODO Auto-generated method stub
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);

		if (songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			Song queriedSong = db.findById(songId, Song.class);
	
			if (queriedSong == null) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			} else {
				response.setData(queriedSong.getJsonRepresentation());
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus getSongTitleById(String songId) {
		// TODO Auto-generated method stub
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);

		if (songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			Song queriedSong = db.findById(songId, Song.class);
	
			if (queriedSong == null) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			} else {
				response.setData(queriedSong.getSongName());
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus deleteSongById(String songId) {
		// TODO Auto-generated method stub
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);

		if (songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			DeleteResult deleteResult = db.remove(songId);
	
			if (deleteResult.getDeletedCount() == 0l) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			}
		}
		return response;
	}

	@Override
	public DbQueryStatus updateSongFavouritesCount(String songId, boolean shouldDecrement) {
		// TODO Auto-generated method stub
		DbQueryStatus response = new DbQueryStatus("OK", DbQueryExecResult.QUERY_OK);

		if (songId == null) {
			response.setMessage("INVALID INPUT");
			response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_GENERIC);
		} else {
			int updateAmount = (shouldDecrement) ? -1 : 1;
	
			Query searchQuery = new Query(Criteria.where("_id").is(songId));
			Song preUpdatedSong= db.findOne(searchQuery, Song.class);
			
			if (preUpdatedSong == null) {
				response.setMessage("NOT FOUND");
				response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			} else {
				long newFavouriteAmount = preUpdatedSong.getSongAmountFavourites() + updateAmount;
				UpdateResult updateResult = db.updateFirst(searchQuery, Update.update("songAmountFavourites", newFavouriteAmount), Song.class);
				
				if (updateResult.getModifiedCount() == 0l) {
					response.setMessage("NOT FOUND");
					response.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
				}
			}
		}
		return response;
	}
}
