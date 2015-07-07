/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *******************************************************************************/
/* Script for fixing PNG transparency problem in IE 6. */
var arVersion = navigator.appVersion.split("MSIE")
var version = parseFloat(arVersion[1])
var isFirefox = navigator.appName == "Netscape";

function trimString(string){
	if (!string){
		return "";
	}
	return string.replace(/^\s+|\s+$/g,'');
}

function addRowIfNotNull(element, title, width){
	return addRowIfNotNullWithExtra(element, "", title, width);
}

function addRowIfNotNullWithExtra(element, extra, title, width){
	element = trimString(element);
	if (!element) {
		return "";
	}
	extra = trimString(extra);
	return "<tr><td width=" + width + ">" + title + "</td><td>" + element + extra + "</td></tr>";
}

function padIfNotNull(element, prefix, suffix){
	element = trimString(element);
	if (!element) {
		return "";
	}
	return prefix + element + suffix;
}

function fixPNG(myImage) 
{
    if ((version >= 5.5) && (version < 7) && (document.body.filters)) 
    {
          var img = myImage;
	      var imgName = img.src.toUpperCase()
	      if (imgName.substring(imgName.length-3, imgName.length) == "PNG")
	      {
		     var imgID = (img.id) ? "id='" + img.id + "' " : ""
		     var imgClass = (img.className) ? "class='" + img.className + "' " : ""
		     var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "
		     var imgStyle = "display:inline-block;" + img.style.cssText 
		     var imgAttribs = img.attributes;
		     for (var j=0; j<imgAttribs.length; j++)
			 {
			    var imgAttrib = imgAttribs[j];
			    if (imgAttrib.nodeName == "align")
			    {		  
			       if (imgAttrib.nodeValue == "left") imgStyle = "float:left;" + imgStyle
			       if (imgAttrib.nodeValue == "right") imgStyle = "float:right;" + imgStyle
			       break
			    }
             }
		     var strNewHTML = "<span " + imgID + imgClass + imgTitle
		     strNewHTML += " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"
	         strNewHTML += "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
		     strNewHTML += "(src='" + img.src + "', sizingMethod='scale');\""
		     strNewHTML += " onmouseover=\"PNGswap('" + img.id + "');\" onmouseout=\"PNGswap('" + img.id +"');\""
		     strNewHTML += "></span>" 
		     img.outerHTML = strNewHTML
	      }  
    }
}

function PNGswap(myID)
{
   var strOver  = "_2"
   var strOff = "_1"
   var oSpan = document.getElementById(myID)
   var currentAlphaImg = oSpan.filters(0).src
   if (currentAlphaImg.indexOf(strOver) != -1)
      oSpan.filters(0).src = currentAlphaImg.replace(strOver,strOff)
   else
      oSpan.filters(0).src = currentAlphaImg.replace(strOff,strOver)
}

