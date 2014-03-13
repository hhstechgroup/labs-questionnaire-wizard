function wizardDataGridHandler(cellID, gridValidatorID) {
	var fullID = wizardGetDataGridHandlerFullID(gridValidatorID);
	document.getElementById(fullID).value = cellID;
}

function wizardTraceAllIds() {
	str = wizardGetAllIds();
	alert(str);
}

function wizardGetAllIds() {
	var allElements = document.getElementsByTagName("*");
	var allIds = [];
	var str = '';
	for (var i = 0, n = allElements.length; i < n; ++i) {
		var el = allElements[i];
		if (el.id) {
			allIds.push(el.id);
			str += '*' + el.id + '*\n';
		}
	}
	return str;
}

function wizardGetDataGridHandlerFullID(gridValidatorID) {
	var result = '';
	var ids = wizardGetAllIds();
	var leftBound = 0;
	var rightBound = 0;
	if (ids.indexOf(gridValidatorID) != -1) {
		for (var i = ids.indexOf(gridValidatorID, 0); i >= 0; i--) {
			if (ids.charAt(i) == '*') {
				leftBound = i + 1;
				break;
			}
		}
		for (var i = ids.indexOf(gridValidatorID, 0); i < ids.length; i++) {
			if (ids.charAt(i) == '*') {
				rightBound = i;
				break;
			}
		}
		result = ids.substring(leftBound, rightBound);
	}
	return result;
}