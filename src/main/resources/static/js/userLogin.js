//$(autoLogin);

function autoLogin(){
	$("#username").val("fella");
	$("#password").val("hello");
	$("#loginBtn").trigger("click");
	//Others {[nook, ether], [kella,oppon]}
}

function userLogin(){
	let username = $("#username").val().trim();
	let password = $("#password").val();

	let erm = [];
	validateUsername(username, erm);
	validatePassword(password, erm);

	if(erm.length>0){
		showErrors(erm);
	}
	else{
		let url = "/api/processLogin";
		let user = {
			username, password
		};
		postIt(url, user, postLogin);
	}
}

function postLogin(response){
	if(response.status=="Success"){
		showMessage(response.message);
		setTimeout(function(){
			location.href="/";
		}, 1000);
		
	}else{
		showErrors(response.errorMessages);
	}
}
