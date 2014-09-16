define([ "jquery" ], function($) {

	updateCounters();
	
	$$('select').each(function(item) {
        item.observe('t5:palette:didChange', function() {
	        updateCounters(); 
	    });
    });
	
	// update on entering guests
	$("input#guests").keyup(function() {
	    updateCounters();
	});
	
	function updateCounters() {
		var players = $('select#playerPalette option').length;
		if ($.trim($('input#guests').val()).length > 0) {
			players += parseInt($('input#guests').val());
		}
		$('span#playerCount').text(players);
		$('span#coachCount').text($('select#coachPalette option').length);
		$('span#ageGroupCount').text($('select#ageGroupPalette option').length);
	}
	
});