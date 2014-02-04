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


questionServices.service('questionPoller', function (Question) {
    var defaultPollingTime = 5000;

    return {
        startPolling: function (talkId, callback) {
            var poller = function () {
                Question.get({talkId: talkId}, callback);
            }
            poller();
            setInterval(poller, defaultPollingTime);
        },

        stopPolling: function (name) {
            clearInterval(polls[name]);
        }
    }
});