var apiURL1;

parseMarkDownHTMLWay = function(mdText, preloadedImages, apiURL) {
	apiURL1 = apiURL;
	let md = new markdownit({ html: true})
				.use(markdownitMultimdTable, {
					multiline:  true,
		              rowspan:    true,
		              headerless: true,
				});
	
	defaultStyles = {
			h1: {
				fontSize: 16,
				bold: true,
				color: '#2E74B5'
			},
			h2: {
				fontSize: 14,
				bold: true,
				color: '#2E74B5'
			},
			h3: {
				fontSize: 13,
				bold: true,
				color: '#2E74B5'
			},
			h4: {
				fontSize: 12,
				bold: true,
				color: '#2E74B5'
			},
			h5: {
				fontSize: 11,
				bold: true,
				color: '#2E74B5'
			},
			h6: {
				fontSize: 10,
				bold: true,
				color: '#2E74B5'
			}
	};
	let html = md.render(mdText);
	let retArr = htmlToPdfmake(html,{'defaultStyles': defaultStyles, 'tableAutoSize' : true});
	
	addStylesAndImages(retArr, preloadedImages);
	
	
	return retArr;
}

function addStylesAndImages(object, preloadedImages) {

	if (typeof object === "string") {
		// Do Nothing
		//object = object;
	} else if (typeof object === "number") {
		// Do Nothing
		//object = object;
	} else if (object != null) {
		try{
			
			if(object["table"]) {
				object["layout"] = greyBorderTable;
			}
			

			if(object["image"]) {
				
				object.image = apiURL1 + object.image;
		
				if(preloadedImages[object["image"]]){
					
					object["image"] = preloadedImages[object["image"]];
					object["maxWidth"]=500;
				} else {
					
					object["image"] = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/4QBARXhpZgAATU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAACBqADAAQAAAABAAABYQAAAAD/7QA4UGhvdG9zaG9wIDMuMAA4QklNBAQAAAAAAAA4QklNBCUAAAAAABDUHYzZjwCyBOmACZjs+EJ+/+IH6ElDQ19QUk9GSUxFAAEBAAAH2GFwcGwCIAAAbW50clJHQiBYWVogB9kAAgAZAAsAGgALYWNzcEFQUEwAAAAAYXBwbAAAAAAAAAAAAAAAAAAAAAAAAPbWAAEAAAAA0y1hcHBsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALZGVzYwAAAQgAAABvZHNjbQAAAXgAAAWcY3BydAAABxQAAAA4d3RwdAAAB0wAAAAUclhZWgAAB2AAAAAUZ1hZWgAAB3QAAAAUYlhZWgAAB4gAAAAUclRSQwAAB5wAAAAOY2hhZAAAB6wAAAAsYlRSQwAAB5wAAAAOZ1RSQwAAB5wAAAAOZGVzYwAAAAAAAAAUR2VuZXJpYyBSR0IgUHJvZmlsZQAAAAAAAAAAAAAAFEdlbmVyaWMgUkdCIFByb2ZpbGUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG1sdWMAAAAAAAAAHwAAAAxza1NLAAAAKAAAAYRkYURLAAAALgAAAaxjYUVTAAAAJAAAAdp2aVZOAAAAJAAAAf5wdEJSAAAAJgAAAiJ1a1VBAAAAKgAAAkhmckZVAAAAKAAAAnJodUhVAAAAKAAAApp6aFRXAAAAFgAAAsJuYk5PAAAAJgAAAthjc0NaAAAAIgAAAv5oZUlMAAAAHgAAAyBpdElUAAAAKAAAAz5yb1JPAAAAJAAAA2ZkZURFAAAALAAAA4prb0tSAAAAFgAAA7ZzdlNFAAAAJgAAAth6aENOAAAAFgAAA8xqYUpQAAAAGgAAA+JlbEdSAAAAIgAAA/xwdFBPAAAAJgAABB5ubE5MAAAAKAAABERlc0VTAAAAJgAABB50aFRIAAAAJAAABGx0clRSAAAAIgAABJBmaUZJAAAAKAAABLJockhSAAAAKAAABNpwbFBMAAAALAAABQJydVJVAAAAIgAABS5hckVHAAAAJgAABVBlblVTAAAAJgAABXYAVgFhAGUAbwBiAGUAYwBuAP0AIABSAEcAQgAgAHAAcgBvAGYAaQBsAEcAZQBuAGUAcgBlAGwAIABSAEcAQgAtAGIAZQBzAGsAcgBpAHYAZQBsAHMAZQBQAGUAcgBmAGkAbAAgAFIARwBCACAAZwBlAG4A6AByAGkAYwBDHqUAdQAgAGgA7ABuAGgAIABSAEcAQgAgAEMAaAB1AG4AZwBQAGUAcgBmAGkAbAAgAFIARwBCACAARwBlAG4A6QByAGkAYwBvBBcEMAQzBDAEOwRMBD0EOAQ5ACAEPwRABD4ERAQwBDkEOwAgAFIARwBCAFAAcgBvAGYAaQBsACAAZwDpAG4A6QByAGkAcQB1AGUAIABSAFYAQgDBAGwAdABhAGwA4QBuAG8AcwAgAFIARwBCACAAcAByAG8AZgBpAGyQGnUoACAAUgBHAEIAIIJyX2ljz4/wAEcAZQBuAGUAcgBpAHMAawAgAFIARwBCAC0AcAByAG8AZgBpAGwATwBiAGUAYwBuAP0AIABSAEcAQgAgAHAAcgBvAGYAaQBsBeQF6AXVBeQF2QXcACAAUgBHAEIAIAXbBdwF3AXZAFAAcgBvAGYAaQBsAG8AIABSAEcAQgAgAGcAZQBuAGUAcgBpAGMAbwBQAHIAbwBmAGkAbAAgAFIARwBCACAAZwBlAG4AZQByAGkAYwBBAGwAbABnAGUAbQBlAGkAbgBlAHMAIABSAEcAQgAtAFAAcgBvAGYAaQBsx3y8GAAgAFIARwBCACDVBLhc0wzHfGZukBoAIABSAEcAQgAgY8+P8GWHTvZOAIIsACAAUgBHAEIAIDDXMO0w1TChMKQw6wOTA7UDvQO5A7oDzAAgA8ADwQO/A8YDrwO7ACAAUgBHAEIAUABlAHIAZgBpAGwAIABSAEcAQgAgAGcAZQBuAOkAcgBpAGMAbwBBAGwAZwBlAG0AZQBlAG4AIABSAEcAQgAtAHAAcgBvAGYAaQBlAGwOQg4bDiMORA4fDiUOTAAgAFIARwBCACAOFw4xDkgOJw5EDhsARwBlAG4AZQBsACAAUgBHAEIAIABQAHIAbwBmAGkAbABpAFkAbABlAGkAbgBlAG4AIABSAEcAQgAtAHAAcgBvAGYAaQBpAGwAaQBHAGUAbgBlAHIAaQENAGsAaQAg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wahZkncDBcxlZUAYBimTGxA3qw4r4R8W/wDBMLwnrOoz33h/x5qWniXZ8t9p9veNlVCsWNu1krFiMk7ASTlizEsf1GooA/If/h1V/wBVQ/8ALf8A/vjR/wAOqv8AqqH/AJb/AP8AfGv14ooA/If/AIdVf9VQ/wDLf/8AvjR/w6q/6qh/5b//AN8a/XiigD8h/wDh1V/1VD/y3/8A740f8Oqv+qof+W//APfGv14ooA/If/h1V/1VD/y3/wD740f8Oqv+qof+W/8A/fGv14ooA/If/h1V/wBVQ/8ALf8A/vjR/wAOqv8AqqH/AJb/AP8AfGv14ooA/If/AIdVf9VQ/wDLf/8AvjR/w6q/6qh/5b//AN8a/XiigD8h/wDh1V/1VD/y3/8A740f8Oqv+qof+W//APfGv14ooA/If/h1V/1VD/y3/wD740f8Oqv+qof+W/8A/fGv14ooA/If/h1V/wBVQ/8ALf8A/vjR/wAOqv8AqqH/AJb/AP8AfGv14ooA/If/AIdVf9VQ/wDLf/8AvjR/w6q/6qh/5b//AN8a/XiigD8h/wDh1V/1VD/y3/8A740f8Oqv+qof+W//APfGv14ooA/If/h1V/1VD/y3/wD740f8Oqv+qof+W/8A/fGv14ooA/If/h1V/wBVQ/8ALf8A/vjR/wAOqv8AqqH/AJb/AP8AfGv14ooA/If/AIdVf9VQ/wDLf/8AvjVi2/4JW2qTK158TJJYv4li0MRsfoxvnA/75NfrhRQB8K/Cv/gnx8Cfh5fW2t69Hd+M9Tt1QgawUOnrMAwZ0s41VGVg33Lhp1XAI+YA19zRRRQRJBAixxxqEREAVVVRgAAcAAdBUlFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAf/9H9WKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA//S/ViiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACi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";
					object["maxWidth"]=500;
				}
			}
		}catch(e){}
		Object.keys(object).forEach(function(key, index) {
			if (typeof object[key] === "string") {
				// Do Nothing
				//object[key] = object[key];
			} else if (typeof object[key] === "number") {
				// Do Nothing
				//object[key] = object[key];
			} else {
				
				addStylesAndImages(object[key],preloadedImages);
			}
		});
	}
	
	return object;
}

