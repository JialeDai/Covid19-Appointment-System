package edu.nyu.covid19vaccinationsignupsystem.exceptions;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(Integer id) {
        super("Could not find patient id: "+id);
    }
    public PatientNotFoundException(String email) { super("Could not find patient email: "+ email); }
}
