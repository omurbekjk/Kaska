#Kaska app similar to instagram with firebase

# How to use this app
1. Register to [firebase](https://firebase.google.com/) and create firebase application
2. Add android application with the package name `com.alexbezhan.instagram`
3. Download `google-services.json` and add it to inside `app` folder
4. Run the app

# Description about CloudFunction and Elastic

## Description how to deploy functions
* npm install
* firebase login(for first time)
* firebase init functions(for first time,Also you need choose only your project(Kaska))
* firebase functions:config:set elasticsearch.username="user" elasticsearch.password="my_password" elasticsearch.url="http://104.154.41.53/elasticsearch/"(for config with your elastic)
* firebase deploy --only functions

## About cloud Function and Elastic
* In ElasticSearch we have two indicies: POST, USER;

* In CRUD operation in firebase, by using Cloud Function we also CRUD automatically data to Elastic;

* Now in Cloud Function we have 3 main functions:

1. Search Users List;
In Elastic we search users by ['username','name','bio','email']

REQUEST EXAMPLE:
URL FOR POST REQUEST: "https://us-central1-kgkaska.cloudfunctions.net/getUserList"
{
    "params": "username|name|bio|email"
}

RESPONSE EXAMPLE:
{
    "message": [
        {
            "name": "Alan Turing"
            "username": "Alan Turing"
            "bio": "Alan Turing"
        },
        {
            "name": "Alan Turing"
            "username": "Alan Turing"
            "bio": "Alan Turing"
        },
    ]
}

2. Search Posts by Tag;
In Elastic we search posts by tag;

REQUEST EXAMPLE:
URL FOR POST REQUEST: "https://us-central1-kgkaska.cloudfunctions.net/getPostListByTag"
{
    "params": "tag1,tag2,tag3"
}

RESPONSE EXAMPLE:
{
    "message": [
        {
            "post1": "Post1"
            "post1": "Post1"
            "post1": "Post1"
        },
        {
            "post2": "Post2"
            "post2": "Post2"
            "post2": "Post2"
        },
    ]
}

3. Search Posts by UserId;

To get user's all posts, we search posts by userId;

REQUEST EXAMPLE:
URL FOR POST REQUEST: "https://us-central1-kgkaska.cloudfunctions.net/getPostListByUserid"
{
    "params": "userUid"
}

RESPONSE EXAMPLE:
{
    "message": [
        {
            "post1": "Post1"
            "post1": "Post1"
            "post1": "Post1"
        },
        {
            "post2": "Post2"
            "post2": "Post2"
            "post2": "Post2"
        },
    ]
}
