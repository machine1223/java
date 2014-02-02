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
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/role', {
        templateUrl: 'views/role.html',
        controller: 'RoleCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
