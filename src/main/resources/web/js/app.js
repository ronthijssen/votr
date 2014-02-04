'use strict';
/*global angular */

// Declare app level module which depends on filters, and services
var votr = angular.module('votr', ['ngRoute', 'votrControllers', 'questionService']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/home.html' /* , controller: 'homeController' */});
        $routeProvider.when('/questions/:talkId', {templateUrl: 'partials/question.html', controller: 'QuestionDetailCtrl' });

        $routeProvider.otherwise({redirectTo: '/'});
    }]);
