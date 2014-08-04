define(
		[ "jquery" ],
		function($) {
			return function() {
				displayResult();
				if ($("input#zero").is(':checked')) {
					$("span#results").hide();
				} else {
					$("span#results").show();
				}
				$("input.scoreA,input.scoreB,input.scoreAV,input.scoreBV")
						.keydown(
								function(event) {
									if (event.keyCode == 46
											|| event.keyCode == 8
											|| event.keyCode == 9
											|| event.keyCode == 27
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

				$("input.scoreA,input.scoreB,input.scoreAV,input.scoreBV")
						.keyup(function() {
							displayResult();
						});
				$("input#zero").click(function() {
					if ($(this).is(':checked')) {
						$("span#results").hide();
					} else {
						$("span#results").show();
					}
				});
			}

			function displayResult() {
				var scoreA = 0;
				var scoreB = 0;
				var winner = "";
				$("input.scoreA").each(function() {
					scoreA += Number($(this).val());
				});
				$("input.scoreB").each(function() {
					scoreB += Number($(this).val());
				});

				if (scoreA == scoreB) {
					$("span.overtime").removeClass("hidden");
				} else {
					$("span.overtime").addClass("hidden");
					$("input.scoreAV,input.scoreBV").val(0);
				}

				$("input.scoreAV").each(function() {
					scoreA += Number($(this).val());
				});
				$("input.scoreBV").each(function() {
					scoreB += Number($(this).val());
				});
				$("span#scoreA").text(scoreA);
				$("span#scoreB").text(scoreB);
				if (scoreA > scoreB) {
					winner = $("input[name='teamASelect']").val();
				} else if (scoreB > scoreA) {
					winner = $("input[name='teamBSelect']").val();
				}
				if (winner != "") {
					$("span#winner").text(winner);
				}
			}
		})