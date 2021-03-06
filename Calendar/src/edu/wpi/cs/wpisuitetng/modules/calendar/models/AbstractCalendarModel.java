/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Seal Team 6
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.calendar.models;


/**
 * this class contains parts of the constructors for events and commitments that are common between the two
 * as well as the methods that are common between them
 */
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public abstract class AbstractCalendarModel extends AbstractModel {
	
	//Name of ID field
	public static final String OWNER_FIELD_NAME = "owner";
	public static String ID_FIELD_NAME = "id";

	//ID used for database stuff
	private int id;
	
	private String owner; //the username of the owner

	
	/**
	 * Default constructor for AbstractCalendarModel
	 * Initializes id to the next integer in the list
	 * @param isDummy true if this is a dummy object (for passing into Data methods), false if it is a real one
	 */
	public AbstractCalendarModel(String username, boolean isDummy){
		this.owner = username;
		id = isDummy ? -1:0;
	}
	
	/**
	 * Gets the database ID of this object
	 * @return the database ID
	 */
	public final int getId(){
		return id;
	}
	
	/**
	 * gets the owner of this object; necessary because this is  private variable 
	 * @return the owner
	 */
	public final String getOwner() {
		return owner;
	}

	/**
	 * Set the ID of this object (if it has never been set before!), otherwise leave it alone
	 * @param id the new value for the id 
	 * @return true if id change was successful, false if id was already nonzero and nothing was done
	 */
	public final boolean setId(int id){
		if(this.id == 0){
			this.id = id;
			return true;
		}
		return false;
	}
	
	public final void setOwner(String username) {
		this.owner = username;
	}

	@Override
	/**
	 * saves the object
	 */
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * deletes the object
	 */
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	
	public static <T extends AbstractCalendarModel> T fromJSON(String jsonString, Class<T> cls){
		return new Gson().fromJson(jsonString, cls);
	}
	
	public static <T extends AbstractCalendarModel> T[] fromJSONArray(String jsonString, Class<T[]> cls){
		return new Gson().fromJson(jsonString, cls);
	}
	
	//don't override toJSON, doing that gets super messy because we don't know the class we are

	//documentation from AbstractModel is fine, so we don't need a javadoc here
	@Override
	public final Boolean identify(Object o) {
		if(o instanceof Integer){
			return o.equals(getId());
		} else if(o instanceof AbstractCalendarModel){
			return ((AbstractCalendarModel)o).getId() == (getId()); //encapsulation will cover this
		} else {
			return false;
		}
	}

}
