angular.module('agentApp').controller('AgentController', AgentController);

AgentController.$inject = [ '$http', '$scope', '$location', '$websocket', '$rootScope' ];

function AgentController($http, $scope, $location, $websocket, $rootScope) {

	$scope.sender;
	$scope.reciver;
	$scope.replyTo;
	$scope.content;
	$scope.performative;
	$scope.agentName;
	
	$http.get("agent/agents/performatives").then(function(value) {
		$rootScope.performatives = value.data;
	})
	
	$http.get("agent/agents/classes").then(function(value) {
		$rootScope.agentClasses = value.data;
	})
	
	$http.get("agent/agents/running").then(function(value) {
		$rootScope.runningAgents = value.data;
	})

	$scope.sendMessage = function() {
		var sender = JSON.parse($scope.sender);
		var reciver = JSON.parse($scope.reciver);
		var replyTo = JSON.parse($scope.replyTo);
		var aclm = {
			performative : $scope.performative,
			sender : sender,
			recivers : [ reciver ],
			replyTo : replyTo,
			content : $scope.content,
		}
		$rootScope.ws.send("aclm" + JSON.stringify(aclm));
	}
	
	$scope.createAgent = function() {
		var newAgent = {
			className : //"agents." + 
			$scope.selectedAgent,
			agentName : $scope.newAgentName
		};
		$scope.newAgentName = "";
		retMessage = "startAgent" + JSON.stringify(newAgent);
		$rootScope.ws.send(retMessage);
	}
	
	function findAgent(name) {
		for (var i = 0; i < $scope.runningAgents.length; ++i) {
			if ($scope.runningAgents[i].id.name == name) {
				return ($scope.runningAgents[i])
			}
		}
	}
};