function imgSwap(oImg)
{
    if ((version >= 5.5) && (version < 7) && (document.body.filters)) 
   {
    PNGswap(oImg.id);
    return;
   }
   var strOver  = "_h"    // image to be used with mouse over
   var strOff = "_n"     // normal image
   var strImg = oImg.src
   if (strImg.indexOf(strOver) != -1) 
      oImg.src = strImg.replace(strOver,strOff);
   else
      oImg.src = strImg.replace(strOff,strOver);
}

    var IMG_PATH = "images/";

    function hlpOver(toggle) {
        var img = document.images["helpicon"];
        if (img) {
            img.src = IMG_PATH + (toggle ? "help_h.png" : "help_n.png");
        }
    }

    function exitOver(toggle, img) {
        if (img) {
            img.src = IMG_PATH + (toggle ? "exit_btn2.png" : "exit_btn.png");
        }
    }

    function showHelp(e) {
        if (!e) e = window.event;

        var hlpDiv = document.getElementById("helpContent");
        
        hlpDiv.style.visibility = "visible";
        
        addEvent(document, "click", hideHelp);
        addEvent(hlpDiv, "click", preventHiding);
        
        hideElements("SELECT");
        
        e.cancelBubble = true;
        if (e.stopPropagation) e.stopPropagation();

    }
    
    function hideElements(elmType) {
        var selects = document.getElementsByTagName(elmType);
        for( i = 0; i < selects.length; i++ ) {
            var elm = selects[i];
            if (elm) {
                elm._oldState = elm.style.visibility;
                elm.style.visibility = "hidden";
            }
        }
    }
    
    function restoreElements(elmType) {
        var selects = document.getElementsByTagName(elmType);
        for( i = 0; i < selects.length; i++ ) {
            var elm = selects[i];
            if (elm && (typeof elm._oldState != 'undefined')) {
                elm.style.visibility = elm._oldState;
            }
        }
    }
    function preventHiding(e) {
         if (!e) e = window.event;
         e.cancelBubble = true;
         if (e.stopPropagation) e.stopPropagation();
    }
    
    function hideHelp() {
        var hlpDiv = document.getElementById("helpContent");

        removeEvent(document, "click", hideHelp);
        removeEvent(hlpDiv, "click", preventHiding);
        hlpDiv.style.visibility = "hidden";
        restoreElements("SELECT");
    }

    /*
     * MultiLingual InputBoxControl.
     */
     
    function mlOpenFields(e, ctrlId) {
        if (!e) e = window.event;
        
        if (window._currMLField != ctrlId) {
            // close old one first
            mlHideFields(e);
        }
        
        mlGotSubFocus(e);
        window._currMLField = ctrlId;
        var elm = document.getElementById("div_" + ctrlId);
        var inpTbl = document.getElementById("tbl_" + ctrlId);
        if (elm) {
            addEvent(document, "click", mlHideFields);
            addEvent(inpTbl, "click", preventHiding);
            addEvent(elm, "click", preventHiding);
            elm.style.visibility= "visible";
        } else {
            alert("no element div_" + ctrlId);
        }
    }
    
    function mlHideFields(e) {
        if (window._currMLField != null) {
            removeEvent(document, "click", mlHideFields);
            var elm = document.getElementById("div_" + window._currMLField);
            var inpTbl = document.getElementById("tbl_" + window._currMLField);
            if (elm) {
                removeEvent(inpTbl, "click", preventHiding);
                removeEvent(elm, "click", preventHiding);
                elm.style.visibility = "hidden";
            }
            window._currMLField = null;
        }
    }
    
    function mlLostFocus(e) {
        if (!e) e = window.event;
        var elm = target_getTarget(e);
        if (window._newName == elm._newName) {
            window._newName = "";
        }
        window.setTimeout("mlHideFieldsOnBlur()", 500);
    }
    
    function mlGotSubFocus(e) {
        if (!e) e = window.event;
        var elm = target_getTarget(e);
        if (elm) {
            window._newName = elm.name;
        }
    }
    
    function mlHideFieldsOnBlur() {
        if(window._newName == "") {
            mlHideFields();
        }
        window._newName = "";
    }
    
    function mlChanged(ctrlId, lang) {
    
        var keyMain = 'fld_' + ctrlId + '.' + lang;
        var fldMain = document.forms['jwicform'].elements[keyMain];
        var frm = document.forms['jwicform'];
        var key = 'fld_' + ctrlId;
        for (var i = 0; i < frm.elements.length; i++) {
            var elm = frm.elements[i];
            var idx = elm.name.lastIndexOf('.');
            if (idx != -1) {
                var prefix = elm.name.substr(0, idx);
                if (prefix == key && elm.name != keyMain && elm.value == fldMain._old)  {
                    elm.value = fldMain.value;
                }
            }
        }
        
        fldMain._old = fldMain.value;
    
    }
    
    /**
     *  Helpfull functions.
     */    
    
	/**
	 * Returns the event target.
	 */
	function target_getTarget(e) {
		if (e.target) {
			return e.target;
		} else {
			return e.srcElement;
		}
	}

    function addEvent( obj, type, fn )
    {
	    if (obj.addEventListener)
		    obj.addEventListener( type, fn, false );
	    else if (obj.attachEvent)
	    {
		    obj["e"+type+fn] = fn;
		    obj[type+fn] = function() { obj["e"+type+fn]( window.event ); }
		    obj.attachEvent( "on"+type, obj[type+fn] );
	    }
    }

    function removeEvent( obj, type, fn )
    {
	    if (obj.removeEventListener)
		    obj.removeEventListener( type, fn, false );
	    else if (obj.detachEvent)
	    {
		    obj.detachEvent( "on"+type, obj[type+fn] );
		    obj[type+fn] = null;
		    obj["e"+type+fn] = null;
	    }
    }

