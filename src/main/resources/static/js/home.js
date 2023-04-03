$(homeInit);

function homeInit(){
	getHomeContent();
}

function getHomeContent(){
	let url = "/api/home";
	
	postIt(url, {}, processHomeContent);
}

function processHomeContent(response){
	if(response.status=="Success"){
		let holders = response.data;
		
		let divsStr = "";
		let imgCoverNames = [];
		let imgIds = [];
		
		for(let i=0; i<holders.length; i++){
			let holder = holders[i];
			
			let books = holder.books;
			
			let str = `<div class="row">
				<div class="col-12">
					<h3>${holder.name} ${holder.value=="" || holder.value==null ? "" : " &gt; <small>"+holder.value+"</small>"}</h3>
				</div>`;
			
			for(let j=0; j<books.length; j++){
				let innerStr = `<div class="ik-book-holder" onclick="doViewBook(${books[j].id})" id="hmDiv${i+"_"+j}">
						<div>
							<h6 style="text-decoration: underline;">${books[j].name}</h6>
						</div>
						<div>
							<img alt="${books[j].name}" id="hmImg${i+"_"+j}" class="ik-small-image-holder" title="${books[j].name}">
						</div>
						<div>
							<p>ISBN: ${books[j].isbn}</p>
						</div>
						
						<div style="text-align:center;">
							<button class="btn btn-primary" type="button" onclick="addToCart(${books[j].id})" id="hmBtn${i+"_"+j}">${books[j].price}</button>
						</div>
					</div>`;
				
				str+=innerStr;
				
				imgCoverNames.push(books[j].coverFileName);
				imgIds.push("hmImg"+i+"_"+j);
			}
			
			str+="</div>";
			
			divsStr+=str;
		}
		
		$("#contentDiv").html(divsStr);
		
		for(let i=0; i< holders.length; i++){
			for(let j=0; j< holders[i].books.length; j++){
				$("#hmDiv"+i+"_"+j).on("click", function(e){
					e.stopPropagation();
				});
				$("#hmBtn"+i+"_"+j).on("click", function(e){
					e.stopPropagation();
				});
			}
		}
		
		getBookCovers(imgCoverNames, imgIds);
		
	}else{
		showMessage(":(");
		//showMessage(response.errorMessages);
	}
}
