package com.assignment.demo.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.demo.Utility.DatabaseUtil;



@RestController
@RequestMapping("/api/multiple")
public class SessionMeeting {
	
	@Autowired
 	private DatabaseUtil db = new DatabaseUtil();

    @PostMapping("/add")
    public HashMap<String, Object> addEvent(@RequestBody Map<String, Object> requestData) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "The specified slot is not available, please select another slot.");
        
        // this can be stored in config

        int dayOfMonth = (int) requestData.get("dayOfMonth");
        int month = (int) requestData.get("month");
        int year = (int) requestData.get("year");
        int session_no = (int) requestData.get("session_no");
        int slot_limit= (int) requestData.get("slot_limit");
        String organiser = requestData.get("organiser").toString();

    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    	LocalDate start = LocalDate.of(year, month, dayOfMonth);

		try {
			String validateQuery = "SELECT * FROM events WHERE organiser='" + organiser
					+ "' AND type='session' AND session_no="+session_no+" AND " + "(start_time = '" + start + "');";

			List<Map<String, Object>> data = db.executeSelectQuery(validateQuery);
			String insertQuery  = "";
			if (data.isEmpty()) {
				insertQuery = "INSERT INTO events (start_time, title, type, session_no, slot_limit, slot_occupied, organiser) VALUES ('"
						+ start + "', 'Session with " + organiser + "' ,'session', " + session_no +", "+ slot_limit + ","
						+ 1 + ", '" + organiser + "');";
			} else if (!data.isEmpty()) {
				Map<String,Object> row = data.get(0);
				int slot_occupied = (int) row.get("slot_occupied");
				
				int id = (int) row.get("id");
				if(slot_occupied>=slot_limit) {
					return response;
				}
				insertQuery = "UPDATE events SET " + " slot_occupied = " + (slot_occupied + 1) + " WHERE organiser = '"
						+ organiser + "' AND id = " + id;
			}
			int id = db.executeUpdateQuery(insertQuery);
			response.put("success", true);
			response.put("message", "Your slot has been booked, please be available on " + start.getDayOfWeek() + ", " +start);
		} catch (Exception e) {
			response.put("message", "Something went wrong please try again later");
		}

        return response;
    }
}