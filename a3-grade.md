# assignment 3

**total grade**: 45.0/54.0

---

## automarker test results

Running auto-marker
('playlist', 'user1-favorites')
('playlist', 'user1-favorites')
('playlist', 'user1-favorites')
{'shabaz-FullName': ['shabaz', '5d61728193528481fe5a3127', '5d61728193528481fe5a3126', '5d61728193528481fe5a3126', '5d61728193528481fe5a3125', '5d61728193528481fe5a3124'], 'tahmid-FullName': ['tahmid', '5d61728193528481fe5a3127', '5d61728193528481fe5a3126', '5d61728193528481fe5a3126', '5d61728193528481fe5a3125', '5d61728193528481fe5a3124']}
{
    "Add Song with additional unexpected param, unexpected param: unexepectedParam: 12345": "Test Passed!",
    "Add Song with all invalid param keys, param keys: inValidSongName:songName1 invalidSongArtistFullName:songArtistFullName1 invalidSongAlbum:songAlbum": "Test Failed! Response status did not match expected response status",
    "Add Song with all valid paramaters, params: songName:songName1 songArtistFullName:songArtistFullName1 songAlbum:songAlbum": "Test Failed! Song data recieved differs from the song data added to the DB",
    "Add Song with one mandatory missing paramater, missing param: songArtistFullName": "Test Failed! Response status did not match expected response status",
    "Calling /followFriend as non-existing userName 'user1' to follow user with userName 'tahmid'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow a non-existing user with userName 'non-existing-user'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'ilir'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'shabaz'": "Test Passed!",
    "Calling /followFriend as userName 'user1' to follow friend with userName 'tahmid'": "Test Passed!",
    "Calling /followFriend with missing param 'friendUserName'": "Test Passed!",
    "Calling /getAllFriendFavouriteSongTitles to get songs of user with userName 'user1' friends likes": "Test Passed!",
    "Calling /unfollowFriend as userName 'user1' to unfollow friend with userName 'ilir'": "Test Passed!",
    "Calling rount /profile with missing params 'fullName'": "Test Passed!",
    "Calling route /profile to add user with userName 'user1'": "Test Passed!",
    "Checking DB to check if all nodes remains as expected after creating user": "Test failed! Some node(s) did not match the expected result.",
    "Checking DB to check if all nodes remains as expected after follow": "Test failed! Some node(s) did not match the expected result.",
    "Checking DB to check if all nodes remains as expected after unfollow": "Test failed! Some node(s) did not match the expected result.",
    "Checking DB to see if user 'user1' was correctly followed users with userNames 'ilir', 'tahmid' and 'shabaz'": "Test Passed!",
    "Checking DB to see if user 'user1' was correctly unfollowed users with userNames 'shabaz'": "Test Passed!",
    "Checking DB to see if user1 actually liked songId 5d620f54d78b833e34e65b46 and 5d620f54d78b833e34e65b47": "Test Failed! DB data does not match expected data",
    "Checking DB to see if user1 actually unliked songId 5d620f54d78b833e34e65b46 and 5d620f54d78b833e34e65b47": "Test Passed!",
    "Checking if the follow was one directional. Only 'user1' followed 'ilir' and not the other way around": "Test Passed!",
    "Checking if the unfollow was one directional. Only 'user1' unfollowed 'shabaz' and not the other way around": "Test Passed!",
    "Checking returned data after calling /getAllFriendFavouriteSongTitles": "Test Failed! Returned data does not match expected data",
    "Decrementing favourites count below 0 for a valid songId, id=5dfb1e38cf5a4637ec53736e": "Test Failed! Song amount favourites count was not zero",
    "Decrementing favourites count for a songId that does not exist, id=000000000000000000000000": "Test Passed!",
    "Decrementing favourites count for a valid songId, id=5d61728193528481fe5a3122": "Test Passed!",
    "Deleting song by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Deleting song by id that exists in the DB, id=5d61728193528481fe5a3122": "Test Failed! Found song in profile DB, for Users 'ilir' and 'shabaz'",
    "Getting song by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Getting song by id that exists in the DB, id=5dfb1e38cf5a4637ec53736d": "Test Passed!",
    "Getting song title by id that does not exist in the DB, id=000000000000000000000000": "Test Passed!",
    "Getting song title by id that exists in the DB, id=5d61728193528481fe5a3122": "Test Passed!",
    "Incrementing favourites count for a songId that does not exist, id=000000000000000000000000": "Test Passed!",
    "Incrementing favourites count for a songId which exists, but providing invalid ?shouldDecrement param, ?shouldDecrement=gibberish!, songId=5d61728193528481fe5a3122": "Test Failed! Response status did not match expected response status",
    "Incrementing favourites count for a valid songId id=5d61728193528481fe5a3122": "Test Passed!",
    "calling /getAllFriendFavouriteSongTitles to get songs user with userName 'ilir' likes'": "Test Passed!",
    "calling /likesong with user1 to like songId 5d620f54d78b833e34e65b46 to see if correct response type is returned": "Test Passed!",
    "calling /likesong with user1 to like songId 5d620f54d78b833e34e65b47 to see if correct response type is returned": "Test Passed!",
    "calling /unlikeSong with user1 to unlike songId 5d620f54d78b833e34e65b46": "Test Passed!",
    "calling /unlikeSong with user1 to unlike songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "checking song 5d620f54d78b833e34e65b46 in DB to see if the favorite counter is decremented by 1": "Test Passed!",
    "checking song DB to see if the favorite counter is decremented for songId 5d620f54d78b833e34e65b47": "Test Passed!",
    "checking song DB to see if the favorite counter is incremented for song with id: 5d620f54d78b833e34e65b46": "Test Passed!",
    "checking song DB to see if the favorite counter is incremented for songId 5d620f54d78b833e34e65b47": "Test Passed!"
}

**automarker grade**: 42.0/48.0

---

## code style

git usage: 1.0/2.0

- commit messages are not capitalized
- proper usage of changesets and git flow

---

code style: 2.0/4.0

- appropriate usage of variable names in both microservices
- no meaningful comments in either microservice