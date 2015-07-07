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

    var mainMenu = {
    	mode : 'child', /** 'parent'/'child' **/
        items : new Array(),
        activeModule : "",
        baseItem : "App",
        controlId : "",
        allItems : new Array(),
        level : 0,
        showDataLostMessage : false,
        menuStartLevel : -1,
        activeSubMenu : new Array(),
        doCloseSM : false,
        isIE6 : navigator.appVersion.indexOf("MSIE 6.") != -1,
        
        hideSubMenusTaskIds : new Array(),
        
        add : function(nId, title) {
            var item = new MenuItem(nId, title);
            item.parent = this;
            item.level = 1;
            this.items[this.items.length] = item;
            this.allItems[item.fullId] = item;
            return item;
        },
        clear : function () {
            this.items = new Array();
        },
        /**
         * Redraw the entire menu.
         */
        update : function() {
            
            var _divMain = document.getElementById("h_menu");
            var sHtml = "";
            var moduleIds = this.activeModule.split(";");

            sHtml = "<div id=\"h_menu_0\" elmId=\"\" class=\"h_item h_root\"";
            if (mainMenu.mode == 'child') {
	            sHtml += " onMouseOver=\"mainMenu.hover(this, '', false, true)\"";
	            sHtml += " onMouseOut=\"mainMenu.hover(this, '', false, false)\"";
            }
            sHtml += ">" + this.baseItem + "</div>";

            sHtml += this.renderActive(this.items, moduleIds, 0);
            _divMain.innerHTML = sHtml;
            
        },
        renderActive : function(theItems, theIds, level) {
        	var sHtml = "";
        	var foundItem = null;
            for (var i = 0; i < theItems.length; i++) {
                var item = theItems[i];
                if (item.id == theIds[level]) {
                	foundItem = item;
                    sHtml += item.toHTML(false);
                    break;
                }
            }
            if (foundItem != null && theIds.length > level) {
            	sHtml += this.renderActive(foundItem.items, theIds, level + 1);
            }
            return sHtml;
        },
        hover : function(divElm, theId, isSub, over) {
        	
        	if(over){
        		mainMenu.menuMouseOver();
        	}
        	else{
        		mainMenu.menuMouseOut();
        	}
        	
        	divElm = jQuery(divElm);
            var itm = theId == '' ? mainMenu : mainMenu.allItems[theId];
            if (itm) {
            	if (!isSub && mainMenu.mode != 'child') {
            		itm = itm.parent;
            	}
            	var clName = isSub ? "h_item_hover_sub" : "h_item_hover";
            	if (over) {
	            	divElm.addClass(clName);	            	
	            } else {
	            	divElm.removeClass(clName);
	            }

            	var myLevel = itm.level - mainMenu.menuStartLevel;
            	
            	var hasChilds = itm.items.length > 0; // display menu
            	if (hasChilds) {
	            	
		            if (over) { // mouse hovering over the menu
			            // get item
			            if (!isSub) {
		            		mainMenu.openSubMenu(divElm, itm, 0);
			            } else {
		            		mainMenu.openSubMenu(divElm, itm, myLevel);
			            }
		            } else if (!isSub) {
		            	//mainMenu.doCloseSM = true;   	
		            	//window.setTimeout("mainMenu.hideSubMenues(false, 0)", 1000);
		            }
		            
            	} else {
            		mainMenu.hideSubMenues(true, myLevel);
            	}
            }
            
            
        },
        /**
         * Open a SubMenu div...
         */
        openSubMenu : function (_parentElm, menuItem, level) {
        	
        	mainMenu.hideSubMenues(true, level);
        	
        	if (mainMenu.menuStartLevel == -1) {
        		mainMenu.menuStartLevel = menuItem.level;
        	}
        	
        	var parentElm = jQuery(_parentElm);
        	var myBox = jQuery(document.createElement('div')).css({position:'absolute'});
        	//Element.extend(myBox);
        	myBox.addClass("h_menu_subbox");
        	
        	var sHtml = "";
            for (var i = 0; i < menuItem.items.length; i++) {
                var item = menuItem.items[i];
                sHtml += item.toHTML(true);
                if (item.id == this.activeId) {
                    activeItem = item;
                }
            }
            var parentLoc = parentElm.offset();
            myBox.html(sHtml);
        	if (level == 0) {
        		if (mainMenu.mode == 'child') {
		        	myBox.offset({
		    			top: (parentLoc.top + parentElm.height()),
		    			left: (parentLoc.left + (parentElm.width() / 2) - 10)
		        	});
        		} else {
		        	myBox.offset({
		    			top: (parentLoc.top + parentElm.height()),
		    			left: (parentLoc.left - 10)
		        	});
        		}
        	} else {
            	myBox.offset({
        			top: parentLoc.top,
        			left: (parentLoc.left + parentElm.width())
            	});
        	}
        	jQuery(document.body).append(myBox);
        	this.activeSubMenu[this.activeSubMenu.length] = myBox;
        	
        	myBox.bind("mouseover", this.menuMouseOver);
        	myBox.bind("mouseout", this.menuMouseOut);
        	
        },
        menuMouseOver : function() {
        	
        	mainMenu.doCloseSM = false;
        	for ( var i = 0; i < mainMenu.hideSubMenusTaskIds.length; i++) {
        		var id = mainMenu.hideSubMenusTaskIds[i];
        		window.clearTimeout(id);
        		
			}
        	
        	mainMenu.hideSubMenusTaskIds = [];
        	
        },
        menuMouseOut : function() {
        	
        	var id = window.setTimeout(function(){
        		mainMenu.hideSubMenues( false, 0);        		
        	},250);
    		mainMenu.hideSubMenusTaskIds.push(id);
        },
        /*
         * Select a main menu item.
         */
        select : function(itemId) {
            
            var item = mainMenu.allItems[itemId];
            if (typeof item.href != 'undefined') {
            	JWic.fireAction(this.controlId, 'selectMenu', item.href);
            	mainMenu.activeModule = item.href;
                this.update();
            }
            mainMenu.hideSubMenues(true, 0);
        },
        /**
         * @param force is unused TODO clean it up
         */
        hideSubMenues : function(force, level) { 
        	
        	if (!level) {
        		level = 0;
        	}
        	if (this.activeSubMenu.length > level) {
        		for (var i = level, len = this.activeSubMenu.length; i < len; ++i) {
        			var menuBox = this.activeSubMenu[i];
        			menuBox.unbind("mouseover", this.menuMouseOver);
        			menuBox.unbind("mouseout", this.menuMouseOut);
        			menuBox.remove();
        		}
        		this.activeSubMenu.splice(level, this.activeSubMenu.length - level);
            	if (level == 0) {
            		mainMenu.menuStartLevel = -1;
            	}
        	}
        	this.doCloseSM = false;
        }
    }
        
    function MenuItem(nId, nTitle, nHref) {
        this.baseStyle = "h_menu_item";
        this.isSub = false;
        this.id = nId;
        this.fullId = nId;
        this.title = nTitle;
        this.href = nHref;
        this.items = new Array();
        this.level = 0;
        this.hasSubModules = false;
        this.parent = null;
        this.add = function(nId, nTitle, nHref, hasSubModules) {
            var item = new MenuItem(nId, nTitle, nHref);
            item.parent = this;
            item.baseStyle = "h_menu_subitem";
            item.isSub = true;
            item.fullId = this.fullId + ";" + nId;
            item.level = this.level + 1;
            this.items[this.items.length] = item;
            mainMenu.allItems[item.fullId] = item;
            item.hasSubModules=hasSubModules;
            return item;
        }
        this.toHTML = function(isWinMen) {
        	
            var sHtml = "";
            sHtml += "<div ";
            if (isWinMen) {
            	sHtml += "class=\"h_sm_item";
            } else {
            	sHtml += "class=\"h_item";
            }
            sHtml += "\" id=\"hhelm_" + this.id + "\"";
            sHtml += " onMouseOver=\"mainMenu.hover(this, '" + this.fullId + "', " + isWinMen + ", true)\"";
            sHtml += " onMouseOut=\"mainMenu.hover(this, '" + this.fullId + "', " + isWinMen + ", false)\"";
            if (this.items.length == 0) {
            	sHtml += "onClick=\"mainMenu.select('" + this.fullId + "')\"";
            }
            sHtml += ">";
            sHtml += "<div class=\"hmi_body";
            if (this.items.length > 0) {
            	sHtml += " hmi_hasChilds";
            }
            if (mainMenu.activeModule.indexOf(this.fullId) == 0) {//this is not right. we should not do menus with strings
            	sHtml += " hmi_isSelected";
            }
            sHtml += "\">";
           	sHtml += this.title + "</div>";
            sHtml += "</div>";
            
            return sHtml;
        }
    }
    

 