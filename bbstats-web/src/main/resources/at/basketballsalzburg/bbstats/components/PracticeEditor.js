document.observe('dom:loaded', function() {
	var _oldUpdateHidden =

	Tapestry.Palette.prototype.updateHidden;
	Tapestry.Palette.prototype.updateHidden = function() {
		var doOldStuff = _oldUpdateHidden.bind(this);
		var doNewStuff = updateCounters.bind(this);

		doOldStuff();
		doNewStuff();
	};
});

function updateCounters() {
	var players = $j('select#playerPalette option').length;
	if ($j.trim($j('input#guests').val()).length > 0) {
		players += parseInt($j('input#guests').val());
	}
	$j('span#playerCount').text(players);
	$j('span#coachCount').text($j('select#coachPalette option').length);
	$j('span#ageGroupCount').text($j('select#ageGroupPalette option').length);
}

$j(function() {
	$j('input#guests').keyup(function() {
		updateCounters();
	});
});