var Sandbox = {
		DataFiltersContains : {
			match : function(comboBox, object) {
				if (comboBox.dataFilterValue) {
					var value = comboBox.dataFilterValue.toLowerCase();
					var objTitle = jQuery.trim(object.title).toLowerCase();
					return objTitle.indexOf(value) !== -1;
				}
				return true;
			}
		},
		forEach : function(array,func){
			var i = 0;
			for(i;i<array.length;i++){
				func(array[i]);
			}
		},
		/**
		 * Nothing. Its a cached default callback/null function.
		 * 
		 */
		nothing : function(x){ return x;},
		/**
		 * Wraps a function in a mechanism that allows it be called once and only once (per page refresh)
		 * 
		 * @param [Function] func - a function
		 * @returns a function that can be called once
		 */
		onlyOnce : function(func){
			var result;
			function theSameThing(){
				return result;
			}
			return function(){
				var f = func;
				func = theSameThing;
				result = f.apply(this,arguments);
				return result;
			}
		},
		ChangeLogViewer : {
			
			/**
			 * Initialize the TableViewer.
			 */
			render: function(element, data) {
				
				var html = "";
				var chgLog = jQuery.parseJSON(data.val());
				for (var i = 0; i < chgLog.length; i++) {
					var change = chgLog[i];
					html += "<div class=\"pls_changeLog\"><div class=\"pls_changeLog_user\">";
					html += "<b>" + change.person + "</b> updated <b>" + change.date + "</b>";
					html += "</div><div class=\"pls_changeLog_content\">";
					/* build list of changes */
					html +="<table class=\"pls_changeLog_table\"><tr><th>Attribute</th><th>Old Value</th><th>New Value</th></tr>";
					for (var a = 0; a < change.data.data.length; a++) {
						var fld = change.data.data[a];
						html += "<tr><td width=\"160px\">" + fld.field + "</td>";
						html += "<td width=\"300px\" class=\"pls_changeLog_oldValue\">" + fld.old + "</td>";
						html += "<td width=\"300px\">" + fld["new"] + "</td></tr>";
					}
					html += "</table>";
					html += "";
					html += "</div></div>";
				}
				element.html(html);
			}
	},	
	PeopleListRenderer : {
		getLabel : function(obj) {
			var s = "";
			
			s += "<b>" + obj.title +  "</b><br>";
			s += "<div style=\"margin-left: 5px; font-size: 8pt; color: #808080;\">";
			
			s += obj.object.zusatz + "<br>";
			
			var addedBr = false;
			
			if (obj.object.phone != "" && obj.object.phone != null) {
				s += "D: " + obj.object.phone + "<br>";
				addedBr = true;
			}
			if (obj.object.mobile != "" && obj.object.mobile != null) {
				s += "M: " + obj.object.mobile + "<br>";
				addedBr = true;
			}
			
			s += "</div>";
			return s;
		}
	},
	
	/**
	 * Handles scrolling in SER table control
	 */
	serScroll : function scroll(data, header, leftCol){
		
        var x = data.scrollLeft;
        var y = data.scrollTop;
        jQuery('#'+ header).scrollLeft(x);
        jQuery('#' + leftCol).scrollTop(y);
    },
	
	/**
	 * TODO this could be a global utility function. Maybe provide another file/namespace.
	 * 
	 * Gets a namespaec if it exists or creates the whole namespace hierechy.
	 */
	getOrCreateNamespace : function(fullyQualifiedName) {
	    var namespaces = fullyQualifiedName.split('.');
	    var parentNamespace = window;
	    
	    for(var i = 0; i<namespaces.length; i++){
	    	var localName = namespaces[i];
	    	var ns = parentNamespace[localName];
	        if (ns === undefined || ns === null)
	            parentNamespace[localName] = (ns = {});
	        parentNamespace = ns;
	    }
	    
	    return parentNamespace;
	},
	
	/**
	 * Used at least by the timeentry module to synchronize script loading.
	 * Tags are known strings as we cannot guarantee order of loading.
	 * Sometimes pages are laoded inside out (child controls first), sometimes
	 * the other way round...so this synchronizer seems to be needed. 
	 */
	Synchronizer : {
		
		registry: {},
		
		loaded: {},
		
		/**
		 * The func will be executed as soon as the notify is called. 
		 */
		waitForLoad: function(tag, func){
			var sync = Sandbox.Synchronizer;
			
			Sandbox.log("waitForLoad: " + tag + " has been loaded = " + sync.loaded[tag]);
			if(sync.loaded[tag]){
				func();
			}else{
				var ar = sync.registry[tag];
				if(!ar){
					ar = [];
					sync.registry[tag] = ar;
				}
				ar.push(func);
				Sandbox.log("waiting for load: " + tag);
			}
		},
		
		/**
		 * Calls all functions registered with the given tag.
		 */
		notifyLoad: function(tag){
			var sync = Sandbox.Synchronizer;
			sync.loaded[tag] = true;

			Sandbox.log("Notifying load: " + tag);
			
			var ar = sync.registry[tag];
			if(ar){
				for(var i=0; i<ar.length; i++){
					ar[i]();
				}
			}
			sync.registry[tag] = null;
		},
		
		notifyDestroy: function(tag){
			Sandbox.Synchronizer.loaded[tag] = null;
		}
	},
	
	Resources: {
		_loadedFiles: {},
		
		loadJs: function(fileName){
			if(this._loadedFiles[fileName]){
				Sandbox.log("already loaded: " + fileName);
				return;
			}
			Sandbox.log("loading: " + fileName);
			this._loadedFiles[fileName] = true;
			
			var s = jQuery(document.createElement("script"));
			s.attr("type", "text/javascript");
			s.attr("src", fileName);
			jQuery(document).find("head").append(s);
		}
	},
	
	log: function(s){
		if(this.DEBUG){
			if(window.console){
				console.log(s);
			}
		}
		
	},
	
	debug: function(s){
		if(this.DEBUG){
			if(window.console && window.console.debug){
				console.debug(s);
			}
			
			var ie6console = jQuery("#timeentry_cons");
			
			if(ie6console != undefined){
				var txtElement = jQuery(document.createElement("div"));
				txtElement.html(s);
				
				ie6console.append(txtElement);
			}
		}
	},
	DEBUG : false
}
/**
 * Load the google maps api. only once
 * (its defined here because i want to make use of the only once function)
 * @param clientId - our client id
 * @returns undefined
 */
