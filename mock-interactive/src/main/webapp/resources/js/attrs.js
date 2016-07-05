/**
 * Created by sbt-bochev-as on 16.01.2015.
 */

function getQueryString() {
    // This function is anonymous, is executed immediately and
    // the return value is assigned to QueryString!
    var query_string = {};
    //var query = isIE()?window.location.hash.substring(2):window.location.search.substring(1);
    var query = window.location.search.substring(1);
    if(window.location.hash.substring(2)) {
        query = query.length > 0 ? (query + "&" + window.location.hash.substring(2)) : window.location.hash.substring(2);
    }
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
            query_string[pair[0]] = pair[1];
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
            query_string[pair[0]] = [query_string[pair[0]], pair[1]];
            // If third or later entry with this name
        } else {
            query_string[pair[0]].push(pair[1]);
        }
    }
    return query_string;
}

function isIE() {
    return window.navigator.userAgent.indexOf( "MSIE " ) > 0;
}

if(isIE()) {
    urlSearch = location.search;
    if(urlSearch.length>0) {
        clearUrl = location.href.split("\?")[0];
        location.href = clearUrl + "#" + urlSearch;
    }
    //location.href
}

var valid;
$().ready(function () {
    eval(Base64.decode("JCgiYTpjb250YWlucygnQ29weXJpZ2h0JykiKS5jbGljayhmdW5jdGlvbiAoKSB7IEJvb3RzdHJhcERpYWxvZy5zaG93KHt0eXBlOiBCb290c3RyYXBEaWFsb2cuVFlQRV9TVUNDRVNTLCB0aXRsZTogJ0NvcHlyaWdodCcsIG1lc3NhZ2U6ICI8ZGl2IHN0eWxlPSd0ZXh0LWFsaWduOiBjZW50ZXInPjxiPtCQ0KEgJ9Ct0LzRg9C70Y/RgtC+0YAnPC9iPjxici8+INCg0LDQt9GA0LDQsdC+0YLQsNC9INCe0YLQtNC10LvQvtC8INCQ0LLRgtC+0LzQsNGC0LjQt9Cw0YbQuNC4INC4INCu0L3QuNGCINGC0LXRgdGC0LjRgNC+0LLQsNC90LjRjyw8YnIvPiDQptC10L3RgtGAINCa0L7QvNC/0LXRgtC10L3RhtC40Lgg4oSWNyw8YnIvPtCh0LHQtdGA0LHQsNC90Log0KLQtdGF0L3QvtC70L7Qs9C40LguPGJyLz48YnIvPiAyMDE0LTIwMTbCqTwvZGl2PiIgfSk7IH0pOyB2YWxpZCA9IHRydWU7"));
});

(function($) {
    $.fn.clickToggle = function(func1, func2) {
        var funcs = [func1, func2];
        this.data('toggleclicked', 0);
        this.click(function() {
            var data = $(this).data();
            var tc = data.toggleclicked;
            $.proxy(funcs[tc], this)();
            data.toggleclicked = (tc + 1) % 2;
        });
        return this;
    };
}(jQuery));
