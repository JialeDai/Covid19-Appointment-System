package edu.nyu.covid19vaccinationsignupsystem.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Integer id) { super("Could not find Appointment id: "+id);};
}
