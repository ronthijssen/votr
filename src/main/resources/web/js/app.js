'use strict';
/*global angular */

// Declare app level module which depends on filters, and services
var votr = angular.module('votr', ['ngRoute', 'votrControllers', 'questionService', 'chartjs-directive']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/home.html' /* , controller: 'homeController' */});
        $routeProvider.when('/questions/:talkId', {templateUrl: 'partials/question.html', controller: 'QuestionDetailCtrl' });
        $routeProvider.when('/report/:talkId', {templateUrl: 'partials/report.html', controller: 'ReportCtrl' });

        $routeProvider.otherwise({redirectTo: '/'});
    }]);