parseMarkDown = function(mdText,preloadedImages) {
	const md = new markdownit('default');
	
	const lstTokens = mdText.split("\n\n");
	let retArray = [];
	for (let i=0; i<lstTokens.length; i++){
		if(lstTokens[i] == ""){
			retArray.push({text:'\n'});
		} else {
			const tokens = md.parse(lstTokens[i]);
			retArray.push(...generatePDFMakeTags(tokens,preloadedImages))
		}
	}
	
	return retArray;
}

generatePDFMakeTags = function(tokens,preloadedImages) {

	let result = [];
	for (let i = 0; i < tokens.length; i++) {
		
		const currentToken = tokens[i]
		let subSection

		switch (currentToken.type) {
			case 'paragraph_open':
				subSection = selectUntilClosingTag(i, 'paragraph_close', tokens, preloadedImages)
				result.push({
					columns : generatePDFMakeTags(subSection.tokens, preloadedImages)
				})
				i = subSection.jumpToIndex;
				break;	
			case 'heading_open':
				subSection = selectUntilClosingTag(i, 'heading_close', tokens, preloadedImages)
				result.push({
					columns : generatePDFMakeTags(subSection.tokens, preloadedImages),
					bold : true,
					style : currentToken.tag
				})
				i = subSection.jumpToIndex;
				break;
			case 'bullet_list_open':
				subSection = selectUntilClosingTag(i, 'bullet_list_close', tokens, preloadedImages)
				result.push({
					ul : generatePDFMakeTags(subSection.tokens, preloadedImages),
				})
				i = subSection.jumpToIndex;
				break;
			case 'ordered_list_open':
				subSection = selectUntilClosingTag(i, 'ordered_list_close', tokens, preloadedImages)
				result.push({
					ol : generatePDFMakeTags(subSection.tokens, preloadedImages),
				})
				i = subSection.jumpToIndex;
				break;
			case 'table_open':
				subSection = selectUntilClosingTag(i, 'table_close', tokens, preloadedImages)
				result.push({
					table : {body: generatePDFMakeTags(subSection.tokens, preloadedImages)},
				})
				i = subSection.jumpToIndex;
				break;	
			case 'inline':
				
				if(currentToken.children[0] && currentToken.children[0].type=="image"){
					result.push(renderInline(currentToken.children,preloadedImages))
				} else {
					result.push({text:renderInline(currentToken.children,preloadedImages)})
				}	
				break;
			default:

		}
	}
	return result;

}

