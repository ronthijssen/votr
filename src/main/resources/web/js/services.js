'use strict';

/* Services */

var questionServices = angular.module('questionService', ['ngResource']);

questionServices.factory('Question', ['$resource',
    function ($resource) {
        return $resource('api/question/:talkId', {}, {
            query: {method: 'GET', params: { talkId: '@talkId'}, isArray: false}
        });
    }]);

questionServices.factory('Answer', ['$resource',
    function ($resource) {
        return $resource('api/answer/:talkId/:questionId', {}, {
            save: {method: 'POST' }
        });
    }]);

