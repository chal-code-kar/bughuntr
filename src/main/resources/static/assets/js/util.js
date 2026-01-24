fixObjectData = function (object) {
    if (typeof object === "string") {
      object = this.fixHTMLChars(object);
    } else if (typeof object === "number") {
      object = object;
    } else if (object != null) {
      Object.keys(object).forEach(function (key, index) {
        if (typeof object[key] === "string") {
          object[key] = this.fixHTMLChars(object[key]);
        } else if (typeof object[key] === "number") {
          object[key] = object[key];
        } else {
          this.fixObjectData(object[key]);
        }
      });
    }
    return object;
  }
  
  function fixHTMLChars(s) {
      var txt = document.createElement("textarea");
      txt.innerHTML = s;
      return txt.value;
  }
  
  getUniqyeValues = function(arr){
      reallyUniqueArr = arr.filter((item, pos, ar) => ar.indexOf(item) === pos)
      return reallyUniqueArr;
  }
  
  toCamelCase = function(input){
      input = input || '';
      return input.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
  }
  
  sortObject = function (object, field){
      object.sort((a, b) => a.field.localeCompare(b.field))
      return object;
  }
  
  getUniqueValues = function(arr){
      reallyUniqueArr = arr.filter((item, pos, ar) => ar.indexOf(item) === pos)
      return reallyUniqueArr;
  }
  