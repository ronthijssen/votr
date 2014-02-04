'use strict';
/*global angular */

// Declare app level module which depends on filters, and services
var myApp = angular.module('myApp', ['ngRoute']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/home.html' /* , controller: 'homeController' */});

        $routeProvider.otherwise({redirectTo: '/'});
    }]);
