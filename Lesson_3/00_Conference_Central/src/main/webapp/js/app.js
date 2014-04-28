'use strict';

/**
 * @ngdoc object
 * @name conferenceApp
 * @requires $routeProvider
 * @requires conferenceControllers
 * @requires ui.bootstrap
 *
 * @description
 * Root app, which routes and specifies the partial html and controller depending on the url requested.
 *
 */
angular.module('conferenceApp',
    ['conferenceControllers', 'ngRoute', 'ui.bootstrap']).
    config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/conference', {
                    templateUrl: '/partials/show_conferences.html',
                    controller: 'ConferenceCtrl'
                }).
                when('/conference/create', {
                    templateUrl: '/partials/create_conferences.html',
                    controller: 'ConferenceCtrl'
                }).
                when('/profile', {
                    templateUrl: '/partials/profile.html',
                    controller: 'ConferenceCtrl'
                }).
                when('/', {
                    templateUrl: '/partials/home.html'
                }).
                otherwise({
                    redirectTo: '/'
                });
        }]);

