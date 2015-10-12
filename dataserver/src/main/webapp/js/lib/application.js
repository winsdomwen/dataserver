/**
 * js全局变量 User: kuhn Date: 12-8-17 Time: 上午11:42 To change this template use
 * File | Settings | File Templates.
 */
Application = {
	contextPath : '', // 上下文路径
	version : ''// 版本
};
// 初始化application
(function() {
	Application.contextPath = "/apts3";
	var contextPath = document.location.pathname;
	var index = contextPath.substr(1).indexOf("/");
	contextPath = contextPath.substr(0, index + 1);
	delete index;
	Application.contextPath = contextPath;
	Application.version = "3.07";

})();

/**
 * 字符串格式化
 * 
 * @return {*}
 */
String.prototype.format = function() {
	if (arguments.length == 0)
		return this;
	for ( var s = this, i = 0; i < arguments.length; i++) {
		s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
	}
	return s;
};

// 复写index，提供忽略大小写
String.prototype._indexOf = String.prototype.indexOf;
String.prototype.indexOf = function() {
	if (typeof (arguments[arguments.length - 1]) != 'boolean')
		return this._indexOf.apply(this, arguments);
	else {
		var bi = arguments[arguments.length - 1];
		var thisObj = this;
		var idx = 0;
		if (typeof (arguments[arguments.length - 2]) == 'number') {
			idx = arguments[arguments.length - 2];
			thisObj = this.substr(idx);
		}
		var re = new RegExp(arguments[0], bi ? 'i' : '');
		var r = thisObj.match(re);
		return r == null ? -1 : r.index + idx;
	}
};

/**
 * 包命名
 * 
 * @param p
 */
function package(p) {
	var pf = p.split(".");
	if (typeof window[pf[0]] == "undefined") {
		window[pf[0]] = {};
	}
	var cur = window[pf[0]];
	for ( var i = 1; i < pf.length; i++) {
		if (typeof cur[pf[i]] == "undefined") {
			cur[pf[i]] = {};
		}
		cur = cur[pf[i]];
	}
}
