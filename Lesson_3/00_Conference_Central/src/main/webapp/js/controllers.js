'use strict';

/**
 * The root conferenceApp module.
 *
 * @type {conferenceApp|*|{}}
 */
var conferenceApp = conferenceApp || {};

/**
 * @ngdoc module
 * @name conferenceControllers
 *
 * @description
 * Angular module for controllers.
 *
 */
conferenceApp.controllers = angular.module('conferenceControllers', []);

/**
 * @ngdoc controller
 * @name ConferenceCtrl
 *
 * @description
 * A controller used for each partial htmls.
 */
conferenceApp.controllers.controller('ConferenceCtrl', function ($scope, $log) {

    /**
     * Invokes the conference.saveProfile API.
     *
     */
    $scope.saveProfile = function () {
        gapi.client.conference.saveProfile({
            displayName: $scope.displayName,
            notificationEmail: $scope.notificationEmail}).
            execute(function (resp) {
                // TODO thagikura give a user feedback.
                $log.info(JSON.stringify(resp));
            });
    };

    /**
     * Invokes the conference.createConference API.
     *
     * @param conferenceForm the form object.
     */
    $scope.createConference = function (conferenceForm) {
        $scope.submitted = true;
        if (conferenceForm.$invalid) {
            return;
        }

        $scope.loading = true;
        gapi.client.conference.createConference($scope.conference).
            execute(function (resp) {
                if (resp.code) {
                    // The request has failed.
                    var errorMessage = resp.error.message || '';
                    $scope.messages = 'Failed to create a conference : ' + errorMessage;
                    $scope.alertStatus = 'warning';
                    $log.error('Failed to create a conference. ErrorMesasge : ' + errorMessage +
                        'Conference : ' + JSON.stringify($scope.conference));
                } else {
                    // The request has succeeded.
                    $scope.messages = 'The conference has been created';
                    $scope.alertStatus = 'success';
                    $scope.submitted = false;
                    $scope.conference = {};
                    $log.info('The conference has been created : ' + JSON.stringify(resp.result));
                }
                $scope.loading = false;
                $scope.$apply();
            });
    };
});

/**
 * @ngdoc controller
 * @name RootCtrl
 *
 * @description
 * The root controller having a scope of the body element and methods used in the application wide
 * such as user authentications.
 *
 */
conferenceApp.controllers.controller('RootCtrl', function ($scope) {
    $scope.CLIENT_ID = 'your-client-id';
    $scope.SCOPES = 'https://www.googleapis.com/auth/userinfo.email';
    $scope.signedIn = false;
    $scope.signInButtonImage = 'img/Red-signin_Medium_base_32dp.png';

    /**
     * Callback function for the OAuth2.
     */
    $scope.oauth2callback = function () {
        gapi.client.oauth2.userinfo.get().execute(function (resp) {
            $scope.signedIn = true;
            $scope.profileImageUrl = resp.picture;
        });
    };

    /**
     * Calls the OAuth2 authentication method.
     */
    $scope.signIn = function () {
        gapi.auth.authorize({client_id: $scope.CLIENT_ID,
                scope: $scope.SCOPES, immediate: true},
            $scope.oauth2callback);
    };

    /**
     * Logs out the user.
     */
    $scope.signOut = function () {
        gapi.auth.signOut();
        $scope.signedIn = false;
    }
});

/**
 * @ngdoc controller
 * @name DatepickerCtrl
 *
 * @description
 * A controller that holds properties for a datepicker.
 */
conferenceApp.controllers.controller('DatepickerCtrl', function ($scope) {
    $scope.today = function () {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function (date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function () {
        $scope.minDate = ( $scope.minDate ) ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.dateOptions = {
        'year-format': "'yy'",
        'starting-day': 1
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate'];
    $scope.format = $scope.formats[0];
});
