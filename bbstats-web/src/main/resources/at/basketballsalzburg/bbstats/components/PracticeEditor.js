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

jQuery.fn.filterByText = function(textbox, selectSingleMatch) {
	return this.each(function() {
		var select = this;
		var options = [];
		$j(select).find('option').each(function() {
			options.push({
				value : $j(this).val(),
				text : $j(this).text()
			});
		});
		$j(select).data('options', options);
		$j(textbox).bind(
				'change keyup',
				function() {
					var options = $j(select).empty().data('options');
					var search = $j.trim($j(this).val());
					var regex = new RegExp(search, "gi");

					$j.each(options, function(i) {
						var option = options[i];
						if (option.text.match(regex) !== null) {
							$j(select).append(
									$j('<option>').text(option.text).val(
											option.value));
						}
					});
					if (selectSingleMatch === true
							&& $j(select).children().length === 1) {
						$j(select).children().get(0).selected = true;
					}
				});
	});
};

$j(function() {
	$j('input#guests').keyup(function() {
		updateCounters();
	});
	$j('select#gymSelect').filterByText($j('input#gymFilter'), true);
	$j('select#playerPalette-avail').filterByText($j('input#playerFilter'),
			true);
	$j('select#coachPalette-avail').filterByText($j('input#coachFilter'), true);
	$j('select#ageGroupPalette-avail').filterByText($j('input#ageGroupFilter'),
			true);
});