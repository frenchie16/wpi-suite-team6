/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John French
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.calendar.entitymanagers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.calendar.models.Event;

/**
 * The entity manager for the Event in the Calendar module.
 * 
 * @author John French
 */
public class EventManager implements EntityManager<Event> {

	//database
	private final Data db;
	
	public EventManager(Data db) {
		this.db = db;
	}
	
	@Override
	public Event makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		
		//parse the JSON to get the event:
		final Event newEvent = Event.fromJson(content);
		
		//Store it or throw an exception if storing it fails
		save(s, newEvent);
		
		//return the event we just stored
		return newEvent;
	}

	@Override
	public Event[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		
		UUID idAsUUID = UUID.fromString(id);
		
		Event[] events = (Event[]) db.retrieve(Event.class, Event.ID_FIELD_NAME, idAsUUID).toArray();
		
		//if there are no events in the array throw an exception
		if(events.length < 1 || events[0] == null) {
			throw new NotFoundException();
		}
		
		return events;
	}

	@Override
	public Event[] getAll(Session s) throws WPISuiteException {
		//Get all events from the database.
		//TODO look into using Event.class instead of a dummy event.
		//Probably would require modifying the main WPI Suite a bit.
		List<Model> events = db.retrieveAll(new Event(), s.getProject());
		
		//return the list as an array
		return (Event[]) events.toArray();
	}

	@Override
	public Event update(Session s, String content) throws WPISuiteException {
		
		//We need to update the fields one by one because that's all the Data class supports.
		//REFLECTION TO THE REFLESCUE
		
		//THIS IS HORRIBLE. db4o is entirely the wrong database to be using here.
		
		Event jsonEvent = Event.fromJson(content);
		
		Field[] fields = Event.class.getFields();
		
		for(Field f : fields){
			try {
				db.update(Event.class, Event.ID_FIELD_NAME, jsonEvent.getId(), f.getName(), f.get(jsonEvent));
			} catch(IllegalAccessException e) {
				throw new WPISuiteException("Illegal field access while updating Event!");
			}
		}
		return jsonEvent;
	}

	@Override
	public void save(Session s, Event model) throws WPISuiteException {
		if(!db.save(model, s.getProject())){
			throw new WPISuiteException("Could not save to database");
		}
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		Event deleted = db.delete(getEntity(s, id)[0]);
		return deleted != null;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Event());
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Event()).size();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

}