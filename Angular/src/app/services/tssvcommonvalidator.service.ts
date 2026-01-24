import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Globals } from '../globals';

@Injectable({
	providedIn: 'root'
})
export class TssvcommonvalidatorService {
 
	p: any;
	constructor(private globals: Globals,
		private router: Router) { }

	isNull = function (data: any) {
		return data == null || data == undefined || data == '';
	}

	fixHTMLChars = function (s: any) {
		var txt = document.createElement("textarea");
		txt.innerHTML = s;
		return txt.value;
	}
	isAccessible() {
		var roles;
		if (typeof this.globals.roles != 'string') {
			roles = JSON.stringify(this.globals.roles);
		} else {
			roles = this.globals.roles;
		}

		if (!(roles.includes("ROLE_Administrator"))) {
			this.router.navigate(["403"]);
		}

		
		
		
		
		
		
		
		
		
		

	}

	isAdmin() {
		var roles;
		if (typeof this.globals.roles != 'string') {
			roles = JSON.stringify(this.globals.roles);
		} else {
			roles = this.globals.roles;
		}

		if (roles.includes("ROLE_Administrator")) {
			return true;
		}else{
			return false;
		}
	}


	isRegexValid = (type: any, data: any) => {
		
		if (type == "numeric") {
			this.p = /^[0-9]+$/;
		} if (type == "password") {
			this.p = /(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&#^])[A-Za-z\d@$!%*?&#^].{7,19}/;
		} else if (type == "alphabets") {
			this.p = /^[a-zA-Z]+$/;
		} else if (type == "Markdownregx") {
			this.p = /^[a-zA-Z0-9 #\-_*=.+:!\[\]()\n,&\'\"\|\/?\\%^`;~$@]+$/;
		} else if (type == "alphanumeric") {
			this.p = /^[a-zA-Z0-9]+$/;
		} else if (type == "alphanumericspace") {
			this.p = /^[a-zA-Z0-9\s]+$/;
		} else if (type == "alphanumericspace_") {
			this.p = /^[a-zA-Z0-9_\s]+$/;
		} else if (type == "name") {
			this.p = /^[.a-zA-Z0-9\s_()&,-]+$/;
		} else if (type == "filename") {
			this.p = /^[.a-zA-Z0-9 _-]+$/;
		} else if (type == "cvss3") {
			this.p = /^CVSS:3\.1\/((AV:[NALP]|AC:[LH]|PR:[UNLH]|UI:[NR]|S:[UC]|[CIA]:[NLH]|E:[XUPFH]|RL:[XOTWU]|RC:[XURC]|[CIA]R:[XLMH]|MAV:[XNALP]|MAC:[XLH]|MPR:[XUNLH]|MUI:[XNR]|MS:[XUC]|M[CIA]:[XNLH])\/)*(AV:[NALP]|AC:[LH]|PR:[UNLH]|UI:[NR]|S:[UC]|[CIA]:[NLH]|E:[XUPFH]|RL:[XOTWU]|RC:[XURC]|[CIA]R:[XLMH]|MAV:[XNALP]|MAC:[XLH]|MPR:[XUNLH]|MUI:[XNR]|MS:[XUC]|M[CIA]:[XNLH])$/;
		} else if (type == "keyvalue") {
			this.p = /^((\"([^\n\r"]+)\")\:\:(\"([^\n\r"]+)\"[\r\n]))*(\"([^\n\r"]+)\")\:\:(\"([^\n\r"]+)\")$/;
		} else if (type == "description") {
			this.p = /^[.a-zA-Z0-9\s_()&,-:/]+$/;
		} else if (type == "ip") {
			this.p = /^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
		} else if (type == "commaSeparedEmpIds") {
			this.p = /^(\d{4}|\d{5}|\d{6}|\d{7}|\d{8}|\d{9})(,\s*(\d{4}|\d{5}|\d{6}|\d{7}|\d{8}|\d{9}))*$/;
		} else if (type == "mobileNo") {
			this.p = /^(\+\d{1,3}[- ]?)?\d{10}(,(\+\d{1,3}[- ]?)?\d{10})*$/;
		} else if (type == "contact") {
			this.p = /^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*(,\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*)*$/;
		} else if (type == "ipCommaSeparated") {
			this.p = /^(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(,\s*(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)))*$/;
		} else if (type == "url") {
			this.p = /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)/;
		} else if (type == "cred") {
			this.p = /^[.a-zA-Z0-9\s_()&,-:/@#$]+$/;
		} else if (type == "ipCIDR1") {
			this.p = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/(\d|[1-2]\d|3[0-2]))?$/;
		} else if (type == "ipCIDR2") {
			this.p = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\-\b([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\b)?$/;
		} else if (type == "matchWordInPara") {
			this.p = /\W*(tcs)\W*/;
		} else if (type == "port") {
			this.p = /^0[0-9]|[0-9]\d$/;
		} else if (type == "commaSeparatedUrl") {
			this.p = /^(https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)+)(:==:\s*https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)+)*$/;
		} else if (type == 'decimal') {
			this.p = /^[0-9]+(\.[0-9][0-9]?)?/;
		} else if (type == "question") {
			this.p = /^[.a-zA-Z0-9\s_()&,-:/]+$/;
		} else if (type == 'asvsquestion') {
			this.p = /^[.a-zA-Z0-9 _\\()'&-:;=,\"?/\n]+/;
		} else if(type == "newquestion"){
			this.p = /^[.a-zA-Z0-9 _()&-?]+/;
		}
		return this.p.test(data);
	}





}
