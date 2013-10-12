$j(document).ready(
		function() {
			init();
			$("periodsZone").observe(Tapestry.ZONE_UPDATED_EVENT,
					function(event) {
						init();
					});
		});

function init() {
	displayResult();
	$j("input.scoreA,input.scoreB,input.scoreAV,input.scoreBV").keydown(
			function(event) {
				if (event.keyCode == 46 || event.keyCode == 8
						|| event.keyCode == 9 || event.keyCode == 27
						|| (event.keyCode == 65 && event.ctrlKey === true)
						|| (event.keyCode >= 35 && event.keyCode <= 39)) {
					return;
				} else {
					if ((event.keyCode < 48 || event.keyCode > 57)
							&& (event.keyCode < 96 || event.keyCode > 105)) {
						event.preventDefault();
					}
				}
			});

	$j("input.scoreA,input.scoreB,input.scoreAV,input.scoreBV").keyup(function() {
		displayResult();
	});
}

function displayResult() {
	var scoreA = 0;
	var scoreB = 0;
	$j("input.scoreA").each(function() {
		scoreA += Number($j(this).val());
	});
	$j("input.scoreB").each(function() {
		scoreB += Number($j(this).val());
	});

	if (scoreA == scoreB) {
		$j("span.overtime").removeClass("hidden");
	} else {
		$j("span.overtime").addClass("hidden");
		$j("input.scoreAV,input.scoreBV").val(0);
	}

	$j("input.scoreAV").each(function() {
		scoreA += Number($j(this).val());
	});
	$j("input.scoreBV").each(function() {
		scoreB += Number($j(this).val());
	});
	$j("span#scoreA").text(scoreA);
	$j("span#scoreB").text(scoreB);
}