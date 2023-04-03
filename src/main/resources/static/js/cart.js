$(initFunctions);

function initFunctions(){
	getIsLoggedIn();
	viewCart();
}

function getIsLoggedIn(){
	let url = "/api/isLoggedIn";
	getIt(url, isLoggedIn);	
}

function isLoggedIn(response){
	if(response.status=="Success"){
		let user = response.data;
		$("#nvUsername").text(user.username);
	}else{
		location.href = "/";
	}
}

function doViewBook(id){
	if(id===undefined||id===null)
		return;
	
	try{
		id = parseInt(id);
		location.href = "/viewBook?id="+id;
	}catch(e){
		
	}
}

function viewCart(){
	let url = "/api/cart";
	
	postIt(url, {}, processCart);
}

function processCart(response){
	if(response.status=="Success"){
		let cart = response.data;
		let lineItems = cart.lineItems;
		let tableBody = $("#ctTable");
		
		let str = "";
		let total = 0;
		
		for(let i=0;i<lineItems.length;i++){
			total+=lineItems[i].total;
			
			let divStr = `<tr id="ctRow${i}">
					<td>${i+1}</td>
					<td id="ctName${i}">${lineItems[i].name}</td>
					<td>${lineItems[i].isbn}</td>
					<td id="ctPrice${i}">${lineItems[i].price}</td>
					<td><button type="button" class="btn btn-close btn-danger" onclick="confirmRemoveFromCart(${i}, ${lineItems[i].bookId})" id="ctRemoveItemFromCart${i}" data-bs-toggle="modal" data-bs-target="#confirmRemoveBook"></button></td>
				</tr>`;
			
			str+=divStr;
		}
		
		tableBody.html(str);
		$("#ctTotal").text(total+"");
		
		if(lineItems.length==0){
			$("#puCheckoutBtn").prop("disabled",true);
		}else{
			$("#puCheckoutBtn").prop("disabled",false);
		}
		
	}else{
		showErrors(response.errorMessages);
	}
}

function confirmRemoveFromCart(index, id){
	$("#ctModalBookName").text($("#ctName"+index).text());
	
	//$("#ctModalBtnRemove").prop("click", "removeFromCart("+id+")");
	$("#ctModalBtnRemove").unbind("click");
	$("#ctModalBtnRemove").on("click", function(e){
		removeFromCart(id);
	});
}

function removeFromCart(id){
	
	//Just cause it is rude not to disable it.
	$("#ctModalBtnCancel").trigger("click");
	
	if(arguments.length==0){
		return;
	}
	
	try{
		id = parseInt(id);
		let url = "/api/removeFromCart?bookId="+id;
		getIt(url, processRemoveFromCart);
	}catch(e){
		
	}
}

function processRemoveFromCart(response){
	if(response.status=="Success"){
		showMessage(response.message);
		processCart(response);
	}else{
		showErrors(response.errorMessages);
	}
}

function checkout(){
	setStep1();
	
	let url = "/api/cart";
	
	postIt(url, {}, processCheckout);
}

function processCheckout(response){
	if(response.status=="Success"){
		let cart = response.data;
		
		$("#puNumberOfItems").text(""+cart.lineItems.length);
		$("#puTotalPrice").text(""+cart.total);
		
		$("#puStep1Btn").unbind("click");
		if(cart.total==0){
			showTwoSteps();
			$("#puStep1Btn").click(savePurchase);
			//$("#puStep1Btn").prop("click","savePurchase()");
		}else{
			showThreeSteps();
			$("#puStep1Btn").click(showEnterPayment);
			//$("#puStep1Btn").prop("click","showEnterPayment()");
		}
		
	}else{
		showErrors(response.errorMessages);
		$("#puModalCloseBtn").trigger("click");
	}
}

function showEnterPayment(){
	setStep2();
	queryPhoneNumber();
}

function queryPhoneNumber(){
	let url = "/api/queryPhoneNumber";
	
	postIt(url, {}, function(response){
		if(response.status=="Success"){
			let user = response.data;
			if(user.phoneNumber!=null || user.phoneNumber!="")
				$("#puPhoneNumber").val(response.data.phoneNumber);
		}else{
			location.href = "/";
		}
	});
}

function doPayment(){
	let phoneNumber = $("#puPhoneNumber").val();
	
	let erm = [];
	validatePhoneNumberMust(phoneNumber, erm);
	
	if(erm.length==0){
		setTimeout(savePurchase , 2000);
		showMessage("Sample value, but this is the place where you wait until you pay in the M-Pesa option and once we receive payment, you will proceed to download page. Wait 2 seconds, oh wait, you will not get this far into reading this message before it changes.");
	}else{
		showErrors(erm);
	}
}

function savePurchase(){
	let url = "/api/checkout";
	
	postIt(url, {}, processSavePurchase);
}

function processSavePurchase(response){
	if(response.status=="Success"){
		setStep3();
		let purchase = response.data;

		$("#puTransactionID").text(purchase.id+"");
		$("#puDownloadLink").prop("href",response.message);
		$("#puDownloadLink").text(response.message);
		
		$("#puCheckoutBtn").prop("disabled",true);
		
		let lineItems = purchase.lineItems;
		
		for(let i=0; i<lineItems.length; i++)
			$("#ctRemoveItemFromCart"+i).prop("disabled", true);
		
	}else{
		$("#puModalCloseBtn").trigger("click");
		showErrors(response.errorMessages);
	}
}

function setStep1(){
	$("#puStep1Div").show();
	$("#puStep2Div").hide();
	$("#puStep3Div").hide();
}

function setStep2(){
	$("#puStep1Div").hide();
	$("#puStep2Div").show();
	$("#puStep3Div").hide();
}

function setStep3(){
	$("#puStep1Div").hide();
	$("#puStep2Div").hide();
	$("#puStep3Div").show();
}

function showTwoSteps(){
	$("#puStep1Header").text("Step 1 of 2");
	$("#puStep2Header").text("Step 2 of 2");
}

function showThreeSteps(){
	$("#puStep1Header").text("Step 1 of 3");
	$("#puStep2Header").text("Step 2 of 3");
	$("#puStep3Header").text("Step 3 of 3");
}