$(viewBookInit);

function viewBookInit(){

	let url = "/api/viewBook";
	
	let id = $("#vbId").val();

	let formData = new FormData();
	formData.append("bookId", id);
	
	postForm(url, formData, processViewBook);
}

function processViewBook(response){
	if(response.status=="Success"){
		let book = response.data;
		
		//You can use either JS or Thymeleaf, for book name I used Thymeleaf.
		//$("#vbName").text(book.name);
		$("#vbDescription").text(book.description);
		$("#vbPrice").text(book.price);
		$("#vbIsbn").text(book.isbn);
		$("#vbLanguage").text(book.language);
		$("#vbNumberOfPages").text(book.numberOfPages);
		$("#vbWidth").text(book.width);
		$("#vbHeight").text(book.height);
		$("#vbYear").text(book.year);
		$("#vbPublisher").text(book.publisher);
		$("#vbCountry").text(book.country);
		$("#vbEdition").text(book.edition);
		
		let authorStr = "";
		for(let i=0;i<book.authors.length;i++){
			authorStr+="<li>"+book.authors[i].name+"</li>";	
		}
		
		$("#vbAuthors").html(authorStr);
		
		if(book.authors.length==1){
			$("#vbAuthorHeader").text("Author");
		}
		
		let categoriesStr = "";
		for(let i=0;i<book.categories.length;i++){
			categoriesStr+="<li>"+book.categories[i].name+"</li>";
		}
		
		$("#vbCategories").html(categoriesStr);
		
		if(book.categories.length==1){
			$("#vbCategoryHeader").text("Category");
		}
		
		//$("#vbAddToCartBtn").prop("onclick","addToCart("+book.id+")");
		
		$("#vbAddToCartBtn").on("click", function(e){
			addToCart(book.id);
		});
		
		getBookCovers([book.coverFileName],["vbCover"]);
	
	}else{
		showErrors(response.errorMessages);
	}
}
