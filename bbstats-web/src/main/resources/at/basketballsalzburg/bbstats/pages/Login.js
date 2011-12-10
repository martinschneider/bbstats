$j(document).ready(function() {
	$j('.login-button').attr({
		onclick : 'javascript:loginForm.submit();',
		href : 'javascript:loginForm.submit();',
		tabindex : '3'
	});

	if (!("autofocus" in document.createElement("input"))) {
		document.getElementById('usernameField').focus();
	}

	$j('.login-form input').keypress(function(event) {
		if (event.which == '13') {
			event.preventDefault();
			$j('.login-form').submit();
		}
	});
});