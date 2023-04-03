$(init);

function init(){
	hideAllDivs();
	getIsLoggedIn();
}

function getIsLoggedIn(){
	let url = "/api/admin/isLoggedIn";
	getIt(url, isLoggedIn);	
}

function isLoggedIn(response){
	if(response.status=="Success"){
		let admin = response.data;
		showMessage("Welcome, "+admin.username+".");
		$("#nvUsername").text(admin.username);
	}else{
		location.href = "/admin/login"
	}
}

function doAddBook(){
	setStage("addBook");
	resetAddBook();
	$("#addAnotherBookBtn").hide();
	$("#addBookBtn").show();
}

function doViewBook(){
	setStage("viewBook");
}

function addBook(){

	let name = $("#abName").val().trim();
	let description = $("#abDescription").val().trim();
	let cover = $("#abCover")[0].files[0];
	let coverName = cover.name;
	
	let bookFile = $("#abBookFile")[0].files[0];
	let bookName = bookFile.name;
	
	let price = $("#abPrice").val().trim();
	let publisher = $("#abPublisher").val().trim();
	let year = $("#abYear").val().trim();
	let edition = $("#abEdition").val().trim();
	let country = $("#abCountry").val().trim();
	let isbn = $("#abIsbn").val().trim();
	let authors = [];
	for(let i=1;i<=5;i++){
		let auth = {};
		let authName = $("#abAuthor"+i).val().trim();
		
		if(authName!==""){
			auth.name = authName;
			authors.push(auth);
		}
	}

	let categories = [];
	for(let i=1;i<=5;i++){
		let cat = {};
		let catName = $("#abCategory"+i).val().trim();
		if(catName!=""){
			cat.name = catName;
			categories.push(cat);
		}
	}

	let width = $("#abWidth").val();
	let height = $("#abHeight").val();
	let numberOfPages = $("#abNumberOfPages").val();
	let language = $("#abLanguage").val();

	//2. Validation
	//Error Messages array
	let erm = [];
	validateBookName(name, erm);
	validateDescription(description, erm);
	validateBookPrice(price, erm);
	validatePublisherName(publisher, erm);
	validateEditionNumber(edition, erm);
	validatePublicationCountry(country, erm);
	validateIsbn(isbn, erm);
	validateAuthors(authors, erm);
	validateCategories(categories, erm);
	validateBookWidth(width, erm);
	validateBookHeight(height, erm);
	validateLanguage(language, erm);
	validateNumberOfPages(numberOfPages, erm);
	validateCover(cover, erm);
	validateBookFile(bookFile, erm);

	if(erm.length!=0){
		showErrors(erm);
	}
	else{	
		try{
			year = parseInt(year);
			edition = parseInt(edition);
			price = parseFloat(price);
			width = parseFloat(width);
			height = parseFloat(height);
			
			let coverFileName = isbn;
			let indexDot = coverName.lastIndexOf(".");
			if(indexDot>0){
				coverFileName = coverFileName + coverName.slice(indexDot);
			}
			
			let bookFileName = isbn;
			let bookDot = bookName.lastIndexOf(".");
			if(bookDot>0){
				bookFileName = bookFileName+ bookName.slice(bookDot);
			}
			
			let book = {name, coverFileName, bookFileName, description, price, publisher, year, edition, country, isbn, authors, categories, width, height, numberOfPages, language};
			
			let url = "/api/admin/addBook";
		
			postIt(url, book, processAddBook);
		}catch(err){
			showErrors("Add Book. Use the correct type of numbers to specify the year, edition price, width and height of the book.");
		}	
	}

}

function resetAddBook(){
	let textElements = ["abName","abDescription","abCover","abPrice","abWidth","abHeight","abPublisher","abEdition","abCountry"
		,"abYear","abIsbn","abNumberOfPages","abAuthor1","abAuthor2","abAuthor3","abAuthor4","abAuthor5","abCategory1","abCategory2","abCategory3","abCategory4","abCategory5"];
	for(let i=0;i<textElements.length;i++){
		$("#"+textElements[i]).val("");
	}
}

function processAddBook(){
	let messenger = arguments[0];
	if(messenger.status==="Success"){
		let book = messenger.data;
		addBookCoverAndFile(messenger.message, book.coverFileName, book.bookFileName);
	}
	else{
		showErrors(messenger.errorMessages);
	}
}

function addBookCoverAndFile(message, coverFileName, bookFileName){
	let url = "/api/admin/addBookCoverAndFile";
	
	let formData = new FormData();
	formData.append("cover", $("#abCover")[0].files[0]);
	formData.append("coverName", coverFileName);
	formData.append("bookFile", $("#abBookFile")[0].files[0]);
	formData.append("bookFileName", bookFileName);
	
	postForm(url, formData, function(){
		let response = arguments[0];
		let respStatus = response.status;
		if(respStatus==="Success"){
			showMessage(message);
			$("#addAnotherBookBtn").show();
			$("#addBookBtn").hide();
		}
		else{
			showErrors(response.errorMessages);
		}
	});
}

