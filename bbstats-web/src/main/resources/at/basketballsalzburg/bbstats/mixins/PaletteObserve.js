define([ "t5/core/zone" ], function(zoneManager) {
	T5.extendInitializers({
		observe : function(spec) {
			$(spec.id).observe(spec.event, function() {
				var url = spec.url;
				var selected = "";
				$(spec.id).childElements().each(function(element) {
					if (!selected) {
						selected += element.value;
					} else {
						selected += "," + element.value;
					}
				});
				//selectedPlayers = $$("input[name='playerPalette']").first().value;
				url = appendQueryStringParameter(url, "selected", selected);
				//url = appendQueryStringParameter(url, "selectedPlayers", selectedPlayers);
				zoneManager.deferredZoneUpdate(spec.zone, url);
			});
		}
	});
	function appendQueryStringParameter(url, name, value) {
		if (url.indexOf('?') < 0) {
			url += '?';
		} else {
			url += '&';
		}
		value = escape(value);
		url += name + '=' + value;
		return url;
	}
});