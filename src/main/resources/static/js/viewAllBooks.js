$(getOriginalBooks);

function getOriginalBooks(){
	getViewAllBooks(1);
}

function getViewAllBooks(page){
	
	try{
		page = parseInt(page);
		let formData = new FormData();
		formData.append("page",page);
		
		let url = "/api/viewAllBooks";
		
		postForm(url, formData, processViewBooks);
	}catch(e){
		
	}
}

function processViewBooks(response){
	if(response.status=="Success"){
		//Index 0 is page and index 1 is PAGE_SIZE
		let arr = response.message.split(",");
		
		//To be used by bootstrap for pagination
		let page = parseInt(arr[0]);
		let pageSize = parseInt(arr[1]);
		
		let books = response.data;
		
		let contentDiv = $("#contentDiv");
		let str = "";
		
		let imgCoverNames = [];
		let imgIds = [];
		
		for(let i=0;i<books.length;i++){
			let divStr = `<div class="ik-book-holder" onclick="doViewBook(${books[i].id})" id="vabDiv${i}">
					<div>
						<h6 style="text-decoration: underline;">${books[i].name}</h6>
					</div>
					<div>
						<img alt="${books[i].name}" id="vabImg${i}" class="ik-small-image-holder" title="${books[i].name}">
					</div>
					<div>
						<p>ISBN: ${books[i].isbn}</p>
					</div>
					
					<div style="text-align:center;">
						<button class="btn btn-primary" type="button" onclick="addToCart(${books[i].id})" id="vabBtn${i}">${books[i].price}</button>
					</div>
				</div>`;
				
			imgCoverNames.push(books[i].coverFileName);
			imgIds.push("vabImg"+i);
			
			str+=divStr;
		}
		
		/**<div>
			<p>${books[i].authors.length>1 ? "1. ": ""} ${books[i].authors[0].name}</p>
		</div> */
		
		contentDiv.html(str);
		
		for(let i=0;i<books.length;i++){
			$("#vabDiv"+i).on("click", function(e){
				e.stopPropagation();
			});
			$("#vabBtn"+i).on("click", function(e){
				e.stopPropagation();
			});
		}
		
		//Load images afterwards
		getBookCovers(imgCoverNames, imgIds);
	}else{
		
	}
}


