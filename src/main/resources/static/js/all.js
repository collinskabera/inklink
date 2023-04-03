$(init);

function init(){
	getIsLoggedIn();
}

function getIsLoggedIn(){
	let url = "/api/isLoggedIn";
	getIt(url, isLoggedIn);	
}

function isLoggedIn(response){
	if(response.status=="Success"){
		let user = response.data;
		//showMessage("Hello "+user.username);
		setIsLoggedIn();
		$("#nvUsername").text(user.username);
		//Set stuff
	}else{
		setIsLoggedOut();
		//showErrors(response.errorMessages);
		//Hide stuff
	}
}

function doViewBook(id){
	if(id===undefined||id===null)
		return;
	
	try{
		id = parseInt(id);
		location.href = "/viewBook?bookId="+id;
	}catch(e){
		
	}
}

function addToCart(bookId){
	if(bookId===undefined||bookId===null)
		return;
		
	 let url = "/api/addToCart?bookId="+bookId;
	 getIt(url, processAddToCart);
}

function processAddToCart(response){
	if(response.status=="Success"){
		showMessage(response.message);
	}else{
		showErrors(response.errorMessages);
	}
}

function logout(){
	let url = "/logout";
	
	getIt(url, function(){
		setIsLoggedOut();
	});
}

//You can replace in a future date with a modal log in, pop up instead of going to another url, but for now
function login(){
	location.href = "/login";
}

function setIsLoggedIn(){
	$("#nvUsername").show();
	$("#nvCart").show();
	
	$("#nvBtnLogOut").show();
	$("#nvBtnLogIn").hide();
}

function setIsLoggedOut(){
	$("#nvUsername").text("");
	//$("#nvUsername").hide();
	$("#nvCart").hide();
	
	$("#nvBtnLogOut").hide();
	$("#nvBtnLogIn").show();
}

function getBookCovers(imgCoverNames, imgIds){
	let bookCoverUrl = "/api/requestBookCover";
		
	for(let i=0;i<imgCoverNames.length;i++){
		let formData = new FormData();
		formData.append("coverFileName", imgCoverNames[i]);
		
		postForm(bookCoverUrl, formData, function(response){
			if(response.status=="Success"){
				
				let imgStr = setCoverBase64(imgCoverNames[i],response.data);
				$("#"+imgIds[i]).prop("src", imgStr);
			}
		});
	}
}

function setCoverBase64(coverName, data){
	let str = "";
	let type = "";
	let lastIndex = coverName.lastIndexOf(".");
	if(lastIndex>0){
		type = coverName.slice(lastIndex+1);
	}
	
	str = "data:image/"+type+";base64,"+data;
	
	return str;
	
}