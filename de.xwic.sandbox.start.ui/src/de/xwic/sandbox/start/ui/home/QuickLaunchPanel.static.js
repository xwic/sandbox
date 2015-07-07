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

var QuickLaunchPanel = {
	
	ignoreClick : false,
	fieldId : null,
	initQuickLaunch : function(fldId) {
		
		QuickLaunchPanel.fieldId = fldId;
		JWic.$("gtQuickLaunchItems").sortable({
			  stop: QuickLaunchPanel.qlStopHandler
		});
		JWic.$("gtQuickLaunchItems").disableSelection();
		jQuery("#gtQuickLaunchItems li").click(this.qlClickHandler);
		
	},
	
	qlStopHandler : function(e, ui) {
		JWic.log("bla: " + QuickLaunchPanel.fieldId);
		QuickLaunchPanel.ignoreClick = true;
		// figure out the new position(s)
		var elms = jQuery("#gtQuickLaunchItems").find("li.gtQLItem");
		JWic.log("Build IDS" + elms);
		var ids = "";
		elms.each(function(idx, val) {
			ids += jQuery(val).attr("pQlItemId") + ";";
		});
		JWic.log("SetValues: " + QuickLaunchPanel.fieldId);
		JWic.$(QuickLaunchPanel.fieldId).val(ids);
	},
	
	qlClickHandler : function(e) {
		if (QuickLaunchPanel.ignoreClick) {
			QuickLaunchPanel.ignoreClick = false;
		} else {
			JWic.log(e);
			var card = jQuery(e.currentTarget);
			var res = card.attr("pQlItemRes")
			var ctrlId = card.attr("pQlCtrlId");
			JWic.fireAction(ctrlId, "launch", res);
		}
	}
}