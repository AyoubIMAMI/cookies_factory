package fr.unice.polytech.cod.interfaces;

import fr.unice.polytech.cod.order.Order;
import fr.unice.polytech.cod.pojo.schedule.Interval;
import fr.unice.polytech.cod.pojo.schedule.Schedule;
import fr.unice.polytech.cod.schedule.*;

import java.util.List;
import java.util.Optional;

public interface ScheduleActions {

    /**
     * Give the available time slots of the Chef
     *
     * @param schedule schedule
     * @param minutes minutes
     * @param numberOfDaysBeforeTheOrder number of days
     * @return the list of available Interval
     */
    List<Interval> getIntervals(Schedule schedule, int minutes, int numberOfDaysBeforeTheOrder);

    DaySlot getDaySlot(Schedule schedule, int numberOfDaysBefore);

    Optional<Order> getOrderToPrepare(Schedule schedule, int numberOfDaysBeforeTheOrder, TimeClock timeClock);
}
