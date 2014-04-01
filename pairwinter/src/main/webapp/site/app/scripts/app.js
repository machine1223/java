'use strict';

angular.module('siteApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: '/static/views/main.html',
        controller: 'MainCtrl'
      })
      .when('/role', {
        templateUrl: '/static/views/role.html',
        controller: 'RoleCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
