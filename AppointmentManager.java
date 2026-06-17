

import java.util.ArrayList;

public class AppointmentManager {

    private ArrayList<Appointment> appointments;

    public AppointmentManager() {
        appointments = new ArrayList<>();
    }

    public boolean addAppointment(Appointment appointment) {

        for (Appointment a : appointments) {

            if (a.getDoctor().getDoctorID()
                    .equals(appointment.getDoctor().getDoctorID())
                    &&
                a.getDate().equals(appointment.getDate())
                    &&
                a.getTime().equals(appointment.getTime())) {

                return false;
            }
        }

        appointments.add(appointment);
        return true;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }
}