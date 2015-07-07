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
 */
package de.xwic.sandbox.basegui.selectors.people;

import org.json.JSONException;
import org.json.JSONWriter;

import de.jwic.json.IObjectToJsonSerializer;
import de.xwic.appkit.core.model.entities.IMitarbeiter;

/**
 * @author Raluca Geogia
 *
 */
public class EmployeeJsonSerializer implements IObjectToJsonSerializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.json.IObjectToJsonSerializer#serialize(java.lang.Object, org.json.JSONWriter)
	 */
	@Override
	public void serialize(Object object, JSONWriter out) throws JSONException {

		IMitarbeiter mit = (IMitarbeiter) object;

		out.object();

		out.key("zusatz");
		out.value(mit.getZusatz() != null ? mit.getZusatz() : "");

		out.key("phone");
		out.value(mit.getTelefon());

		out.key("mobile");
		out.value(mit.getHandyNr());

		out.key("email");
		out.value(mit.getEmail());

		out.endObject();

	}

}
