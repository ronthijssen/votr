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

questionServices.factory('Report', ['$resource',
    function ($resource) {
        return $resource('api/admin/showanswers/:talkId', {}, {
            query: {method: 'GET', params: { talkId: '@talkId'}, isArray: false}
        });
    }]);

questionServices.service('questionPoller', function (Question) {
    var defaultPollingTime = 5000;
    var poller;

    return {
        startPolling: function (talkId, callback) {
            poller = function () {
                Question.get({talkId: talkId}, callback);
            }
            poller();
            setInterval(poller, defaultPollingTime);
        },

        stopPolling: function () {
            clearInterval(poller);
        }
    }
});


questionServices.service('reportPoller', function (Report) {
    var defaultPollingTime = 2500;
    var poller;

    return {
        startPolling: function (talkId, callback) {
            poller = function () {
                Report.get({talkId: talkId}, callback);
            }
            poller();
            setInterval(poller, defaultPollingTime);
        },

        stopPolling: function () {
            clearInterval(poller);
        }
    }
});