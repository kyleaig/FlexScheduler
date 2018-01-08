var gridH;
var gridW;
var cal_container;
// TODO: var session = {}; to add ctrl + undo support. remove the last element

window.onload = function() {
	cal_container = document.getElementsByClassName('cal_container').item(0);
	console.log(cal_container);
	calId = cal_container.id;
	dayCols = document.getElementsByClassName('day_col');

	gridW = parseFloat(window.getComputedStyle(dayCols.item(0)).getPropertyValue('width'));
	gridH = parseFloat(window.getComputedStyle(document.body).getPropertyValue('--gridH'));
	for (var i = 0; i < dayCols.length; i++) {
		loadColListener(dayCols[i]);
	}

	// Get the new employee modal
	var newEmpModal = document.getElementById('newEmpModal');

	// Get the button that opens a new employee modal
	var newEmpBtn = document.getElementById('newEmpBtn');
	// When the user clicks on the button, open the modal
	newEmpBtn.onclick = function() {
		newEmpModal.style.display = "block";
	}

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];
	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
		newEmpModal.style.display = "none";
	}
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if (event.target == newEmpModal) {
		newEmpModal.style.display = "none";
	}
}

function loadColListener(col) {
	this.startTime;
	this.endTime;
	this.newshiftDiv;
	this.selected = null;
	this.drawing = false;
	this.dragging = false;
	this.resizing = false;
	this.mouseOffset;

	var slots = col.getElementsByClassName('timeSlot');
	col.addEventListener("mousedown", function(e) {
		if (e.button == 0) {
			if (e.target.className == 'shiftDiv' || e.target.nodeName == 'P') {
				this.selected = e.target.nodeName == 'P' ? e.target.parentNode : e.target;
				this.selected.style.opacity = 0.5;
				this.mouseOffset = e.clientY - parseFloat(this.selected.style.top);
			} else if (e.target.className == 'handle') {
				this.selected = e.target;
				this.resizing = true;
			} else {
				this.drawing = true;
				slot = e.target;
				this.selected = makeshiftDiv(e);
				//this.startTime = this.selected.style.top;
				col.appendChild(this.selected);
			}
		}
	});
	col.addEventListener("mousemove",
			function(e) {
				if (this.drawing) {
					var newHeight = (e.pageY - this.selected.offsetTop); // TODO: somethin not right about that offsetTop bein there...
					if (newHeight < 0) {
						// reverse direction.
						this.selected.style.top = parseFloat(this.selected.style.top) + Math.round(newHeight/gridH) * gridH + 'px';
					}
					newHeight = Math.abs(newHeight) <= gridH ? gridH : Math.abs(newHeight);
					this.selected.style.height = (Math.round(newHeight/gridH) * gridH) + 'px';
				} else if (this.resizing) {	// Resizing shift
					var newHeight = (e.pageY - this.selected.parentElement.offsetTop);
					newHeight = newHeight <= gridH ? gridH : Math.abs(newHeight);
					this.selected.parentElement.style.height = (Math.round(newHeight/gridH) * gridH) + 'px';
				} else if (this.selected != null) { // Dragging shift
					this.dragging = true;
					this.selected.style.cursor = 'move';
					var y = e.clientY;
					y -= this.mouseOffset;
					y = Math.round(y/gridH) * gridH;
					//console.log(y + ' + ' + this.mouseOffset);
					this.selected.style.top = y + 'px';
				}
			});
	col.addEventListener("mouseup", function(e) {
		if (this.selected != null) {
			this.selected.style.opacity = 1;
		}
		if (e.button == 0) {
			if (this.drawing) {
				// TODO: Problem with e.target not being the slot.
				slot = e.target;
				this.endTime = this.selected.offsetBottom;
				newShift(col.id, this.startTime, this.endTime, this.selected);
			} else if (this.dragging) {
				this.selected.style.cursor = 'default';
			} else {
				if (!this.resizing)
					editShift(this.selected);
			}
		}
		this.drawing = false;
		this.dragging = false;
		this.resizing = false;
		this.selected = null;
	});
}

function makeshiftDiv(e) {
	var boxDiv = document.createElement('div');
	boxDiv.className = 'shiftDiv';
	var y = e.pageY - (gridH / 2);
	y = Math.round(y / gridH) * gridH;
	boxDiv.style.top = y + 'px';
	boxDiv.style.width = '13%';
	boxDiv.style.height = gridH + 'px';
	boxDiv.style.opacity = 0.5;
	
	boxDiv.innerHTML += '<p>Kyle</p>';
	var handleSpan = document.createElement('span');
	handleSpan.className = 'handle';
	handleSpan.style.width = gridW + 'px';
	handleSpan.style.height = (gridH/3) + 'px';
	boxDiv.appendChild(handleSpan);
	return boxDiv;
}

// Creating a new shift
function newShift(day, startTime, endTime, span) {
	console.log('New shift: (' + day + ', ' + startTime + '-' + endTime + ')');
}

function editShift(shiftDiv) {
	alert("edit shift");
	var modalDiv = document.createElement('div');
	modalDiv.className = 'shiftModal';
	modalDiv.style.top = shiftDiv.style.top;
	
}