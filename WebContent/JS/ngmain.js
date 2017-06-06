'use strict';
angular.module('agentApp', [ 'ngRoute', 'ngCookies', 'ngWebSocket' ]).config(
		
	function($routeProvider) {

		$routeProvider.when('/', {
			controller : 'AgentController',
			templateUrl : './HTML/agent.html',
		}).otherwise({
			redirectTo : '/'
		});
	}).run(function($rootScope, $websocket, $location, $window) {

		var wsUri = getRootUri() + "/agent/websocket";
		var ws = $websocket(wsUri);
	
		ws.onMessage(function(message) {
			var i = message.data.indexOf("{");
			var type = message.data.substring(0, i);
			
			console.log(type);
			var json = "";
			
			if (type != "message"){
				 json = JSON.parse(message.data.replace(type, ""));
			}
			
			
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
		
				case "message":
					$rootScope.messages.push(message.data.replace(type, ""));
					break;
			}
			
			console.log("WS: " + message.data);
			console.log($rootScope.messages);
			
		});
	
		ws.onOpen(function() {
			console.log("WS: " + "Socket open successfully!");
		});
		
		ws.onClose(function() {
			console.log("WS: " + "Socket closed successfully!");
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