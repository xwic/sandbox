package de.xwic.system.model.roles.editor.model;

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

/**
 * 
 *
 * @author Aron Cotrau
 */
public class OtherControlId {

	private String scopeId;
	private String lblId;
	private String chkAccessId;
	private String chkExecuteId;

	private boolean access = false;
	private boolean execute = false;

	/**
	 * @return the access
	 */
	public boolean isAccess() {
		return access;
	}

	/**
	 * @param access
	 *            the access to set
	 */
	public void setAccess(boolean access) {
		this.access = access;
	}

	/**
	 * @return the execute
	 */
	public boolean isExecute() {
		return execute;
	}

	/**
	 * @param execute
	 *            the execute to set
	 */
	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	/**
	 * @return the scopeId
	 */
	public String getScopeId() {
		return scopeId;
	}

	/**
	 * @param scopeId
	 *            the scopeId to set
	 */
	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	/**
	 * @return the lblId
	 */
	public String getLblId() {
		return lblId;
	}

	/**
	 * @param lblId
	 *            the lblId to set
	 */
	public void setLblId(String lblId) {
		this.lblId = lblId;
	}

	/**
	 * @return the chkAccessId
	 */
	public String getChkAccessId() {
		return chkAccessId;
	}

	/**
	 * @param chkAccessId
	 *            the chkAccessId to set
	 */
	public void setChkAccessId(String chkAccessId) {
		this.chkAccessId = chkAccessId;
	}

	/**
	 * @return the chkExecuteId
	 */
	public String getChkExecuteId() {
		return chkExecuteId;
	}

	/**
	 * @param chkExecuteId
	 *            the chkExecuteId to set
	 */
	public void setChkExecuteId(String chkExecuteId) {
		this.chkExecuteId = chkExecuteId;
	}

}
