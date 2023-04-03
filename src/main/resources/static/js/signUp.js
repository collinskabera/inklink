function doSignUp(){
	let username = $("#suUsername").val().trim();
	let password = $("#suPassword")	.val();
	let confirmPassword = $("#suConfirm").val();
	let firstName = $("#suFirstName").val().trim();
	let lastName = $("#suLastName").val().trim();
	let gender;
	if($("#suGenderMale").prop("checked")){
		gender = "Male";
	}else if($("#suGenderFemale").prop("checked")){
		gender = "Female";
	}else{
		gender="";
	}
	let email = $("#suEmail").val().trim();
	
	let erm = [];
	validateUsername(username, erm);
	validatePassword(password, erm);
	validatePasswordsEqual(password, confirmPassword, erm);
	validateFirstName(firstName, erm);
	validateLastName(lastName, erm);
	validateEmailAddress(email, erm);
	
	//Gender can be empty.
	if(erm.length>0){
		showErrors(erm);
	}else{
		let user = {username, password, firstName, lastName, gender, email};
		
		let url = "/api/signUp";
		
		postIt(url, user, processSignUp);
	}
}

function processSignUp(response){
	if(response.status=="Success"){
		showMessage(response.message);
		setTimeout(function(){
			location.href = "/";
		}, 2000);
	}else{
		showErrors(response.errorMessages);
	}
}