function getViewBook(id){
	if(id===null||id===undefined)
		return;

	let url = "/api/admin/viewBook";
	
	let formData = new FormData();
	formData.append("id",id);
	
	postForm(url, formData, showViewBook);

}

function showViewBook(){
	let response = arguments[0];
	if(response.status=="Success"){
		setStage("viewBook");
		viewBookDisableElements();
		resetViewBook();
		
		let book = response.data;
		
		$("#vbId").val(book.id);
		$("#vbName").val(book.name);
		$("#vbIsbn").val(book.isbn);
		$("#vbDescription").val(book.description);
		$("#vbPublisher").val(book.publisher);
		$("#vbYear").val(book.year);
		$("#vbLanguage").val(book.language);
		$("#vbEdition").val(book.edition);
		$("#vbCountry").val(book.country);
		$("#vbPrice").val(book.price);
		$("#vbHeight").val(book.height);
		$("#vbWidth").val(book.width);
		$("#vbNumberOfPages").val(book.numberOfPages);
		
		for(let i=0;i<book.authors.length;i++)
			$("#vbAuthor"+(i+1)).val(book.authors[i].name);
			
		for(let i=0;i<book.categories.length;i++)
			$("#vbCategory"+(i+1)).val(book.categories[i].name);
			
		setCoverBase64(book.coverFileName, book.coverBase64);
		
	}else{
		showErrors(response.errorMessages);
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
	
	$("#vbCurrentCover").prop("src",str);
}

function resetViewBook(){

	let textElements = ["vbId","vbName","vbDescription","vbCover","vbPrice","vbWidth","vbHeight","vbPublisher","vbEdition","vbCountry"
		,"vbYear","vbIsbn","vbNumberOfPages","vbAuthor1","vbAuthor2","vbAuthor3","vbAuthor4","vbAuthor5","vbCategory1","vbCategory2","vbCategory3","vbCategory4","abCategory5"];
	for(let i=0;i<textElements.length;i++){
		$("#"+textElements[i]).val("");
	}

}

/*function viewBookRequestCover(coverFileName){
	let url = "/api/requestBookCover";
	
	let formData = new FormData();
	formData.append("coverFileName", coverFileName);

	postForm(url, formData, processViewBookRequestCover);
}

function processViewBookRequestCover(cover){
	$("#vbCurrentCover").src = cover;
	let keys = Object.keys(cover);
	console.log("Number: "+keys.length+" Keys: "+keys.join(", "));
}*/

function viewBookDisableElements(){
	viewBookDisableElementsButtons();
	
	let elements = ["vbId","vbName", "vbCover", "vbDescription","vbPrice","vbPublisher","vbYear","vbCountry","vbEdition","vbAuthor1","vbAuthor2","vbAuthor3","vbAuthor4","vbAuthor5","vbCategory1","vbCategory2","vbCategory3","vbCategory4","vbCategory5","vbWidth","vbHeight","vbNumberOfPages","vbLanguage"];
	for(let i=0;i<elements.length;i++){
		$("#"+elements[i]).prop("disabled",true);
	}
}

function viewBookDisableElementsButtons(){
	$("#vbDivBtnEdit").show();
	$("#vbDivBtnCancel").hide();
	$("#vbDivBtnSave").hide();
	
	let divs = ["vbDivBtnEdit", "vbDivBtnCancel","vbDivBtnDelete","vbDivBtnSave"];
	for(let i=0;i<divs.length;i++){
		$("#"+divs[i]).removeClass("col-md-4");
		$("#"+divs[i]).addClass("col-md-6");
	}
}

function activateEditBook(){
	activateEditBookButtons();
	
	let elements = ["vbName", "vbCover", "vbDescription","vbPrice","vbPublisher","vbYear","vbCountry","vbEdition","vbAuthor1","vbAuthor2","vbAuthor3","vbAuthor4","vbAuthor5","vbCategory1","vbCategory2","vbCategory3","vbCategory4","vbCategory5","vbWidth","vbHeight","vbNumberOfPages","vbLanguage"];
	for(let i=0;i<elements.length;i++){
		$("#"+elements[i]).prop("disabled",false);
	}
}

function activateEditBookButtons(){
	$("#vbDivBtnEdit").hide();
	$("#vbDivBtnCancel").show();
	$("#vbDivBtnSave").show();
	
	let divs = ["vbDivBtnEdit", "vbDivBtnCancel","vbDivBtnDelete","vbDivBtnSave"];
	for(let i=0;i<divs.length;i++){
		$("#"+divs[i]).removeClass("col-md-6");
		$("#"+divs[i]).addClass("col-md-4");
	}
}

function cancelEditBook(){
	viewBookDisableElements();
	showMessage("No changes were made to the database.");
}

function saveEditBook(){

	let id = $("#vbId").val().trim();
	let name = $("#vbName").val().trim();
	let description = $("#vbDescription").val().trim();
	let price = $("#vbPrice").val().trim();
	let publisher = $("#vbPublisher").val().trim();
	let year = $("#vbYear").val().trim();
	let edition = $("#vbEdition").val().trim();
	let country = $("#vbCountry").val().trim();
	let isbn = $("#vbIsbn").val().trim();
	let authors = [];
	for(let i=1;i<=5;i++){
		let auth = {};
		let authName = $("#vbAuthor"+i).val().trim();
		
		if(authName!==""){
			auth.name = authName;
			authors.push(auth);
		}
	}

	let categories = [];
	for(let i=1;i<=5;i++){
		let cat = {};
		let catName = $("#vbCategory"+i).val().trim();
		if(catName!=""){
			cat.name = catName;
			categories.push(cat);
		}
	}

	let width = $("#vbWidth").val();
	let height = $("#vbHeight").val();
	let numberOfPages = $("#vbNumberOfPages").val();
	let language = $("#vbLanguage").val();

	//2. Validation
	//Error Messages array
	let erm = [];
	validateBookId(id, erm);
	validateBookName(name, erm);
	validateDescription(description, erm);
	validateBookPrice(price, erm);
	validatePublisherName(publisher, erm);
	validateEditionNumber(edition, erm);
	validatePublicationCountry(country, erm);
	validateIsbn(isbn, erm);
	validateAuthors(authors, erm);
	validateCategories(categories, erm);
	validateBookWidth(width, erm);
	validateBookHeight(height, erm);
	validateLanguage(language, erm);
	validateNumberOfPages(numberOfPages, erm);

	if(erm.length!=0){
		showErrors(erm);
	}
	else{
		try{
			year = parseInt(year);
			edition = parseInt(edition);
			price = parseFloat(price);
			width = parseFloat(width);
			height = parseFloat(height);
		
			let book = {id, name, description, price, publisher, year, edition, country, isbn, authors, categories, width, height, numberOfPages, language};
			
			let url = "/api/admin/editBook";
		
			postIt(url, book, processEditBook);
		}catch(err){
			showErrors("Use the correct type of numbers to specify the year, edition price, width and height of the book.");
		}
	}
}

function processEditBook(response){
	if(response.status=="Success"){
		showMessage(response.message);
		viewBookDisableElements();
	}else{
		showErrors(response.errorMessages);
	}
}

function confirmDeleteBook(){
	$("#vbModalBookName").text($("#vbName").text());
}

function deleteBook(){
	$("#vbModalBtnCancel").trigger("click");
	
	let id = $("#vbId").val();
	let url = "/api/admin/deleteBook";
	
	if(id==null){
		return;
	}

	let formData = new FormData();
	formData.append("id",id);
	postForm(url, formData, processDeleteBook);

}

function processDeleteBook(response){
	if(response.status=="Success"){
		showMessage(response.message);
		//viewAllBooks();
		$("#viewBook").hide();
	}else{
		showErrors(response.errorMessages);
	}
}

function viewAllBooks(){
	let url = "/api/admin/viewAllBooks";
	getIt(url,showViewAllBooks);
}

function showViewAllBooks(){
	setStage("viewAllBooks");
	let response = arguments[0];
	
	if(response.status=="Success"){
		let books = response.data;
		
		let tableStr = "";
		for(let i=0;i<books.length;i++){
			let rowStr = `<tr>
				<td>${i+1}</td>
				<td>${books[i].id}</td>
				<td>${books[i].name}</td>
				<td>${books[i].isbn}</td>
				<td>${books[i].price}</td>
				<td style="text-align-center;"><button class="btn btn-primary" onclick="getViewBook(${books[i].id})" id="vabRow${books[i].id}">View</button></td>
			</tr>`;
			tableStr+=rowStr;
		}
		
		$("#vabTableBody").html(tableStr);
	}else{
		showErrors(response.errorMessages);	
	}
}

function logout(){
	let url = "/logout";
	getIt(url,function(){
		location.href = "/admin/login";
	});
}

function setStage(id){
	hideAllDivs();
	showOneDiv(id);
}

function hideAllDivs(){
	let divs = ["addBook","viewBook", "viewAllBooks"];
	for(let i = 0;i<divs.length;i++)
		$("#"+divs[i]).hide();
}

function showOneDiv(id){
	$("#"+id).show();
}
