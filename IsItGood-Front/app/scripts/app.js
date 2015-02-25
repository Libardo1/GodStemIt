'use strict';

// App libraries
var app = angular.module('oauth', [
  'oauth.directive',      // login directive
  'oauth.accessToken',    // access token service
  'oauth.endpoint',       // oauth endpoint service
  'oauth.profile',        // profile model
  'oauth.interceptor'     // bearer token interceptor
]);

angular.module('oauth').config(['$locationProvider','$httpProvider',
  function($locationProvider, $httpProvider) {
    $httpProvider.interceptors.push('ExpiredInterceptor');
  }]);

app.controller('PredictionController',function($scope){
  $scope.sentence = 'Wpisz tutaj jakieś głupie zdanie...';
  $scope.mood = '-';
  $scope.prob = 0;

  $scope.wipe = function() {
    $scope.sentence = '';
    $scope.mood = '-';
    $scope.prob = 0;
  }

  $scope.predict = function() {
    console.log("Calling prediction for: " + $scope.sentence)
    $scope.prob = $scope.sentence.length % 100
    $scope.mood = "POZYTYWNIE"
  }
});
