$(initSearch);

function initSearch(){
	$("#seSearch").on("keyup", function(e){
		if(e.keyCode==13||e.key=="Enter"){
			getSearch();
		}
	});
}

function getSearch(){
	let val = $("#seSearch").val().trim();
	
	let erm = [];
	validateSearch(val, erm);
	
	if(erm.length>0){
		showErrors(erm);
	}else{
		
		let url = "/api/search?value="+val;
		getIt(url, processSearch);
	}
}

function processSearch(response){

	if(response.status=="Success"){
		
		let holders = response.data;
		
		if(holders.length==0){
			showErrors("You search term did not return any results.");
			return;	
		}
			
		let str = "";
		
		let imgCoverNames = [];
		let imgIds = [];
		
		for(let i=0;i< holders.length; i++){
			let holder = holders[i];
			
			let divStr = `<div>
					<div>
						<h4 style="text-decoration: underline;">${holder.name}</h4>
					</div>`;
			
			let books = holder.books;
			for(let j=0;j<books.length;j++){
				
				let descr = books[j].description;
				if(descr.length>50){
					descr = descr.slice(0, 50)+"...";
				}
				
				let innerStr = `<div class="ik-search-content-div" onclick="doViewBook(${books[j].id})">
						<div class="row">
							<div class="col-lg-1">
								<img alt="Cover Image" title="${books[j].name}" width="65" height="90" id="seImg${i}_${j}">
							</div>
							<div class="col-lg-11">
								<div class="row">
									<div class="col-lg-12">
										<label class="form-label"><strong>Name:</strong> </label>
										<label id="seName${i}_${j}">${books[j].name}</label>
									</div>
									<div class="col-lg-4">
										<label class="form-label"><strong>ISBN:</strong> </label>
										<label id="seIsbn${i}_${j}">${books[j].isbn}</label>
									</div>
									<div class="col-lg-4">
										<label class="form-label"><strong>Price: </strong></label>
										<label id="sePrice${i}_${j}">${books[j].price}</label>
									</div>
									<div class="col-lg-4">
										<label class="form-label"><strong>Number of pages:</strong></label>
										<label id="seNumberOfPages${i}_${j}">${books[j].numberOfPages}</label>
									</div>
									<div class="col-lg-12">
										<label class="form-label"><strong>Description:</strong></label>
										<label id="seDescription${i}_${j}">${descr}</label>
									</div>
								</div>
							</div>
						</div>
					</div>`;
				
				divStr+=innerStr;
				
				imgCoverNames.push(books[j].coverFileName);
				imgIds.push("seImg"+i+"_"+j);
			}
			
			divStr+="</div>";
			str+=divStr;
		}
		
		$("#searchResultsDiv").html(str);
		
		showSearch();
		
		getBookCovers(imgCoverNames, imgIds);
	}else{
		showErrors(response.errorMessages);
	}
}

function showSearch(){
	$("#searchDiv").show();
	$("#contentDiv").hide();
}

function resetSearch(){
	$("#seSearch").val("");
	$("#searchDiv").hide();
	$("#searchResultsDiv").html("");
	$("#contentDiv").show();
}

function validateSearch(val, arr){
	if(val==null||val===""){
		arr.push("You did not enter any search term.");
		return;
	}
	if(val.length<3){
		arr.push("You search term is too short. Minimum characters is 3.");
		return;
	}
	val = val.toLowerCase();
	let notAllowed = ["the"];
	for(let i=0;i< notAllowed.length; i++){
		if(val===notAllowed[i]){
			arr.push("The search term you entered is too broad. Narrow the scope of search.");
		}
	}
}