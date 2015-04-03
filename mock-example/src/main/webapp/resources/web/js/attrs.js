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
        location.href = clearUrl + "#?" + urlSearch;
    }
    //location.href

}