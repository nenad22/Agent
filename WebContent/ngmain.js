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
		/*	$rootScope.user = '';
			$rootScope.activeUsers = [];
			$rootScope.messages = [];

			function getRootUri() {
				return "ws://"
						+ (document.location.hostname == "" ? "localhost"
								: document.location.hostname)
						+ ":"
						+ (document.location.port == "" ? "8080"
								: document.location.port);
			}
			var wsUri = getRootUri() + "/chat/websocket";

			var ws = $websocket(wsUri);

			ws.onMessage(function(message) {

				if (message.data == "loginfalse") {
					alert("Wrong username and/or password");
				} else if (message.data == "registertrue") {
					alert("Successfully registered")
				} else if (message.data == "registerfalse") {
					alert("Username already exists");
				} else if (message.data.indexOf("logintrue") == 0) {
					$rootScope.user = JSON.parse(message.data.replace(
							"logintrue", ""));
					$location.url('/chat');
				} else if (message.data.indexOf("adduser") == 0) {
					var user = JSON.parse(message.data.replace("adduser", ""));
					$rootScope.activeUsers.push(user);

				} else if (message.data.indexOf("removeuser") == 0) {
					var user = JSON.parse(message.data.replace("removeuser", ""));
					for (var i = 0; i < $rootScope.activeUsers.length; i++) {
					    if($rootScope.activeUsers[i].username==user.username){
					    	$rootScope.activeUsers.splice(i,1);
					    	break;
					    }
					}

				}else if (message.data.indexOf("message") == 0) {
					var msg = JSON.parse(message.data.replace("message", ""));
					$rootScope.messages.push(msg);
				}else if (message.data.indexOf("users") == 0) {
					var users = JSON.parse(message.data.replace("users", ""));
					$rootScope.activeUsers=users;

				}

				/*
				 * if (message.data.indexOf("messages") == 0) {
				 * $rootScope.messages = JSON.parse(message.data.replace(
				 * "messages", "")); }else
				 */
			/*});

			ws.onOpen(function() {
				alert("Socket open");
			});
			ws.onClose(function() {
				$location.url('/login');
				alert("Socket closed");
			});
			$rootScope.ws = ws;

			$window.onbeforeunload = function() {
				if ($rootScope.user != '') {
					$rootScope.ws.send("logout"
							+ JSON.stringify($rootScope.user));
				}
				$rootScope.user = '';
				$location.url('/login');
				$rootScope.ws.close();
				$rootScope.ws.open
			};
			$window.onreset = function() {
				if ($rootScope.user != '') {
					$rootScope.ws.send("logout"
							+ JSON.stringify($rootScope.user));
				}

				$rootScope.user = '';
				$location.url('/login');
				$rootScope.ws.close();
				$rootScope.ws.open
			};*/
		});
