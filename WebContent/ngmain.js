'use strict';
angular.module('agentApp', [ 'ngRoute', 'ngCookies', 'ngWebSocket' ]).config(
		function($routeProvider) {

			$routeProvider.when('/', {
				controller : 'AgentController',
				templateUrl : './agent.html',
			}).otherwise({
				redirectTo : '/'
			});
		}).run(
		function($rootScope, $websocket, $location, $window) {
			$rootScope.user = '';
			$rootScope.activeUsers = [];
			$rootScope.messages = [];

			var wsUri = getRootUri() + "/agent/websocket";
			var ws = $websocket(wsUri);

			ws.onMessage(function(message) {
				//TODO establish protocol
			});

			ws.onOpen(function() {
				alert("Socket open");
			});
			ws.onClose(function() {
				alert("Socket closed");
			});
			$rootScope.ws = ws;

			
		});

function getRootUri() {
	return "ws://"
			+ (document.location.hostname == "" ? "localhost"
					: document.location.hostname)
			+ ":"
			+ (document.location.port == "" ? "8080"
					: document.location.port);
}