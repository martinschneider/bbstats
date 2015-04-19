// A class that updates a zone on any client-side event.
// Based on http://tinybits.blogspot.com/2010/03/new-and-better-zoneupdater.html
// and some help from Inge Solvoll.

ZoneUpdater = Class.create({

    initialize: function (spec) {
	    var $j = jQuery.noConflict();
        this.element = $j('[id="' + spec.elementId + '"]');
        this.listenerURI = spec.listenerURI;
	    $j.extend(this.element, {getStorage: function () {
            return {zoneId: spec.zoneId};
        }});
        this.element.data('zoneId', spec.zoneId);

        if (spec.clientEvent) {
            this.clientEvent = spec.clientEvent;
            this.element.on(this.clientEvent, this.updateZone.bindAsEventListener(this));
        }
    },

    updateZone: function () {
	    var $j = jQuery.noConflict();
        var zoneManager = Tapestry.findZoneManagerForZone(this.element.data('zoneId'));

        if (!zoneManager) {
            return;
        }

        var listenerURIWithValue = this.listenerURI;

        var parameters = {};
        if (this.element.val()) {
            var param = this.element.val();
            if (param) {
                //listenerURIWithValue = addQueryStringParameter(listenerURIWithValue, 'param', param);
                parameters["param"] = param;
            }
        }

        zoneManager.updateFromURL(listenerURIWithValue, parameters);
    }

});

function addQueryStringParameter(url, name, value) {
    if (url.indexOf('?') < 0) {
        url += '?'
    } else {
        url += '&';
    }
    value = escape(value);
    url += name + '=' + value;
    return url;
}