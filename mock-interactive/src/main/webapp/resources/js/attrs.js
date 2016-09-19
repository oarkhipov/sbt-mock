/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

String.prototype.replaceAll2 = function (search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};

function showInfo(text) {
    if (text) {
        $.notify({
            //options
            icon: 'glyphicon glyphicon-info-sign',
            message: htmlDecode(text)
        }, {
            //    settings
            type: "success",
            allow_dismiss: true,
            placement: {
                from: "bottom"
            },
            delay: 2000,
            animate: {
                enter: 'animated fadeInUp',
                exit: 'animated fadeOutDown'
            },
            z_index:1100
        });
    }
}
function showError(text) {
    if (text) {
        text = text.replaceAll2("\\r\\n", "<br/>");
        text = text.replaceAll2("\\n\\r", "<br/>");
        text = text.replaceAll2("\\n", "<br/>");
        text = text.replaceAll2("\\r", "");
        $.notify({
            //options
            icon: 'glyphicon glyphicon-warning-sign',
            message: text
        }, {
            //    settings
            type: "danger",
            placement: {
                from: "bottom"
            },
            delay: 0,
            animate: {
                enter: 'animated fadeInUp',
                exit: 'animated fadeOutDown'
            },
            z_index:1100
        });
    }
}
