const functions = require('firebase-functions');
const request = require('request-promise');
const https = require('http-https');
const elasticsearch = require('elasticsearch');

const METHODS = {
  post: 'POST',
  put: 'PUT',
  get: 'GET',
  delete: 'DELETE'
};

const INDICES = {
  users: {
    index: 'users',
    type: 'user'
  },
  posts: {
    index: 'posts',
    type: 'post'
  }
};

const userSearchParameters = ['username','name','bio','email']

const elasticSearchConfig = functions.config().elasticsearch;
const elasticAuth = {
  username: elasticSearchConfig.username,
  password: elasticSearchConfig.password
};

const userAuthData = elasticSearchConfig.username + ':' + elasticSearchConfig.password;

const client = new elasticsearch.Client({
  host: 'http://35.224.105.36/elasticsearch',
  log: 'trace',
  httpAuth: userAuthData
});

const generateRequestToElastic = (elasticSearchMethod, elasticSearchUrl, body, json) => {
  return elasticSearchRequest = {
    method: elasticSearchMethod,
    url: elasticSearchUrl,
    auth: elasticAuth,
    body: body,
    json: json
  };
}

exports.dbCrudUser = functions.database.ref('/users/{uid}').onWrite((change, context) => {
  const user_id = context.params.uid;
  const userData = change.after.val();

  const elasticSearchUrl = elasticSearchConfig.url + INDICES.users.index + '/' + INDICES.users.type + '/' + user_id;
  const requestMethod = userData ? METHODS.post : METHODS.delete;

  return request(generateRequestToElastic(requestMethod, elasticSearchUrl, userData, true)).then(response => {
    console.log(response);
  });
});

exports.dbCrudPost = functions.database.ref('/feed-posts/{user_id}/{uid}').onWrite((change, context) => {
  const post_id = context.params.uid;
  const postData = change.after.val();

  const elasticSearchUrl = elasticSearchConfig.url + INDICES.posts.index + '/' + INDICES.posts.type + '/' + post_id;
  const requestMethod = postData ? METHODS.post : METHODS.delete;

  return request(generateRequestToElastic(requestMethod, elasticSearchUrl, postData, true)).then(response => {
    console.log(response);
  });
});


exports.getUserList = functions.https.onRequest((req,res) => {
  const searchParams = req.body.params;
  var result = [];
  const response = client.search({
    index: INDICES.users.index,
    type: INDICES.users.type,
    body: {
      query: {
        multi_match: {
          query: searchParams,
          fields: userSearchParameters
        }
      }
    }
  }).then((data) => {
    data.hits.hits.map((hit) => {
      result.push(hit)
    });
    res.status(200).json({
      message: result
    });
  }).catch((err) => {
    res.status(404).json({
      message: err
    })
  })
});

exports.getPostListByUserid = functions.https.onRequest((req,res) => {
  const searchParams = req.body.params;
  var result = [];
  const response = client.search({
    index: INDICES.posts.index,
    type: INDICES.posts.type,
    body: {
      query: {
        match: {
          uid: searchParams
        }
      }
    }
  }).then((data) => {
    data.hits.hits.map((hit) => {
      result.push(hit);
    });
    res.status(200).json({
      message: result
    });
  }).catch((err) => {
    res.status(200).json({
      message: err
    });
  });
});

exports.getPostListByTag = functions.https.onRequest((req,res) => {
  const searchParams = req.body.params;
  var result = [];
  const response = client.search({
    index: INDICES.posts.index,
    type: INDICES.posts.type,
    body: {
      query: {
        term: {
          tag: searchParams
        }
      }
    }
  }).then((data) => {
    data.hits.hits.map((hit) => {
      result.push(hit)
    });
    res.status(200).json({
      message: result
    });
  }).catch((err) => {
    res.status(404).json({
      message: err
    })
  })
});