renderInline = function(tokens,preloadedImages) {
	let result = [];
	for (let i = 0; i < tokens.length; i++) {
		const currentToken = tokens[i]
		let subSection
		
		switch (currentToken.type) {
			case 'em_open':
				subSection = selectUntilClosingTag(i, 'em_close', tokens, preloadedImages)
				result.push({
					text : renderInline(subSection.tokens,preloadedImages),
					italics : true
				})
				i = subSection.jumpToIndex;
				break;
			case 'strong_open':
				subSection = selectUntilClosingTag(i, 'strong_close', tokens, preloadedImages)
				result.push({
					text : renderInline(subSection.tokens,preloadedImages),
					bold : true
				})
				i = subSection.jumpToIndex;
				break;
			case 'link_open':
				subSection = selectUntilClosingTag(i, 'link_close', tokens, preloadedImages)
				result.push({
					text : subSection.tokens[0].content,
					link : currentToken.attrs[0][1],
					decoration : "underline",
					color : "blue"	
				})
				i = subSection.jumpToIndex;
				break;
			case 'image':
				result.push({
						margin: [0, 10, 0, 10],
						image: preloadedImages[currentToken.attrs[0][1]],
						maxWidth: 500,
						alignment: 'center'
				})
				break;	
			case 'text':
				result.push(currentToken.content)
			default:
				// return
		}
	}
	if(result && Array.isArray(result) && result.length == 1){
		return result[0];
	} else {
		return result;
	}
}

selectUntilClosingTag = function(startIndex, closingTag, tokens,preloadedImages) {
	let resultTokens = []

	for (let i = startIndex + 1; i < tokens.length; i++) {
		const currentToken = tokens[i]

		if (currentToken.type === closingTag) {
			return {
				tokens : resultTokens,
				jumpToIndex : i
			}
		} else {
			resultTokens.push(currentToken)
		}
	}
}