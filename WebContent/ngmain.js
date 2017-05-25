'use strict';
angular.module('agentApp', [ 'ngRoute', 'ngCookies', 'ngWebSocket' ]).config(
	function($routeProvider) {
		
		$routeProvider.when('/', {
			controller : 'AgentController',
			templateUrl : './agent.html',
		}).otherwise({
			redirectTo : '/'
		});
	});