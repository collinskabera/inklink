function postIt(url, data, callback){
	url = getSiteUrl()+url;
	
	$.ajax({
		url: url,
		type: "POST",
		data: JSON.stringify(data),
		dataType: "json",
		contentType:"application/json; charset=utf-8",
		success: callback
	});
}

function postForm(url, data, callback){
	url = getSiteUrl()+url;
	
	$.ajax({
		url: url,
		type: "POST",
		contentType : false,
		processData : false,
		data: data,
		success: callback
	});
}

function getIt(url, callback){
	url = getSiteUrl()+url;
	
	$.get(url, callback);
}

function getSiteUrl(){
	let curr = location.href;
	let index = curr.indexOf("/");
	if(index!==-1){
		curr = curr.slice(0, index);
	}
	
	return curr;
}

function showErrors(erm){

	let toastDiv = `<div class="toast show" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">
			<div class="toast-header">
				<!--<img src="..." class="rounded me-2" alt="...">-->
				<strong class="me-auto">InkLink Bookshop</strong>
				<!--<small class="muted">just now</small>-->
				<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
			</div>
			<div class="toast-body">
				${Array.isArray(erm) ? erm.join("<br>") : erm}
			</div>
		</div>`;
		
	$("#toastContainer").append(toastDiv);
}

function showMessage(message){
	
	let toastDiv = `<div class="toast show" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">
			<div class="toast-header">
				<!--<img src="..." class="rounded me-2" alt="...">-->
				<strong class="me-auto">InkLink Bookshop</strong>
				<!--<small class="muted">just now</small>-->
				<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
			</div>
			<div class="toast-body">
				${Array.isArray(message) ? message.join("<br>") : message}
			</div>
		</div>`;
		
	$("#toastContainer").append(toastDiv);
}