Sandbox.maps =  Sandbox.onlyOnce(function maps(clientId){
	var script = document.createElement('script');
//	script.src = 'http://maps.googleapis.com/maps/api/js?client='+clientId+'&sensor=false&v=3.13&callback=Sandbox\.maps\.load';//need to add url into google maps account
	script.src = 'http://maps.googleapis.com/maps/api/js?sensor=false&v=3.13&callback=Sandbox\.maps\.load';//for dev and stage we can use free version
	document.body.appendChild(script);
	return Sandbox.maps;
});

Sandbox.maps.load = (function($){
	var callbacks = [];
	var loaded = false;
	

	
	function executeCallbacks(){
		$.each(callbacks,function(i,e){
			e(google.maps);
		});
	}
	
	function event(){
		executeCallbacks();
		loaded = true;
	}
	
	
	function addCallback(callback){
		if(loaded){
			window.setTimeout(function(){
				callback(google.maps);
			},0);
		}else{
			callbacks.push(callback);
		}
		
	}
	
	event.addCallback = addCallback;
	
	return event;
}(jQuery));

/*
 * converts a function of n arguments to a function that while it doenst have all arguments it keep return a function that takes the rest
 * e.g.
 * 
 * var add = curry(function(a,b){
 * 		return a+b;
 * });
 * 
 * add(1,2) === 3
 * add(1)(2) == 3
 * add(1)()(2) = 3
 * 
 * 
 */
Sandbox.curry = (function(){
	var slice = Array.prototype.slice;
	function curry(func, length) {
		length = length || func.length;

		return function() {
			var args = slice.call(arguments),
				len = args.length;

			if (len >= length) {
				return func.apply(this, args);
			}

			return curry(function() {
				return func.apply(this, args.concat(slice.call(arguments)));
			}, length - len);
		};
	}
	
	return curry;
}())
