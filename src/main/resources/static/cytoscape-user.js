function GetParameterValues(param) {
	var url = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	for (var i = 0; i < url.length; i++) {
		var urlparam = url[i].split('=');
		if (urlparam[0] == param) {
			return urlparam[1];
		}
	}
}

$(document).ready(function() {

	var userId = GetParameterValues('userId');
	if(userId == null) {
		userId = 1;
	}
	$.getJSON("cytoscape/users/"+userId, function(data) {
		var cy = cytoscape({
			container : document.getElementById('cy'),
			boxSelectionEnabled : false,
			autounselectify : true,

			style : cytoscape.stylesheet().selector('node').css({
				'shape': 'ellipse', 
				'height' : 80,
				'width' : 80,
				'background-fit' : 'cover',
				'border-color' : '#000',
				'border-width' : 3,
				'border-opacity' : 0.5,
				'content' : 'data(name)',
				'text-valign' : 'center',
				'color' : 'white',
				'text-outline-width' : 2,
				'text-outline-color' : '#888',
				'background-color' : '#888'
			}).selector('edge').css({
				'curve-style' : 'bezier',
				'width' : 6,
				'target-arrow-shape' : 'triangle',
				'line-color' : '#ffaaaa',
				'target-arrow-color' : '#ffaaaa'
			}).selector('node[type = "User"]').css({
				'background-color' : '#DDD', 
				'shape': 'rectangle',
				'height' : 90,
				'width' : 90,
			}).selector('node[type = "InterestType"]').css({
				'background-color' : '#AAA', 
				'shape': 'star'
			}),			
			elements : data,
			layout : {
				name : 'cose',
				directed : true,
				padding : 10
			}

		}); // cy init
	});

});
