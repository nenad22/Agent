'use strict';
angular.module('agentApp', [ 'ngRoute', 'ngCookies', 'ngWebSocket' ]).config(
		function($routeProvider) {

			$routeProvider.when('/', {
				controller : 'AgentController',
				templateUrl : './agent.html',
			}).otherwise({
				redirectTo : '/'
			});
		}).run(function($rootScope, $websocket, $location, $window) {

	var wsUri = getRootUri() + "/agent/websocket";
	var ws = $websocket(wsUri);
	
	ws.onMessage(function(message) {
		var i = message.data.indexOf("{");
		var type = message.data.substring(0, i);
		var json = JSON.parse(message.data.replace(type, ""));
		switch (type) {
		case "startAgent":
			$rootScope.runningAgents.push(json);
			break;

		case "stopAgent":
			for (var i = 0; i < $rootScope.runningAgents.length; ++i) {
				if ($rootScope.runningAgents[i].aid.name == json.aid.name) {
					$rootScope.runningAgents.splice(i, 1);
					break;
				}
			}
			break;

		}
		$rootScope.runningAgents.push(message.data);
		alert("WS: " + message.data);
	});

	ws.onOpen(function() {
		alert("Socket open");
	});
	ws.onClose(function() {
		alert("Socket closed");
	});
	$rootScope.ws = ws;

	$rootScope.agentClasses = [];
	$rootScope.runningAgents = [];
	$rootScope.performatives = [];
	$rootScope.messages = [];
});

function getRootUri() {
	return "ws://"
			+ (document.location.hostname == "" ? "localhost"
					: document.location.hostname) + ":"
			+ (document.location.port == "" ? "8080" : document.location.port);
}