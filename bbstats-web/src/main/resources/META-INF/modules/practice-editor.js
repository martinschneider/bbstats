define([ "jquery" ], function($) {
	// initialize counters
	updateCounters();
	initializeListeners();
	
	// update on entering guests
	$("input#guests").keyup(function() {
		updateCounters();
	});

	// update on using the palette 
	$("div.palette div.palette-controls button").on("click", function() {
		updateCounters();
		initializeListeners();
	});
	$("div.palette select option").on("dblclick", function() {
		updateCounters();
		initializeListeners();
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
	
	function initializeListeners()
	{
		$("input[name='coachPalette']").parent("div.palette").find("div.palette-available select option").on("click", function() {
			$("input[name='coachPalette']").parent("div.palette").find("button[data-action='select']").focus();
		});
		
		$("input[name='playerPalette']").parent("div.palette").find("div.palette-available select option").on("click", function() {
			$("input[name='playerPalette']").parent("div.palette").find("button[data-action='select']").focus();
		});
		
		$("input[name='ageGroupPalette']").parent("div.palette").find("div.palette-available select option").on("click", function() {
			$("input[name='ageGroupPalette']").parent("div.palette").find("button[data-action='select']").focus();
		});
		
		$("input[name='coachPalette']").parent("div.palette").find("div.palette-selected select option").on("click", function() {
			$("input[name='coachPalette']").parent("div.palette").find("button[data-action='deselect']").focus();
		});
		
		$("input[name='playerPalette']").parent("div.palette").find("div.palette-selected select option").on("click", function() {
			$("input[name='playerPalette']").parent("div.palette").find("button[data-action='deselect']").focus();
		});
		
		$("input[name='ageGroupPalette']").parent("div.palette").find("div.palette-selected select option").on("click", function() {
			$("input[name='ageGroupPalette']").parent("div.palette").find("button[data-action='deselect']").focus();
		});
		
		$("button[data-action='deselect']").on("click", function() {
			$(this).siblings("button[data-action='select']").focus();
		});
		
		$("button[data-action='select']").on("click", function() {
			$(this).siblings("button[data-action='deselect']").focus();
		});
	}
});