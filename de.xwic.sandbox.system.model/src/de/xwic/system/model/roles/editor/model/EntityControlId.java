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
 * @author Aron Cotrau
 */
public class EntityControlId {

	private String scopeId;
	private String lblId;
	private String className;
	private String chkReadId;
	private String chkUpdateId;
	private String chkCreateId;
	private String chkDeleteId;

	private boolean read = false;
	private boolean update = false;
	private boolean create = false;
	private boolean delete = false;

	/**
	 * 
	 */
	public EntityControlId() {
	}

	/**
	 * @return the read
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read
	 *            the read to set
	 */
	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update
	 *            the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * @return the create
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * @param create
	 *            the create to set
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete() {
		return delete;
	}

	/**
	 * @param delete
	 *            the delete to set
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
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
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the chkReadId
	 */
	public String getChkReadId() {
		return chkReadId;
	}

	/**
	 * @param chkReadId
	 *            the chkReadId to set
	 */
	public void setChkReadId(String chkReadId) {
		this.chkReadId = chkReadId;
	}

	/**
	 * @return the chkUpdateId
	 */
	public String getChkUpdateId() {
		return chkUpdateId;
	}

	/**
	 * @param chkUpdateId
	 *            the chkUpdateId to set
	 */
	public void setChkUpdateId(String chkUpdateId) {
		this.chkUpdateId = chkUpdateId;
	}

	/**
	 * @return the chkCreateId
	 */
	public String getChkCreateId() {
		return chkCreateId;
	}

	/**
	 * @param chkCreateId
	 *            the chkCreateId to set
	 */
	public void setChkCreateId(String chkCreateId) {
		this.chkCreateId = chkCreateId;
	}

	/**
	 * @return the chkDeleteId
	 */
	public String getChkDeleteId() {
		return chkDeleteId;
	}

	/**
	 * @param chkDeleteId
	 *            the chkDeleteId to set
	 */
	public void setChkDeleteId(String chkDeleteId) {
		this.chkDeleteId = chkDeleteId;
	}

}
