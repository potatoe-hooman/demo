package com.assignment.demo.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.demo.Utility.DatabaseUtil;

@RestController
@RequestMapping("/api/single")
public class OneToOneMeetings {
	
	@Autowired
 	private DatabaseUtil db = new DatabaseUtil();

    @PostMapping("/add")
    public HashMap<String, Object> addEvent(@RequestBody Map<String, Object> requestData) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "The specified slot is not available, please select another slot.");

        int dayOfMonth = (int) requestData.get("dayOfMonth");
        int month = (int) requestData.get("month");
        int year = (int) requestData.get("year");
        int hour = (int) requestData.get("hour");
        int min = (int) requestData.get("min");
        double period = (double) requestData.get("period");
        String organiser = requestData.get("organiser").toString();

    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    	LocalDate sd = LocalDate.of(year, month, dayOfMonth);
    	LocalTime st = LocalTime.of(hour, min);
        LocalDateTime start = LocalDateTime.of(sd,st);
        LocalDateTime end = start.plusMinutes((long) (period * 60));

		try {
			String validateQuery = "SELECT * FROM events WHERE organiser='" + organiser + "' AND type='one-to-one' AND " + "((start_time <= '"
					+ start + "' AND end_time > '" + start + "') OR " + "(start_time < '" + end + "' AND end_time >= '" + end + "'));";

			List<Map<String, Object>> data = db.executeSelectQuery(validateQuery);
			if (data.isEmpty()) {
				String insertQuery = "INSERT INTO events (start_time, end_time, title, type, organiser) VALUES ('" + start + "', '" + end
						+ "', 'Call with "+organiser+"' ,'one-to-one', '" + organiser + "');";
				int id = db.executeUpdateQuery(insertQuery);
				response.put("success", true);
				response.put("message", "Your slot has been booked, please be available on " + start.getDayOfWeek() + ", " +start);
			}
		} catch (Exception e) {
			response.put("message", "Something went wrong please try again later");
		}

        return response;
    }
}
